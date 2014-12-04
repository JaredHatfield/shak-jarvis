package com.unitvectory.shak.jarvis;

import java.io.File;

import org.apache.log4j.Logger;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * The app configuration.
 * 
 * @author Jared Hatfield
 * 
 */
@Root(name = "config")
public class AppConfig {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(AppConfig.class);

	/**
	 * the aws access key
	 */
	@Element(name = "awsaccesskey")
	private String awsAccessKey;

	/**
	 * the aws secret key
	 */
	@Element(name = "awssecretkey")
	private String awsSecretKey;

	/**
	 * the queue url
	 */
	@Element(name = "queueurl")
	private String queueUrl;

	/**
	 * the database user
	 */
	@Element(name = "dbuser")
	private String dbUser;

	/**
	 * the database password
	 */
	@Element(name = "dbpassword")
	private String dbPassword;

	/**
	 * the database url
	 */
	@Element(name = "dburl")
	private String dbUrl;

	/**
	 * the pushover app key
	 */
	@Element(name = "pushover", required = false)
	private String pushover;

	/**
	 * Creates a new instance of the AppConfig class.
	 */
	public AppConfig() {
	}

	/**
	 * @return the awsAccessKey
	 */
	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	/**
	 * @return the awsSecretKey
	 */
	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	/**
	 * @return the queueUrl
	 */
	public String getQueueUrl() {
		return queueUrl;
	}

	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @return the dbUrl
	 */
	public String getDbUrl() {
		return dbUrl;
	}

	/**
	 * @return the pushover
	 */
	public String getPushover() {
		return pushover;
	}

	/**
	 * Load the config file.
	 * 
	 * @param file
	 *            the file path
	 * @return the config; null if not loaded
	 */
	public static AppConfig load(String file) {
		try {
			Serializer serializer = new Persister();
			return serializer.read(AppConfig.class, new File(file), false);
		} catch (Exception e) {
			log.error("Unable to load config file.", e);
			return null;
		}
	}
}
