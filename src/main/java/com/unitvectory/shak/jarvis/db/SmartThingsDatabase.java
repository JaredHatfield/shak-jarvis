package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart things database
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThingsDatabase extends AbstractDatabase implements
		SmartThingsDAO {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(SmartThingsDatabase.class);

	/**
	 * Creates a new instance of the SmartThingsDatabase
	 * 
	 * @param ds
	 *            the data source
	 */
	public SmartThingsDatabase(DataSource ds) {
		super(ds);
	}

	/**
	 * the device details query
	 */
	private static final String DeviceDetailsQuery = "SELECT d.pid, h.home, l.name, t.type, l.isInside, l.isOutside, l.isTarget "
			+ "FROM smart_device d "
			+ "JOIN smart_device_details l ON d.pid = l.pid "
			+ "JOIN smart_device_type t ON l.type = t.pid "
			+ "JOIN smart_hub h ON d.hubid = h.hubid "
			+ "WHERE d.deviceid = ? AND d.locationid = ? AND d.hubid = ? "
			+ "LIMIT 1 ";

	public SmartThingsDeviceDetails getDeviceDetails(SmartEvent event)
			throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(DeviceDetailsQuery);
			stmt.setString(1, event.getDeviceId());
			stmt.setString(2, event.getLocationId());
			stmt.setString(3, event.getHubId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				SmartThingsDeviceDetails details = new SmartThingsDeviceDetails();
				details.setHome(rs.getInt("home"));
				details.setName(rs.getString("name"));
				details.setType(rs.getString("type"));
				details.setInside(rs.getBoolean("isInside"));
				details.setOutside(rs.getBoolean("isOutside"));
				details.setTarget(rs.getBoolean("isTarget"));
				return details;
			}

			return null;
		} catch (SQLException e) {
			log.error("Unable to get device details", e);
			throw e;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	public InsertResult insertSmartEvent(SmartEvent event) {
		int device = this.getSmartEventDevicePid(event.getHubId(),
				event.getLocationId(), event.getDeviceId());
		if (device == -1) {
			return InsertResult.Error;
		}

		InsertResult historyResult = this
				.insertSmartEventHistory(event, device);
		switch (historyResult) {
		case Duplicate:
			return InsertResult.Duplicate;
		case Error:
			return InsertResult.Error;
		case Success:
			return this.insertSmartEventRecent(event, device);
		default:
			return InsertResult.Error;
		}
	}

	/**
	 * Inserts a smart event into the recent table.
	 * 
	 * @param event
	 *            the event
	 * @param device
	 *            the device id
	 * @return the insert result
	 */
	private InsertResult insertSmartEventRecent(SmartEvent event, int device) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			String query = event.getRecentQuery();
			if (query == null) {
				return InsertResult.Success;
			}

			stmt = con.prepareStatement(query);
			event.setRecentParams(stmt, device);
			stmt.execute();
			return InsertResult.Success;
		} catch (SQLException e) {
			log.error("Unable to insert SmartEventRecent", e);
			return InsertResult.Error;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	/**
	 * Inserts a smart event into the history table
	 * 
	 * @param event
	 *            the event
	 * @param device
	 *            the device id
	 * @return the insert result
	 */
	private InsertResult insertSmartEventHistory(SmartEvent event, int device) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			String query = event.getHistoryQuery();
			if (query == null) {
				return InsertResult.Success;
			}

			stmt = con.prepareStatement(query);
			event.setHistoryParams(stmt, device);
			stmt.execute();
			return InsertResult.Success;
		} catch (MySQLIntegrityConstraintViolationException e) {
			return InsertResult.Duplicate;
		} catch (SQLException e) {
			log.error("Unable to insert SmartEventHistory", e);
			return InsertResult.Error;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	/**
	 * The smart event device pid query
	 */
	private final static String GetSmartEventDevicePidQuery = "SELECT pid FROM smart_device WHERE hubid = ? AND locationid = ? AND deviceid = ? LIMIT 1";

	/**
	 * Gets the smart event device pid
	 * 
	 * @param hubId
	 *            the hub id
	 * @param locationId
	 *            the location id
	 * @param deviceId
	 *            the device id
	 * @return the device pid
	 */
	private int getSmartEventDevicePid(String hubId, String locationId,
			String deviceId) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(GetSmartEventDevicePidQuery);
			stmt.setString(1, hubId);
			stmt.setString(2, locationId);
			stmt.setString(3, deviceId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				int pid = rs.getInt("pid");
				return pid;
			}
		} catch (SQLException e) {
			log.error("Unable to get SmartEventDevicePid", e);
			return -1;
		} finally {
			this.closeEverything(con, stmt, rs);
		}

		return this.insertSmartEventDevice(hubId, locationId, deviceId);
	}

	/**
	 * the insert smart event device query
	 */
	private final static String InsertSmartEventDeviceQuery = "INSERT INTO smart_device (hubid, locationid, deviceid, added) VALUES (?,?,?,NOW())";

	/**
	 * Inserts a smart event device.
	 * 
	 * @param hubId
	 *            the hub id
	 * @param locationId
	 *            the location id
	 * @param deviceId
	 *            the device id
	 * @return the device id
	 */
	private int insertSmartEventDevice(String hubId, String locationId,
			String deviceId) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertSmartEventDeviceQuery,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, hubId);
			stmt.setString(2, locationId);
			stmt.setString(3, deviceId);
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int pid = rs.getInt(1);
				return pid;
			}

			return -1;
		} catch (SQLException e) {
			log.error("Unable to insert SmartEventDevice", e);
			return -1;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	public SmartEvent getPreviousEvent(SmartEvent event) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(event.getPreviousQuery());
			stmt.setString(1, event.getHubId());
			stmt.setString(2, event.getLocationId());
			stmt.setString(3, event.getDeviceId());
			rs = stmt.executeQuery();
			if (rs.next()) {
				return event.getPreviousObject(rs);
			}

			return null;
		} catch (SQLException e) {
			log.error("Unable to get previous event", e);
			return null;
		} catch (SmartException e) {
			log.error("Unable to get previous event", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

}
