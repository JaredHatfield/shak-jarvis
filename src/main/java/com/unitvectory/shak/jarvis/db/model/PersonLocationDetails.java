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
     * Creates a new instance of the PersonLocationDetails class.
     * 
     * @param firstName
     *            the first name
     * @param lastName
     *            the last name
     * @param home
     *            the home id
     */
    public PersonLocationDetails(String firstName, String lastName, int home) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.home = home;
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

}
