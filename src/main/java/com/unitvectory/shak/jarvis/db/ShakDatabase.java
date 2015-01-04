package com.unitvectory.shak.jarvis.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.unitvectory.shak.jarvis.AppConfig;

/**
 * The shak database interfaces
 * 
 * @author Jared Hatfield
 * 
 */
public class ShakDatabase {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(ShakDatabase.class);

	/**
	 * the data source
	 */
	private ComboPooledDataSource dataSource;

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
		this.dataSource = new ComboPooledDataSource();
		try {
			this.dataSource.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			log.error("Unable to load MySQL database driver.", e);
		}

		this.dataSource.setPreferredTestQuery("SELECT 1");
		this.dataSource.setUser(config.getDbUser());
		this.dataSource.setPassword(config.getDbPassword());
		this.dataSource.setJdbcUrl(config.getDbUrl());
		this.dataSource.setInitialPoolSize(1);
		this.dataSource.setMinPoolSize(1);
		this.dataSource.setMaxIdleTimeExcessConnections(120);
		this.dataSource.setIdleConnectionTestPeriod(30);

		// Create the DAO objects
		this.st = new SmartThingsDatabase(this.dataSource);
		this.pl = new PersonLocationDatabase(this.dataSource);
		this.pts = new PushToSpeechDatabase(this.dataSource);
	}

	/**
	 * Creates a new instance of the ShakDatabase class.
	 * 
	 * This is the in memory instance used for testing.
	 */
	public ShakDatabase() {
		this.dataSource = null;
		this.st = new SmartThingsMemory();
		this.pl = new PersonLocationMemory();
		this.pts = new PushToSpeechMemory();
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
	 * Close the connection
	 */
	public void close() {
		this.dataSource.close();
	}

	/**
	 * Tests if the database is still connected.
	 * 
	 * @return true if connected; otherwise false
	 */
	public boolean isConnected() {
		if (this.dataSource == null) {
			return true;
		}

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
