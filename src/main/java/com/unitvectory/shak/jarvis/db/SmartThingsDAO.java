package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;

import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart things database DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface SmartThingsDAO {

	/**
	 * Inserts a smart event.
	 * 
	 * @param event
	 *            the event
	 * @return the result of the insert
	 */
	InsertResult insertSmartEvent(SmartEvent event);

	/**
	 * Gets the smart things device details.
	 * 
	 * @param event
	 *            the event
	 * @return the details
	 * @throws SQLException
	 */
	SmartThingsDeviceDetails getDeviceDetails(SmartEvent event)
			throws SQLException;

	/**
	 * Gets the previous smart event.
	 * 
	 * @param event
	 *            the current event
	 * @return the previous event
	 * @throws SQLException
	 */
	SmartEvent getPreviousEvent(SmartEvent event) throws SQLException;
}
