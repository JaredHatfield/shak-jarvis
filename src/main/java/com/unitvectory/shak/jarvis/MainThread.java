package com.unitvectory.shak.jarvis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
import com.unitvectory.shak.jarvis.db.ShakDatabase;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
import com.unitvectory.shak.jarvis.pushover.PushOver;
import com.unitvectory.shak.jarvis.pushover.PushOverClient;
import com.unitvectory.shak.jarvis.pushtospeech.PushToSpeechClient;

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
	private AtomicBoolean running;

	/**
	 * the done flag
	 */
	private AtomicBoolean done;

	/**
	 * The app config.
	 */
	private AppConfig config;

	/**
	 * the database.
	 */
	private ShakDatabase database;

	/**
	 * the event processor
	 */
	private HomeEventProcessor eventProcessor;

	/**
	 * the sqs client
	 */
	private AmazonSQSAsync sqs;

	/**
	 * the pushover client
	 */
	private PushOver pushover;

	/**
	 * the executor service
	 */
	private ExecutorService executor;

	/**
	 * Creates a new instance of MainThread.
	 * 
	 * @param config
	 *            the config
	 */
	public MainThread(AppConfig config) {
		this.config = config;
		this.done = new AtomicBoolean(false);
		this.running = new AtomicBoolean(true);

		this.database = new ShakDatabase(config);

		this.executor = Executors.newCachedThreadPool();

		// The pushover client
		if (config.getPushover() != null && config.getPushover().length() > 0) {
			this.pushover = new PushOverClient(config.getPushover());
		}

		// Make the event processor
		this.eventProcessor = new HomeEventProcessor(this.database,
				new PushToSpeechClient(), this.pushover, this.executor);

		// Make the SQS Client
		AmazonSQSAsyncClient asyncSQS = new AmazonSQSAsyncClient(
				new BasicAWSCredentials(this.config.getAwsAccessKey(),
						this.config.getAwsSecretKey()));
		asyncSQS.setRegion(Region.getRegion(Regions.US_EAST_1));
		QueueBufferConfig queueBufferConfig = new QueueBufferConfig();
		queueBufferConfig.setMaxInflightReceiveBatches(1);
		this.sqs = new AmazonSQSBufferedAsyncClient(asyncSQS, queueBufferConfig);
	}

	@Override
	public void run() {
		log.info("Starting thread.");
		while (this.running.get()) {

			// Make sure we can connect to the database
			if (!this.database.isConnected()) {
				try {
					log.info("Database not connected... waiting for 10 seconds...");
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// Safe to ignore
				}

				// Keep going...
				continue;
			}

			// Receive messages from the queue
			int count = 1;
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
					this.config.getQueueUrl());
			ReceiveMessageResult receiveMessageResult = sqs
					.receiveMessage(receiveMessageRequest);
			List<Message> messages = receiveMessageResult.getMessages();

			// Loop through all of the messages
			for (Message message : messages) {
				// Process the message
				JsonPublishRequest publishRequest = new JsonPublishRequest(
						message);
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
		}

		this.done.set(true);
		log.info("Thread ended.");
	}

	/**
	 * Done running the main thread.
	 */
	public void done() {
		this.running.set(false);

		// Wait for the thread to finish
		while (!this.done.get()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Safe to ignore
			}
		}

		// Shut down the executor gracefully
		this.executor.shutdown();
		try {
			if (!this.executor.awaitTermination(60, TimeUnit.SECONDS)) {
				this.executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			log.error("Failed to shut down executor.", e);
		}

		try {
			this.database.close();
		} catch (Exception e) {
			log.error("Failed to close database connection.", e);
		}
	}
}
