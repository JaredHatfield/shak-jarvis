package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
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
	 *            the basic data source
	 */
	public PushToSpeechDatabase(BasicDataSource ds) {
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
}
