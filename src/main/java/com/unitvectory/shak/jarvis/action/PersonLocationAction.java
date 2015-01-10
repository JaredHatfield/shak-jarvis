package com.unitvectory.shak.jarvis.action;

import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.LatchDAO;
import com.unitvectory.shak.jarvis.db.model.LatchValue;
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
		LatchDAO latch = cache.getLatchDatabase();

		PersonLocationDetails person = cache.getPerson(event.getToken());
		if (person == null) {
			return notifications.getList();
		}

		int home = person.getHome();
		String token = person.getToken();
		String location = event.getLocation();
		char status = event.getStatus();

		// The previous value of the latch
		LatchValue latchValue = latch.getLatch(home,
				this.getLatchName(token, location, status));

		// Latch is true so no notification needed
		if (LatchValue.TRUE.equals(latchValue)) {
			return notifications.getList();
		}

		// Notify of the location change
		StringBuilder sb = new StringBuilder();
		sb.append(person.getFirstName());
		if (status == 'P') {
			sb.append(" is arriving ");
			if (!location.equals("home")) {
				sb.append("at ");
			}
		} else if (status == 'N') {
			sb.append(" has left ");
		} else {
			sb.append(" has moved to ");
		}

		sb.append(location);
		sb.append("... ");
		String message = sb.toString();

		// Update all of the location latches
		latch.setLatch(person.getHome(), this.getLatchName(token, "home", 'P'),
				location.equals("home") && status == 'P');
		latch.setLatch(person.getHome(), this.getLatchName(token, "home", 'N'),
				location.equals("home") && status == 'N');
		latch.setLatch(person.getHome(), this.getLatchName(token, "work", 'P'),
				location.equals("work") && status == 'P');
		latch.setLatch(person.getHome(), this.getLatchName(token, "work", 'N'),
				location.equals("work") && status == 'N');

		// Update the welcome home latch
		latch.setLatch(person.getHome(), ":t:" + token + ":welcomehome:", false);

		// PushToSpeech Notification
		notifications.speak(person.getHome(), message);

		// PushOver Notifications
		notifications.notifyHome(person.getHome(), message, token);

		return notifications.getList();
	}

	private String getLatchName(String token, String location, char state) {
		StringBuilder sb = new StringBuilder();
		sb.append(":t:");
		sb.append(token);
		sb.append(":l:");
		sb.append(location);
		sb.append(":s:");
		sb.append(state);
		sb.append(":");
		return sb.toString();
	}
}
