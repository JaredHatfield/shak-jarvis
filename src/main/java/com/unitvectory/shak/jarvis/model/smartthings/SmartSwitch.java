package com.unitvectory.shak.jarvis.model.smartthings;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The Smart Switch
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartSwitch extends SmartEvent {

	/**
	 * the value
	 */
	private boolean value;

	/**
	 * Creates a new instance of the SmartSwitch class.
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
	 * @param value
	 *            the value
	 * @throws SmartException
	 */
	public SmartSwitch(String hubId, String locationId, String deviceId,
			String eventId, String date, boolean value) throws SmartException {
		super("switch", hubId, locationId, deviceId, eventId, date);
		this.value = value;
	}

	/**
	 * Creates a new instance of the SmartSwitch class.
	 * 
	 * @param publish
	 *            the publish
	 * @throws SmartException
	 */
	public SmartSwitch(SmartThingsPublish publish) throws SmartException {
		super("switch", publish.getHubId(), publish.getLocationId(), publish
				.getDeviceId(), publish.getId(), publish.getDate());

		if (!this.getName().equals(publish.getName())) {
			throw new SmartException("Incorrect name");
		}

		try {
			if (publish.getValue() == null) {
				throw new SmartException("Bad data.");
			} else if (publish.getValue().equalsIgnoreCase("on")) {
				value = true;
			} else if (publish.getValue().equalsIgnoreCase("off")) {
				value = false;
			} else {
				throw new SmartException("Bad data.");
			}
		} catch (Exception e) {
			throw new SmartException("Bad data.", e);
		}
	}

	/**
	 * @return the value
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * The history query.
	 */
	private static final String HistoryQuery = "INSERT INTO smart_switch_event (device, eventid, value, occurred) VALUES (?,?,?,?)";

	@Override
	public String getHistoryQuery() {
		return HistoryQuery;
	}

	@Override
	public void setHistoryParams(PreparedStatement stmt, int device)
			throws SQLException {
		stmt.setInt(1, device);
		stmt.setString(2, this.getEventId());
		stmt.setBoolean(3, this.getValue());
		stmt.setTimestamp(4, this.getTimestamp());
	}

	/**
	 * The recent query.
	 */
	private static final String RecentQuery = "INSERT INTO smart_switch_recent (device, eventid, value, occurred) "
			+ "VALUES (?,?,?,?) "
			+ "ON DUPLICATE KEY UPDATE eventid = VALUES(eventid), "
			+ "value = VALUES(value), occurred = VALUES(occurred)";

	@Override
	public String getRecentQuery() {
		return RecentQuery;
	}

	@Override
	public void setRecentParams(PreparedStatement stmt, int device)
			throws SQLException {
		stmt.setInt(1, device);
		stmt.setString(2, this.getEventId());
		stmt.setBoolean(3, this.getValue());
		stmt.setTimestamp(4, this.getTimestamp());
	}

	/**
	 * the previous query
	 */
	private static final String PreviousQuery = "SELECT d.hubid, "
			+ "d.locationid, d.deviceid, e.eventid, e.occurred, e.value "
			+ "FROM smart_switch_event e "
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
		boolean value = rs.getBoolean("value");
		return new SmartSwitch(hubId, locationId, deviceId, eventId, occurred,
				value);
	}
}
