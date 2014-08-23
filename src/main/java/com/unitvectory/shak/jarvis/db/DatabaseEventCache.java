package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The database event cache
 * 
 * @author Jared Hatfield
 * 
 */
public class DatabaseEventCache {

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
		}

		this.deviceDetails.put(key, details);
		return details;
	}

	public List<PersonLocationDetails> getPeople(int home) {
		Integer key = new Integer(home);
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

	/**
	 * Clears the cache
	 */
	public void clear() {
		this.locationTokenPerson.clear();
		this.deviceDetails.clear();
	}
}
