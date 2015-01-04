package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * The push to speech database
 * 
 * @author Jared Hatfield
 * 
 */
public class PushToSpeechDatabase extends AbstractDatabase implements
		PushToSpeechDAO {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(PushToSpeechDatabase.class);

	/**
	 * Creates a new instance of the PushToSpeechDatabase class.
	 * 
	 * @param ds
	 *            the data source
	 */
	public PushToSpeechDatabase(DataSource ds) {
		super(ds);
	}

	/**
	 * the device list query
	 */
	private static final String DeviceListQuery = "SELECT deviceid "
			+ "FROM home_pushtospeech WHERE home = ? AND active = 1 ";

	public List<String> getPushDeviceIds(int home) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> ids = new ArrayList<String>();
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(DeviceListQuery);
			stmt.setInt(1, home);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ids.add(rs.getString("deviceid"));
			}

			return ids;
		} catch (SQLException e) {
			log.error("Unable to get push device ids", e);
			return ids;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	private static final String InsertHistoryQuery = "INSERT INTO home_pushtospeech_history "
			+ "(pushtospeech, time, event, text) "
			+ "VALUES ((SELECT id FROM home_pushtospeech WHERE deviceid = ?), NOW(), ?, ?) ";

	public void insertHistory(String deviceId, String event, String text) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertHistoryQuery);
			stmt.setString(1, deviceId);
			stmt.setString(2, event);
			stmt.setString(3, text);
			stmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Unable to insert into home_pushtospeech_history", e);
		} finally {
			this.closeEverything(con, stmt, null);
		}
	}
}
