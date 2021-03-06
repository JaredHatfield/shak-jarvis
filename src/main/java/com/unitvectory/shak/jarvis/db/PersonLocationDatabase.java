package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.db.model.WeatherDetails;
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
	 *            the data source
	 */
	public PersonLocationDatabase(DataSource ds) {
		super(ds);
	}

	private static final String RecentLocationQuery = "SELECT l.name location, "
			+ "r.status, r.occurred "
			+ "FROM person p "
			+ "JOIN person_location_recent r ON p.id = r.person "
			+ "JOIN person_place l ON r.place = l.id "
			+ "WHERE p.token = ? ORDER BY r.occurred DESC LIMIT 1 ";

	public PersonLocationRecent getRecentLocation(String token) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(RecentLocationQuery);
			stmt.setString(1, token);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String location = rs.getString("location");
				char status = rs.getString("status").charAt(0);
				Date occurred = rs.getTimestamp("occurred");
				return new PersonLocationRecent(token, location, status,
						occurred);
			}

			return null;
		} catch (SQLException e) {
			log.error("Unable to get person id", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	/**
	 * the person list query
	 */
	private static final String PersonListQuery = "SELECT token, firstName, lastName, pushover "
			+ "FROM person WHERE home = ? ORDER BY lastName, firstName, token ";

	public List<PersonLocationDetails> getPeople(int home) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(PersonListQuery);
			stmt.setInt(1, home);
			rs = stmt.executeQuery();
			List<PersonLocationDetails> list = new ArrayList<PersonLocationDetails>();
			while (rs.next()) {
				String token = rs.getString("token");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String pushover = rs.getString("pushover");
				PersonLocationDetails person = new PersonLocationDetails(token,
						firstName, lastName, home, pushover);
				list.add(person);
			}

			return list;
		} catch (SQLException e) {
			log.error("Unable to get person id", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
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
			if (rs.next()) {
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
				publish.getStatus(), publish.getTimestamp());
		switch (historyResult) {
		case Duplicate:
			return InsertResult.Duplicate;
		case Error:
			return InsertResult.Error;
		case Success:
			return this.insertLocationRecent(person, place,
					publish.getStatus(), publish.getTimestamp());
		default:
			return InsertResult.Error;
		}
	}

	/**
	 * the insert person location event query
	 */
	private static final String InsertPersonLocationEventQuery = "INSERT INTO person_location_event "
			+ "(person, place, status, occurred) " + "VALUES (?,?,?,?) ";

	/**
	 * Inserts the location event
	 * 
	 * @param person
	 *            the person id
	 * @param place
	 *            the place id
	 * @param status
	 *            the status
	 * @param timestamp
	 *            the timestamp
	 * @return the result
	 */
	private InsertResult insertLocationEvent(int person, int place,
			char status, Timestamp timestamp) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertPersonLocationEventQuery);
			stmt.setInt(1, person);
			stmt.setInt(2, place);
			stmt.setString(3, status + "");
			stmt.setTimestamp(4, timestamp);
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
			+ "VALUES (?,?,?,?) "
			+ "ON DUPLICATE KEY UPDATE place = VALUES(place), "
			+ "status = VALUES(status), occurred = VALUES(occurred) ";

	/**
	 * Inserts the location recent
	 * 
	 * @param person
	 *            the person id
	 * @param place
	 *            the place id
	 * @param status
	 *            the status
	 * @param timestamp
	 *            the timestamp
	 * @return the result
	 */
	private InsertResult insertLocationRecent(int person, int place,
			char status, Timestamp timestamp) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(InsertPersonLocationRecentQuery);
			stmt.setInt(1, person);
			stmt.setInt(2, place);
			stmt.setString(3, status + "");
			stmt.setTimestamp(4, timestamp);
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
			if (rs.next()) {
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
			if (rs.next()) {
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

	private static final String HomeTimezoneQuery = "SELECT l.timezone "
			+ "FROM home h " + "JOIN home_location l ON h.location = l.id "
			+ "WHERE h.id = ? " + "LIMIT 1 ";

	public TimeZone getTimezone(int home) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(HomeTimezoneQuery);
			stmt.setInt(1, home);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return TimeZone.getTimeZone(rs.getString("timezone"));
			}

			return TimeZone.getDefault();
		} catch (SQLException e) {
			log.error("Unable to get timezone", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}

	private static final String WeatherQuery = "SELECT w.summary, "
			+ "ROUND(w.temperatureMin) temperatureMin, ROUND(w.temperatureMax) temperatureMax "
			+ "FROM home_weather w "
			+ "JOIN home_location l ON w.location = l.id "
			+ "JOIN home h on l.id = h.location " + "WHERE h.id = ? "
			+ "AND DATE(CONVERT_TZ(w.time, 'UTC', l.timezone)) = ? "
			+ "LIMIT 1 ";

	public WeatherDetails getWeather(int home, Date time) {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = this.getConnection();
			stmt = con.prepareStatement(WeatherQuery);
			stmt.setInt(1, home);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			stmt.setString(2, df.format(time));
			rs = stmt.executeQuery();
			if (rs.next()) {
				String summary = rs.getString("summary");
				int temperatureMin = rs.getInt("temperatureMin");
				int temperatureMax = rs.getInt("temperatureMax");
				return new WeatherDetails(summary, temperatureMin,
						temperatureMax);
			}

			return null;
		} catch (SQLException e) {
			log.error("Unable to get weather", e);
			return null;
		} finally {
			this.closeEverything(con, stmt, rs);
		}
	}
}
