package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location database
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationDatabase extends AbstractDatabase implements
		PersonLocationDAO {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(PersonLocationDatabase.class);

	/**
	 * Creates a new instance of the PersonLocationDatabase class.
	 * 
	 * @param ds
	 *            the basic data source
	 */
	public PersonLocationDatabase(BasicDataSource ds) {
		super(ds);
	}

	/**
	 * the person name query
	 */
	private static final String PersonNameQuery = "SELECT firstName, lastName, home, pushover "
			+ "FROM person WHERE token = ? LIMIT 1 ";

	public PersonLocationDetails getPerson(String token) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(PersonNameQuery);
			stmt.setString(1, token);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				int home = rs.getInt("home");
				String pushover = rs.getString("pushover");
				return new PersonLocationDetails(token, firstName, lastName,
						home, pushover);
			}

			return null;
		} catch (SQLException e) {
			log.error("Unable to get person id", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	public InsertResult insertLocation(PersonLocationPublish publish) {
		int person = this.getPersonId(publish.getToken());
		if (person == -1) {
			return InsertResult.Error;
		}

		int place = this.getPersonPlaceId(publish.getLocation());
		if (place == -1) {
			return InsertResult.Error;
		}

		InsertResult historyResult = this.insertLocationEvent(person, place,
				publish.getStatus());
		switch (historyResult) {
		case Duplicate:
			return InsertResult.Duplicate;
		case Error:
			return InsertResult.Error;
		case Success:
			return this
					.insertLocationRecent(person, place, publish.getStatus());
		default:
			return InsertResult.Error;
		}
	}

	/**
	 * the insert person location event query
	 */
	private static final String InsertPersonLocationEventQuery = "INSERT INTO person_location_event "
			+ "(person, place, status, occurred) " + "VALUES (?,?,?,NOW()) ";

	/**
	 * Inserts the location event
	 * 
	 * @param person
	 *            the person id
	 * @param place
	 *            the place id
	 * @param status
	 *            the status
	 * @return the result
	 */
	private InsertResult insertLocationEvent(int person, int place, char status) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertPersonLocationEventQuery);
			stmt.setInt(1, person);
			stmt.setInt(2, place);
			stmt.setString(3, status + "");
			stmt.execute();
			return InsertResult.Success;
		} catch (SQLException e) {
			log.error("Unable to insert location event", e);
			return InsertResult.Error;
		} finally {
			this.closeEverything(con, stmt, null);
		}
	}

	/**
	 * the insert person location recent query
	 */
	private static final String InsertPersonLocationRecentQuery = "INSERT INTO person_location_recent "
			+ "(person, place, status, occurred) "
			+ "VALUES (?,?,?,NOW()) "
			+ "ON DUPLICATE KEY UPDATE place = VALUES(place), "
			+ "status = VALUES(status), occurred = NOW()";

	/**
	 * Inserts the location recent
	 * 
	 * @param person
	 *            the person id
	 * @param place
	 *            the place id
	 * @param status
	 *            the status
	 * @return the result
	 */
	private InsertResult insertLocationRecent(int person, int place, char status) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertPersonLocationRecentQuery);
			stmt.setInt(1, person);
			stmt.setInt(2, place);
			stmt.setString(3, status + "");
			stmt.execute();
			return InsertResult.Success;
		} catch (SQLException e) {
			log.error("Unable to insert location event", e);
			return InsertResult.Error;
		} finally {
			this.closeEverything(con, stmt, null);
		}
	}

	/**
	 * the person id query
	 */
	private static final String PersonIdQuery = "SELECT id FROM person WHERE token = ? ";

	/**
	 * Gets the person id
	 * 
	 * @param token
	 *            the token
	 * @return the id; -1 if not found
	 */
	private int getPersonId(String token) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(PersonIdQuery);
			stmt.setString(1, token);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt("id");
			}

			return -1;
		} catch (SQLException e) {
			log.error("Unable to get person id", e);
			return -1;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	/**
	 * the person place id query
	 */
	private static final String PersonPlaceIdQuery = "SELECT id FROM person_place WHERE name = ? ";

	/**
	 * Gets the person place id
	 * 
	 * @param name
	 *            the place name
	 * @return the id; -1 on failure
	 */
	private int getPersonPlaceId(String name) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(PersonPlaceIdQuery);
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getInt("id");
			}

			return -1;
		} catch (SQLException e) {
			log.error("Unable to get person id", e);
			return -1;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}
}
