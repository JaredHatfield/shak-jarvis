package com.unitvectory.shak.jarvis.model.smartthings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.exception.SmartException;

/**
 * The SmartEvent
 * 
 * @author Jared Hatfield
 * 
 */
public abstract class SmartEvent {

	/**
	 * The logger
	 */
	private static Logger log = Logger.getLogger(SmartEvent.class);

	/**
	 * the name
	 */
	private String name;

	/**
	 * the hubId
	 */
	private String hubId;

	/**
	 * the locationId
	 */
	private String locationId;

	/**
	 * the deviceId
	 * 
	 */
	private String deviceId;

	/**
	 * the eventId
	 */
	private String eventId;

	/**
	 * the date
	 */
	private String date;

	/**
	 * Creates a new instance of the SmartEvent class.
	 * 
	 * @param name
	 *            the name
	 * @param hubId
	 *            the hubId
	 * @param locationId
	 *            the locationId
	 * @param deviceId
	 *            the deviceId
	 * @param eventId
	 *            the eventId
	 * @param date
	 *            the date
	 * @throws SmartException
	 */
	public SmartEvent(String name, String hubId, String locationId,
			String deviceId, String eventId, String date) throws SmartException {
		this.name = name;
		this.hubId = hubId;
		this.locationId = locationId;
		this.deviceId = deviceId;
		this.eventId = eventId;
		this.date = date;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the hubId
	 */
	public String getHubId() {
		return hubId;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return this.date;
	}

	/**
	 * Gets the timestamp
	 * 
	 * @return the timestamp
	 */
	public Timestamp getTimestamp() {
		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			java.util.Date utilDate = df.parse(this.date);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			return new Timestamp(sqlDate.getTime());
		} catch (Exception e) {
			log.error("Unable to parse date " + this.date, e);
			return null;
		}
	}

	/**
	 * Gets the calendar
	 * 
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			java.util.Date utilDate = df.parse(this.date);
			calendar.setTime(utilDate);
			return calendar;
		} catch (Exception e) {
			log.error("Unable to parse date " + this.date, e);
			return null;
		}
	}

	/**
	 * Gets the cache id used to identify the record when cached in memory.
	 * 
	 * @return the cache id
	 */
	public String getCacheId() {
		if (this.deviceId == null || this.locationId == null
				|| this.hubId == null) {
			return null;
		}

		String id = "/" + this.deviceId + "/" + this.locationId + "/"
				+ this.hubId;
		id = DigestUtils.sha1Hex(id);
		return id;
	}

	/**
	 * Gets the history query.
	 * 
	 * @return the history query
	 */
	public abstract String getHistoryQuery();

	/**
	 * Sets the history prepared statement parameters
	 * 
	 * @param stmt
	 *            the prepared statement
	 * @param device
	 *            the device
	 * @throws SQLException
	 */
	public abstract void setHistoryParams(PreparedStatement stmt, int device)
			throws SQLException;

	/**
	 * Gets the recent query.
	 * 
	 * @return the recent query
	 */
	public abstract String getRecentQuery();

	/**
	 * Sets the recent prepared statement parameters
	 * 
	 * @param stmt
	 *            the prepared statement
	 * @param device
	 *            the device
	 * @throws SQLException
	 */
	public abstract void setRecentParams(PreparedStatement stmt, int device)
			throws SQLException;

	/**
	 * Gets the previous query.
	 * 
	 * @return the previous query
	 */
	public abstract String getPreviousQuery();

	/**
	 * Gets the previous event object.
	 * 
	 * @param rs
	 *            the result set
	 * @return the event object
	 * @throws SQLException
	 * @throws SmartException
	 */
	public abstract SmartEvent getPreviousObject(ResultSet rs)
			throws SQLException, SmartException;

	/**
	 * Formats a data retrieved from database in standard format.
	 * 
	 * @param date
	 *            the sql date
	 * @return the standard date string
	 */
	protected String formatDate(java.sql.Date date) {
		SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date utilDate = new java.util.Date(date.getTime());
		return df.format(utilDate);
	}
}
