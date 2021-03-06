package com.unitvectory.shak.jarvis.model.smartthings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The SmartContact
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartContact extends SmartEvent {

	/**
	 * the status
	 */
	private char status;

	/**
	 * Creates a new instance of the SmartContact class.
	 * 
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
	 * @param status
	 *            the status
	 * @throws SmartException
	 */
	public SmartContact(String hubId, String locationId, String deviceId,
			String eventId, String date, char status) throws SmartException {
		super("contact", hubId, locationId, deviceId, eventId, date);
		this.status = status;
	}

	/**
	 * Creates a new instance of the SmartContact class.
	 * 
	 * @param publish
	 *            the publish
	 * @throws SmartException
	 */
	public SmartContact(SmartThingsPublish publish) throws SmartException {
		super("contact", publish.getHubId(), publish.getLocationId(), publish
				.getDeviceId(), publish.getId(), publish.getDate());

		if (!this.getName().equals(publish.getName())) {
			throw new SmartException("Incorrect name");
		}

		try {
			this.status = publish.getValue().toUpperCase().charAt(0);
		} catch (Exception e) {
			throw new SmartException("Bad data.", e);
		}
	}

	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * The history query.
	 */
	private static final String HistoryQuery = "INSERT INTO smart_contact_event (device, eventid, status, occurred) VALUES (?,?,?,?)";

	@Override
	public String getHistoryQuery() {
		return HistoryQuery;
	}

	@Override
	public void setHistoryParams(PreparedStatement stmt, int device)
			throws SQLException {
		stmt.setInt(1, device);
		stmt.setString(2, this.getEventId());
		stmt.setString(3, this.getStatus() + "");
		stmt.setTimestamp(4, this.getTimestamp());
	}

	/**
	 * The recent query.
	 */
	private static final String RecentQuery = "INSERT INTO smart_contact_recent (device, eventid, status, occurred) "
			+ "VALUES (?,?,?,?) "
			+ "ON DUPLICATE KEY UPDATE eventid = VALUES(eventid), "
			+ "status = VALUES(status), occurred = VALUES(occurred)";

	@Override
	public String getRecentQuery() {
		return RecentQuery;
	}

	@Override
	public void setRecentParams(PreparedStatement stmt, int device)
			throws SQLException {
		stmt.setInt(1, device);
		stmt.setString(2, this.getEventId());
		stmt.setString(3, this.getStatus() + "");
		stmt.setTimestamp(4, this.getTimestamp());
	}

	/**
	 * the previous query
	 */
	private static final String PreviousQuery = "SELECT d.hubid, "
			+ "d.locationid, d.deviceid, e.eventid, e.occurred, e.status "
			+ "FROM smart_contact_event e "
			+ "JOIN smart_device d ON e.device = d.pid "
			+ "WHERE d.hubid = ? AND d.locationid = ? AND d.deviceid = ? "
			+ "ORDER BY e.occurred DESC LIMIT 1, 1";

	@Override
	public String getPreviousQuery() {
		return PreviousQuery;
	}

	@Override
	public SmartEvent getPreviousObject(ResultSet rs) throws SQLException,
			SmartException {
		String hubId = rs.getString("hubid");
		String locationId = rs.getString("locationid");
		String deviceId = rs.getString("deviceid");
		String eventId = rs.getString("eventid");
		String occurred = this.formatDate(rs.getTimestamp("occurred"));
		char status = rs.getString("status").charAt(0);
		return new SmartContact(hubId, locationId, deviceId, eventId, occurred,
				status);
	}
}
