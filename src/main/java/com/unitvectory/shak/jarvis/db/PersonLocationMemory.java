package com.unitvectory.shak.jarvis.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class PersonLocationMemory implements PersonLocationDAO {

	private Map<String, PersonLocationDetails> people;

	private List<PersonLocationPublish> locations;

	private Map<String, PersonLocationRecent> recent;

	public PersonLocationMemory() {
		this.people = new TreeMap<String, PersonLocationDetails>();
		this.locations = new ArrayList<PersonLocationPublish>();
		this.recent = new HashMap<String, PersonLocationRecent>();
	}

	public PersonLocationRecent getRecentLocation(String token) {
		return this.recent.get(token);
	}

	public List<PersonLocationDetails> getPeople(int home) {
		List<PersonLocationDetails> list = new ArrayList<PersonLocationDetails>();
		for (PersonLocationDetails person : this.people.values()) {
			if (person.getHome() == home) {
				list.add(person);
			}
		}

		return list;
	}

	public PersonLocationDetails getPerson(String token) {
		synchronized (this) {
			return this.people.get(token);
		}
	}

	public InsertResult insertLocation(PersonLocationPublish publish) {
		synchronized (this) {
			this.locations.add(publish);
			this.recent.put(
					publish.getToken(),
					new PersonLocationRecent(publish.getToken(), publish
							.getLocation(), publish.getStatus(), publish
							.getDate()));
			return InsertResult.Success;
		}
	}

	public void insertPerson(PersonLocationDetails details) {
		synchronized (this) {
			this.people.put(details.getToken(), details);
		}
	}
}
