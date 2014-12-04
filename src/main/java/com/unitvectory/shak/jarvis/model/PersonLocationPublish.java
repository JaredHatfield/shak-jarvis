package com.unitvectory.shak.jarvis.model;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The person location publish
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationPublish {

	/**
	 * The JsonPublishRequest used to created this object
	 */
	private JsonPublishRequest publish;

	/**
	 * the token
	 */
	private String token;

	/**
	 * the location
	 */
	private String location;

	/**
	 * the status
	 */
	private char status;

	/**
	 * the date
	 */
	private Date date;

	/**
	 * Creates a new instance of the PersonLocationPublish class.
	 * 
	 * @param publish
	 *            the publish
	 */
	public PersonLocationPublish(JsonPublishRequest publish) {
		this.publish = publish;
		this.token = publish.getData().get("token");
		this.location = publish.getData().get("location");
		String statusString = publish.getData().get("status");
		if (statusString == null || statusString.length() == 0) {
			status = ' ';
		} else {
			status = statusString.charAt(0);
		}

		String dateString = publish.getData().get("date");
		if (dateString == null) {
			dateString = publish.getTimestamp();
		}

		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			this.date = df.parse(dateString);
		} catch (ParseException e) {
			this.date = new Date();
		}
	}

	/**
	 * @return the publish
	 */
	public JsonPublishRequest getPublish() {
		return publish;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Gets the timestamp
	 * 
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		java.sql.Date sqlDate = new java.sql.Date(this.date.getTime());
		return new Timestamp(sqlDate.getTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Location " + this.token + " " + this.status + " "
				+ this.location;
	}

}
