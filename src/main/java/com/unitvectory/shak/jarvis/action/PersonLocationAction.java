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

		// The previous value of the latch
		LatchValue latchValue = latch.getLatch(
				person.getHome(),
				this.getLatchName(person.getToken(), event.getLocation(),
						event.getStatus()));

		// Latch is true so no notification needed
		if (LatchValue.TRUE.equals(latchValue)) {
			return notifications.getList();
		}

		// Notify of the location change
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

		// Update all of the location latches
		latch.setLatch(person.getHome(), this.getLatchName(person.getToken(),
				"home", 'P'),
				event.getLocation().equals("home") && event.getStatus() == 'P');
		latch.setLatch(person.getHome(), this.getLatchName(person.getToken(),
				"home", 'N'),
				event.getLocation().equals("home") && event.getStatus() == 'N');
		latch.setLatch(person.getHome(), this.getLatchName(person.getToken(),
				"work", 'P'),
				event.getLocation().equals("work") && event.getStatus() == 'P');
		latch.setLatch(person.getHome(), this.getLatchName(person.getToken(),
				"work", 'N'),
				event.getLocation().equals("work") && event.getStatus() == 'N');

		// Update the welcome home latch
		latch.setLatch(person.getHome(), ":t:" + person.getToken()
				+ ":welcomehome:", false);

		// PushToSpeech Notification
		notifications.speak(person.getHome(), message);

		// PushOver Notifications
		notifications.notifyHome(person.getHome(), message, person.getToken());

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
