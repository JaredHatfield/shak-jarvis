package com.unitvectory.shak.jarvis.action;

import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;

/**
 * The smart motion action
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartMotionAction extends SmartAction {

	/**
	 * Creates a new instance of the SmartMotionAction class.
	 * 
	 */
	public SmartMotionAction() {
	}

	@Override
	public List<ActionNotification> getActions(DatabaseEventCache cache,
			SmartEvent event) {
		NotificationBuilder notifications = new NotificationBuilder(cache,
				"MOTION");

		if (!(event instanceof SmartMotion)) {
			return notifications.getList();
		}

		SmartMotion motion = (SmartMotion) event;

		SmartThingsDeviceDetails details = cache.getDeviceDetails(event);

		if (details == null) {
			return notifications.getList();
		}

		// Outside motion
		if (motion.getStatus() == 'A' && details.isOutside()) {
			String message = details.getName() + " motion... ";
			notifications.speak(details.getHome(), message);
		}

		// Motion inside when no one is home
		if (motion.getStatus() == 'A' && details.isInside()
				&& !cache.isSomeoneHome(details.getHome())) {
			notifications.speak(details.getHome(), "{VAILED_THREAT}");

			String message = "Unexpected motion in the " + details.getName()
					+ "... ";
			notifications.notifyHome(details.getHome(), message);
		}

		return notifications.getList();
	}
}
