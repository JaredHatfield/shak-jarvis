package com.unitvectory.shak.jarvis.db.model;

import java.util.Date;

/**
 * The recent person location
 * 
 * @author Jared Hatfield
 *
 */
public class PersonLocationRecent {

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
	 * the occurred date time
	 */
	private Date occurred;

	/**
	 * Creates a new instance of the PersonLocationRecent class.
	 * 
	 * @param token
	 *            the token
	 * @param location
	 *            the location
	 * @param status
	 *            the status
	 * @param occurred
	 *            the occurred date time
	 */
	public PersonLocationRecent(String token, String location, char status,
			Date occurred) {
		this.token = token;
		this.location = location;
		this.status = status;
		this.occurred = new Date(occurred.getTime());
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
	 * @return the occurred
	 */
	public Date getOccurred() {
		return new Date(occurred.getTime());
	}
}
