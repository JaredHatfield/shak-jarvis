package com.unitvectory.shak.jarvis.action;

import java.util.ArrayList;
import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;

/**
 * The notification builder
 * 
 * @author Jared Hatfield
 *
 */
public class NotificationBuilder {

	private List<ActionNotification> notifications;

	private DatabaseEventCache cache;

	private String type;

	public NotificationBuilder(DatabaseEventCache cache, String type) {
		this.notifications = new ArrayList<ActionNotification>();
		this.cache = cache;
		this.type = type;
	}

	public List<ActionNotification> getList() {
		return this.notifications;
	}

	public void speak(int home, String message) {
		this.notifications.add(ActionNotification.buildPushToSpeech(this.type,
				message, true, home));
	}

	public void notifyHome(int home, String message) {
		List<PersonLocationDetails> people = this.cache.getPeople(home);
		if (people == null) {
			return;
		}

		for (PersonLocationDetails person : people) {
			if (person.getPushOver() == null) {
				continue;
			}

			this.notifyPerson(person.getPushOver(), message);
		}
	}

	public void notifyHome(int home, String message, String exceptUser) {
		List<PersonLocationDetails> people = this.cache.getPeople(home);
		if (people == null) {
			return;
		}

		for (PersonLocationDetails person : people) {
			if (person.getPushOver() == null) {
				continue;
			}

			if (person.getToken().equals(exceptUser)) {
				continue;
			}

			this.notifyPerson(person.getPushOver(), message);
		}
	}

	public void notifyPerson(String pushOver, String message) {
		this.notifications.add(ActionNotification.buildPushOver(this.type,
				message, true, pushOver));
	}
}
