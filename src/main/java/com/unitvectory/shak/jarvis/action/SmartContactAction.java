package com.unitvectory.shak.jarvis.action;

import java.util.ArrayList;
import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartContact;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart contact action
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartContactAction extends SmartAction {

	/**
	 * Creates a new instance of the SmartContactAction class.
	 * 
	 */
	public SmartContactAction() {
	}

	@Override
	public List<ActionNotification> getActions(DatabaseEventCache cache,
			SmartEvent event) {
		List<ActionNotification> notifications = new ArrayList<ActionNotification>();

		if (!(event instanceof SmartContact)) {
			return notifications;
		}

		SmartContact contact = (SmartContact) event;

		SmartThingsDeviceDetails details = cache.getDeviceDetails(event);

		if (details == null) {
			return notifications;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(details.getName());
		sb.append(" is ");
		if (contact.getStatus() == 'O') {
			sb.append("open... ");
		} else if (contact.getStatus() == 'C') {
			sb.append("closed... ");
		} else {
			sb.append("unknown... ");
		}

		String message = sb.toString();

		// PushToSpeech Notification
		notifications.add(ActionNotification.buildPushToSpeech("CONTACT",
				message, true, details.getHome()));

		// PushOver Notifications
		List<PersonLocationDetails> homePeople = cache.getPeople(details
				.getHome());
		for (PersonLocationDetails homePerson : homePeople) {
			if (homePerson.getPushOver() == null) {
				continue;
			}

			PersonLocationRecent location = cache.getRecentLocation(homePerson
					.getToken());
			if (location != null && location.getLocation().equals("home")
					&& location.getStatus() == 'P') {
				continue;
			}

			notifications.add(ActionNotification.buildPushOver("CONTACT",
					message, true, homePerson.getPushOver()));
		}

		return notifications;
	}
}
