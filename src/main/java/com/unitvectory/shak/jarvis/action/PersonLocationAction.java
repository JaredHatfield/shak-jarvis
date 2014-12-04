package com.unitvectory.shak.jarvis.action;

import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location action
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationAction {

	/**
	 * Creates an instance of the PersonLocationAction class.
	 */
	public PersonLocationAction() {
	}

	/**
	 * Gets the list of actions for an event.
	 * 
	 * @param cache
	 *            the cache
	 * @param event
	 *            the event
	 * @return the list of actions
	 */
	public List<ActionNotification> getActions(DatabaseEventCache cache,
			PersonLocationPublish event) {
		NotificationBuilder notifications = new NotificationBuilder(cache,
				"LOCATION");

		PersonLocationDetails person = cache.getPerson(event.getToken());
		if (person == null) {
			return notifications.getList();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(person.getFirstName());
		if (event.getStatus() == 'P') {
			sb.append(" is arriving ");
			if (!event.getLocation().equals("home")) {
				sb.append("at ");
			}
		} else if (event.getStatus() == 'N') {
			sb.append(" has left ");
		} else {
			sb.append(" has moved to ");
		}

		sb.append(event.getLocation());
		sb.append("... ");
		String message = sb.toString();

		// PushToSpeech Notification
		notifications.speak(person.getHome(), message);

		// PushOver Notifications
		notifications.notifyHome(person.getHome(), message, person.getToken());

		return notifications.getList();
	}
}
