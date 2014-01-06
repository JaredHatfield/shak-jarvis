package com.unitvectory.shak.jarvis.db;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * The person location database
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationDatabase extends AbstractDatabase implements
        PersonLocationDAO {

    /**
     * Creates a new instance of the PersonLocationDatabase class.
     * 
     * @param ds
     *            the basic data source
     */
    public PersonLocationDatabase(BasicDataSource ds) {
        super(ds);
    }

}
