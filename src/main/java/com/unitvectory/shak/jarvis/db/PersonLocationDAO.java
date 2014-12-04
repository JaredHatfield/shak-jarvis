package com.unitvectory.shak.jarvis.db;

import java.util.List;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface PersonLocationDAO {

	/**
	 * Get the recent location for a person.
	 * 
	 * @param token
	 *            the token
	 * @return the recent location
	 */
	PersonLocationRecent getRecentLocation(String token);

	/**
	 * Gets all of the people in a home.
	 * 
	 * @param home
	 *            the home
	 * @return the list of people
	 */
	List<PersonLocationDetails> getPeople(int home);

	/**
	 * Gets a person's name.
	 * 
	 * @param token
	 *            the token
	 * @return the person details
	 */
	PersonLocationDetails getPerson(String token);

	/**
	 * Inserts a location.
	 * 
	 * @param publish
	 *            the public
	 * @return the insert result
	 */
	InsertResult insertLocation(PersonLocationPublish publish);
}
