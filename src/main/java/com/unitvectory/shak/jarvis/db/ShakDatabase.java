package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

import com.unitvectory.shak.jarvis.AppConfig;

/**
 * The shak database interfaces
 * 
 * @author Jared Hatfield
 * 
 */
public class ShakDatabase {

    /**
     * the data source
     */
    private BasicDataSource dataSource;

    /**
     * the smart things DAO
     */
    private SmartThingsDAO st;

    /**
     * the person location DAO
     */
    private PersonLocationDAO pl;

    /**
     * the push to speech DAO
     */
    private PushToSpeechDAO pts;

    /**
     * Creates a new instance of the ShakDatabase class.
     * 
     * @param config
     *            the config
     */
    public ShakDatabase(AppConfig config) {
        // Connect to the database
        this.dataSource = new BasicDataSource();
        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setValidationQuery("SELECT 1");
        this.dataSource.setUsername(config.getDbUser());
        this.dataSource.setPassword(config.getDbPassword());
        this.dataSource.setUrl(config.getDbUrl());
        this.dataSource.setInitialSize(1);

        // Create the DAO objects
        this.st = new SmartThingsDatabase(this.dataSource);
        this.pl = new PersonLocationDatabase(this.dataSource);
        this.pts = new PushToSpeechDatabase(this.dataSource);
    }

    /**
     * @return the smart things dao
     */
    public SmartThingsDAO st() {
        return st;
    }

    /**
     * @return the person location dao
     */
    public PersonLocationDAO pl() {
        return pl;
    }

    /**
     * @return the push to speech dao
     */
    public PushToSpeechDAO pts() {
        return pts;
    }

    /**
     * Tests if the database is still connected.
     * 
     * @return true if connected; otherwise false
     */
    public boolean isConnected() {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            if (connection == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    // Safe to ignore
                }
            }
        }
    }

}
