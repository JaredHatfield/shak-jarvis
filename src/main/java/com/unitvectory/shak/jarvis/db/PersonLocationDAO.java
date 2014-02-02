package com.unitvectory.shak.jarvis.db;

import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface PersonLocationDAO {

    /**
     * Gets a person's name.
     * 
     * @param token
     *            the token
     * @return the person name
     */
    String getPersonName(String token);

    /**
     * Inserts a location.
     * 
     * @param publish
     *            the public
     * @return the insert result
     */
    InsertResult insertLocation(PersonLocationPublish publish);
}
