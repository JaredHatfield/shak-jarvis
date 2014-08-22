package com.unitvectory.shak.jarvis.db.model;

/**
 * The person location details
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationDetails {

	/**
	 * the first name
	 */
	private String firstName;

	/**
	 * the last name
	 */
	private String lastName;

	/**
	 * the home id
	 */
	private int home;

	/**
	 * the push over token
	 */
	private String pushOver;

	/**
	 * Creates a new instance of the PersonLocationDetails class.
	 * 
	 * @param firstName
	 *            the first name
	 * @param lastName
	 *            the last name
	 * @param home
	 *            the home id
	 * @param pushOver
	 *            the push over
	 */
	public PersonLocationDetails(String firstName, String lastName, int home,
			String pushOver) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.home = home;
		this.pushOver = pushOver;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the home
	 */
	public int getHome() {
		return home;
	}

	/**
	 * @return the pushOver
	 */
	public String getPushOver() {
		return pushOver;
	}
}
