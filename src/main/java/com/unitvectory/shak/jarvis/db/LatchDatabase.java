package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.unitvectory.shak.jarvis.db.model.LatchValue;

/**
 * The latch database
 * 
 * @author Jared Hatfield
 *
 */
public class LatchDatabase extends AbstractDatabase implements LatchDAO {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(SmartThingsDatabase.class);

	/**
	 * Creates a new instance of the LatchDatabase class.
	 * 
	 * @param ds
	 *            the data source
	 */
	public LatchDatabase(DataSource ds) {
		super(ds);
	}

	public static final String GetLatchQuery = "SELECT value "
			+ "FROM home_latch " + "WHERE home = ? and name = ? " + "LIMIT 1 ";

	public LatchValue getLatch(int home, String name) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(GetLatchQuery);
			stmt.setInt(1, home);
			stmt.setString(2, name);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getBoolean("value")) {
					return LatchValue.TRUE;
				} else {
					return LatchValue.FALSE;
				}
			}

			return LatchValue.UNKNOWN;
		} catch (SQLException e) {
			log.error("Unable to get device details", e);
			return LatchValue.ERROR;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	private static final String SetLatchQuery = "INSERT INTO home_latch "
			+ "(home, name, value, updated) " + "VALUES (?, ?, ?, NOW()) "
			+ "ON DUPLICATE KEY UPDATE "
			+ "value = VALUES(value), updated = NOW()";

	public void setLatch(int home, String name, boolean value) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(SetLatchQuery);
			stmt.setInt(1, home);
			stmt.setString(2, name);
			stmt.setBoolean(3, value);
			stmt.execute();
		} catch (MySQLIntegrityConstraintViolationException e) {
			log.error("Unable to update latch", e);
		} catch (SQLException e) {
			log.error("Unable to update latch", e);
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

}
