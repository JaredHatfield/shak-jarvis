package com.unitvectory.shak.jarvis;

import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.buffered.QueueBufferConfig;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.unitvectory.shak.jarvis.db.PersonLocationDAO;
import com.unitvectory.shak.jarvis.db.PersonLocationDatabase;
import com.unitvectory.shak.jarvis.db.SmartThingsDAO;
import com.unitvectory.shak.jarvis.db.SmartThingsDatabase;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;

/**
 * The main thread.
 * 
 * @author Jared Hatfield
 * 
 */
public class MainThread extends Thread {

    /**
     * the log
     */
    private static Logger log = Logger.getLogger(MainThread.class);

    /**
     * the running flag.
     */
    private boolean running;

    /**
     * the done flag
     */
    private boolean done;

    /**
     * The app config.
     */
    private AppConfig config;

    /**
     * the data source
     */
    private BasicDataSource dataSource;

    /**
     * the event processor
     */
    private HomeEventProcessor eventProcessor;

    /**
     * the sqs client
     */
    private AmazonSQSAsync sqs;

    /**
     * Creates a new instance of MainThread.
     * 
     * @param config
     *            the config
     */
    public MainThread(AppConfig config) {
        this.config = config;
        this.done = false;
        synchronized (this) {
            this.running = true;
        }

        // Connect to the database
        this.dataSource = new BasicDataSource();
        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setValidationQuery("SELECT 1");
        this.dataSource.setUsername(this.config.getDbUser());
        this.dataSource.setPassword(this.config.getDbPassword());
        this.dataSource.setUrl(this.config.getDbUrl());
        this.dataSource.setInitialSize(1);

        SmartThingsDAO st = new SmartThingsDatabase(this.dataSource);
        PersonLocationDAO pl = new PersonLocationDatabase(this.dataSource);

        // Make the event processor
        this.eventProcessor = new HomeEventProcessor(st, pl);

        // Make the SQS Client
        AmazonSQSAsyncClient asyncSQS =
                new AmazonSQSAsyncClient(new BasicAWSCredentials(
                        this.config.getAwsAccessKey(),
                        this.config.getAwsSecretKey()));
        asyncSQS.setRegion(Region.getRegion(Regions.US_EAST_1));
        QueueBufferConfig queueBufferConfig = new QueueBufferConfig();
        queueBufferConfig.setMaxInflightReceiveBatches(1);
        this.sqs =
                new AmazonSQSBufferedAsyncClient(asyncSQS, queueBufferConfig);
    }

    @Override
    public void run() {
        boolean runningThread = true;
        log.info("Starting thread.");
        while (runningThread) {

            // Receive messages from the queue
            int count = 1;
            ReceiveMessageRequest receiveMessageRequest =
                    new ReceiveMessageRequest(this.config.getQueueUrl());
            ReceiveMessageResult receiveMessageResult =
                    sqs.receiveMessage(receiveMessageRequest);
            List<Message> messages = receiveMessageResult.getMessages();

            // Loop through all of the messages
            for (Message message : messages) {
                // Process the message
                JsonPublishRequest publishRequest =
                        new JsonPublishRequest(message);
                eventProcessor.processEvent(publishRequest);

                // Done with the message
                sqs.deleteMessageAsync(new DeleteMessageRequest(this.config
                        .getQueueUrl(), message.getReceiptHandle()));
            }

            count = messages.size();
            if (count == 0) {
                log.info("Nothing...");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Safe to ignore
                }
            }

            // Decide if to keep running
            synchronized (this) {
                runningThread = this.running;
            }
        }

        this.done = true;
        log.info("Thread ended.");
    }

    /**
     * Done running the main thread.
     */
    public void done() {
        synchronized (this) {
            this.running = false;
        }

        // Wait for the thread to finish
        while (!this.done) {
        }
    }
}
