package com.unitvectory.shak.jarvis;

import java.util.Scanner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.log4j.Logger;

/**
 * The main application.
 * 
 * @author Jared Hatfield
 * 
 */
public class App implements Daemon {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(App.class);

	/**
	 * the main thread
	 */
	private static MainThread mainThread;

	/**
	 * the app config
	 */
	private static AppConfig config;

	/**
	 * The main application.
	 * 
	 * @param args
	 *            the args
	 */
	public static void main(String[] args) {
		// create the parser
		org.apache.commons.cli.CommandLineParser parser = new BasicParser();
		try {
			// The required options
			Options options = new Options();
			@SuppressWarnings("static-access")
			Option configOption = OptionBuilder.withArgName("config").hasArg()
					.withDescription("path to config file").isRequired()
					.create("config");
			options.addOption(configOption);

			// Parse the command line arguments
			CommandLine line = parser.parse(options, args);
			String configPath = line.getOptionValue("config");

			// Load the config
			config = AppConfig.load(configPath);
			if (config == null) {
				return;
			}

			// Start the app
			App app = new App();
			try {
				app.start();
			} catch (Exception e) {
				log.error("Failed to start app.", e);
			}

			// Wait for input to exit
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in, "UTF-8");
			while (!sc.nextLine().equals("")) {
				System.out.println("Press ENTER to exit...");
			}

			// Exit the app
			log.info("Exiting...");
			try {
				app.stop();
			} catch (Exception e) {
				log.error("Failed to stop app.", e);
			}
		} catch (ParseException exp) {
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}
	}

	public void destroy() {
	}

	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		String configPath = "/etc/shak-jarvis/config.xml";
		config = AppConfig.load(configPath);
	}

	public void start() throws Exception {
		mainThread = new MainThread(config);
		mainThread.start();
	}

	public void stop() throws Exception {
		mainThread.done();
	}
}
