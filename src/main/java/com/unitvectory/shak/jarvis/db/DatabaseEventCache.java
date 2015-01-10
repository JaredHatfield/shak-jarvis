package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.db.model.LatchValue;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.db.model.WeatherDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The database event cache
 * 
 * @author Jared Hatfield
 * 
 */
public class DatabaseEventCache {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(DatabaseEventCache.class);

	/**
	 * the database
	 */
	private ShakDatabase database;

	/**
	 * the location token person
	 */
	private Map<String, PersonLocationDetails> locationTokenPerson;

	/**
	 * the device details
	 */
	private Map<String, SmartThingsDeviceDetails> deviceDetails;

	/**
	 * the home people
	 */
	private Map<Integer, List<PersonLocationDetails>> homePeople;

	/**
	 * the recent location
	 */
	private Map<String, PersonLocationRecent> recentLocation;

	/**
	 * the previous events
	 */
	private Map<String, SmartEvent> previousEvent;

	/**
	 * the home timezones
	 */
	private Map<Integer, TimeZone> homeTimezones;

	/**
	 * Creates a new instance of the DatabaseEventCache class.
	 * 
	 * @param database
	 *            the database
	 */
	public DatabaseEventCache(ShakDatabase database) {
		this.database = database;
		this.locationTokenPerson = new HashMap<String, PersonLocationDetails>();
		this.deviceDetails = new HashMap<String, SmartThingsDeviceDetails>();
		this.homePeople = new HashMap<Integer, List<PersonLocationDetails>>();
		this.recentLocation = new HashMap<String, PersonLocationRecent>();
		this.previousEvent = new HashMap<String, SmartEvent>();
		this.homeTimezones = new HashMap<Integer, TimeZone>();
	}

	/**
	 * Gets the person name.
	 * 
	 * @param token
	 *            the token
	 * @return the person
	 */
	public PersonLocationDetails getPerson(String token) {
		PersonLocationDetails name = this.locationTokenPerson.get(token);
		if (name != null) {
			return name;
		}

		name = this.database.pl().getPerson(token);
		this.locationTokenPerson.put(token, name);
		return name;
	}

	/**
	 * Gets the device details
	 * 
	 * @param event
	 *            the event
	 * @return the device details
	 */
	public SmartThingsDeviceDetails getDeviceDetails(SmartEvent event) {
		String key = event.getDeviceId() + event.getLocationId()
				+ event.getHubId();
		if (this.deviceDetails.containsKey(key)) {
			return this.deviceDetails.get(key);
		}

		SmartThingsDeviceDetails details = null;
		try {
			details = this.database.st().getDeviceDetails(event);
		} catch (SQLException e) {
			log.warn("sql failed to get device details", e);
		}

		this.deviceDetails.put(key, details);
		return details;
	}

	public boolean isSomeoneHome(int home) {
		List<PersonLocationDetails> people = this.getPeople(home);
		if (people == null) {
			return false;
		}

		for (PersonLocationDetails person : people) {
			PersonLocationRecent location = this.getRecentLocation(person
					.getToken());
			if (location == null) {
				continue;
			}

			if (location.getLocation().equalsIgnoreCase("home")
					&& location.getStatus() == 'P') {
				return true;
			}
		}

		return false;
	}

	public List<PersonLocationDetails> getPeople(int home) {
		Integer key = Integer.valueOf(home);
		if (this.homePeople.containsKey(key)) {
			return this.homePeople.get(key);
		}

		List<PersonLocationDetails> list = null;
		list = this.database.pl().getPeople(home);
		if (list != null) {
			this.homePeople.put(key, list);
		}

		return list;
	}

	public PersonLocationRecent getRecentLocation(String token) {
		if (this.recentLocation.containsKey(token)) {
			return this.recentLocation.get(token);
		}

		PersonLocationRecent recent = this.database.pl().getRecentLocation(
				token);
		if (recent != null) {
			this.recentLocation.put(token, recent);
		}

		return recent;
	}

	public SmartEvent getPreviousEvent(SmartEvent event) {
		if (event == null) {
			return null;
		}

		String id = event.getCacheId();
		if (this.previousEvent.containsKey(id)) {
			return this.previousEvent.get(id);
		}

		SmartEvent previous = null;
		try {
			previous = this.database.st().getPreviousEvent(event);
		} catch (SQLException e) {
			log.warn("sql failed to get previous event", e);
		}

		if (previous != null) {
			this.previousEvent.put(id, previous);
		}

		return previous;
	}

	public List<PersonLocationDetails> getPeopleArrivingHome(int home,
			Calendar compare) {
		List<PersonLocationDetails> people = this.getPeople(home);
		List<PersonLocationDetails> arriving = new ArrayList<PersonLocationDetails>();
		if (people == null) {
			return arriving;
		}

		for (PersonLocationDetails person : people) {
			PersonLocationRecent location = this.getRecentLocation(person
					.getToken());
			if (location == null) {
				continue;
			}

			String welcome = ":t:" + person.getToken() + ":welcomehome:";
			LatchValue welcomeLatch = this.database.l().getLatch(home, welcome);

			Date locDate = location.getOccurred();
			Date comDate = compare.getTime();
			if (location.getLocation().equalsIgnoreCase("home")
					&& location.getStatus() == 'P' && locDate.after(comDate)
					&& LatchValue.FALSE.equals(welcomeLatch)) {
				arriving.add(person);
				this.database.l().setLatch(home, welcome, true);
			}
		}

		return arriving;
	}

	public List<PersonLocationDetails> getPeopleAt(int home, String loc,
			char status, Calendar compare) {
		List<PersonLocationDetails> people = this.getPeople(home);
		List<PersonLocationDetails> atWork = new ArrayList<PersonLocationDetails>();
		if (people == null) {
			return atWork;
		}

		for (PersonLocationDetails person : people) {
			PersonLocationRecent location = this.getRecentLocation(person
					.getToken());
			if (location == null) {
				continue;
			}

			Calendar calendar = Calendar.getInstance(TimeZone
					.getTimeZone("UTC"));
			calendar.setTime(location.getOccurred());
			if (location.getLocation().equalsIgnoreCase(loc)
					&& location.getStatus() == status
					&& calendar.get(Calendar.YEAR) == compare
							.get(Calendar.YEAR)
					&& calendar.get(Calendar.DAY_OF_YEAR) == compare
							.get(Calendar.DAY_OF_YEAR)) {
				atWork.add(person);
			}
		}

		return atWork;
	}

	public TimeZone getHomeTimeZone(int home) {
		Integer key = Integer.valueOf(home);
		if (this.homeTimezones.containsKey(key)) {
			return this.homeTimezones.get(key);
		}

		TimeZone timezone = this.database.pl().getTimezone(home);
		this.homeTimezones.put(key, timezone);
		return timezone;
	}

	public WeatherDetails getWeather(int home, Date time) {
		// No caching required for infrequent request type
		return this.database.pl().getWeather(home, time);
	}

	public LatchDAO getLatchDatabase() {
		return this.database.l();
	}

	/**
	 * Clears the cache
	 */
	public void clear() {
		this.locationTokenPerson.clear();
		this.deviceDetails.clear();
		this.homePeople.clear();
		this.recentLocation.clear();
		this.previousEvent.clear();
		this.homeTimezones.clear();
	}
}
