package com.unitvectory.shak.jarvis.action;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.ocpsoft.prettytime.PrettyTime;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.PersonLocationRecent;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.holiday.HolidayList;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;

/**
 * The smart motion action
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartMotionAction extends SmartAction {

	private HolidayList holiday;

	/**
	 * Creates a new instance of the SmartMotionAction class.
	 * 
	 */
	public SmartMotionAction() {
		this.holiday = new HolidayList();
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

		// Get the previous motion information
		SmartEvent previousMotion = cache.getPreviousEvent(motion);
		if (previousMotion == null) {
			return notifications.getList();
		}

		SmartThingsDeviceDetails previousDetails = cache
				.getDeviceDetails(previousMotion);
		if (previousDetails == null) {
			return notifications.getList();
		}

		// Get the calendars
		Calendar currentCalendar = motion.getCalendar();
		Calendar previousCalendar = previousMotion.getCalendar();

		// Set the dates to the users time zone
		TimeZone homeTimeZone = cache.getHomeTimeZone(details.getHome());
		currentCalendar.setTimeZone(homeTimeZone);
		previousCalendar.setTimeZone(homeTimeZone);

		// Motion inside in the target area
		if (motion.getStatus() == 'A' && details.isTarget()
				&& cache.isSomeoneHome(details.getHome())) {

			// Good morning message
			if (TimeHelper.isMorning(currentCalendar)
					&& TimeHelper.isFirstMorning(currentCalendar,
							previousCalendar)) {
				StringBuilder sb = new StringBuilder();
				sb.append("Good morning ");

				// Look up the list of holidays
				List<String> holidayNames = this.holiday
						.getHolidays(currentCalendar);
				for (int i = 0; i < holidayNames.size(); i++) {
					sb.append("Today is ");
					if (i > 0) {
						sb.append("also ");
					}

					sb.append(holidayNames.get(i));
					sb.append("...");
				}

				notifications.speak(details.getHome(), sb.toString());
			}
			// Calendar morningTime = (Calendar) currentCalendar.clone();
			// morningTime.setTimeZone(homeTimeZone);
			// morningTime.get

			// Welcome home message
			StringBuilder sb = new StringBuilder();
			List<PersonLocationDetails> people = cache.getPeopleArrivingHome(
					details.getHome(), previousCalendar);
			for (PersonLocationDetails person : people) {
				sb.append("Welcome home ");
				sb.append(person.getFirstName());
				sb.append("... ");
			}

			if (people.size() > 0) {
				List<PersonLocationDetails> atWork = cache.getPeopleAt(
						details.getHome(), "work", 'P', currentCalendar);
				for (PersonLocationDetails person : atWork) {
					sb.append(person.getFirstName());
					sb.append(" is still at work... ");
				}

				List<PersonLocationDetails> leftWork = cache.getPeopleAt(
						details.getHome(), "work", 'N', currentCalendar);
				for (PersonLocationDetails person : leftWork) {
					PersonLocationRecent location = cache
							.getRecentLocation(person.getToken());
					PrettyTime pt = new PrettyTime(currentCalendar.getTime());
					sb.append(person.getFirstName());
					sb.append(" left work ");
					sb.append(pt.format(location.getOccurred()));
					sb.append("... ");
				}
			}

			if (sb.length() > 0) {
				notifications.speak(details.getHome(), sb.toString());
			}
		}

		return notifications.getList();
	}
}
