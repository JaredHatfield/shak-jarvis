package com.unitvectory.shak.jarvis.action;

import java.util.Calendar;

/**
 * The time helper class
 * 
 * @author Jared Hatfield
 *
 */
public class TimeHelper {

	/**
	 * Tests if a time is during the morning.
	 * 
	 * @param time
	 *            the time
	 * @return true if during the morning; otherwise false
	 */
	public static boolean isMorning(Calendar time) {
		if (time == null) {
			return false;
		}

		int hour = time.get(Calendar.HOUR);
		return hour >= 6 && hour <= 10;
	}

	/**
	 * Tests if this will be the first good morning trigger for a message.
	 * 
	 * @param time
	 *            the current time
	 * @param previous
	 *            the previous time
	 * @return true if the first time; otherwise false
	 */
	public static boolean isFirstMorning(Calendar time, Calendar previous) {
		if (time == null || previous == null) {
			return false;
		}

		if (time.get(Calendar.YEAR) == previous.get(Calendar.YEAR)
				&& time.get(Calendar.DAY_OF_YEAR) == previous
						.get(Calendar.DAY_OF_YEAR)
				&& previous.get(Calendar.HOUR) >= 6) {
			return false;
		}

		return true;
	}
}
