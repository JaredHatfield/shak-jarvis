package com.unitvectory.shak.jarvis.db;

import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface PersonLocationDAO {

    String getPersonName(String token);

    InsertResult insertLocation(PersonLocationPublish publish);
}
