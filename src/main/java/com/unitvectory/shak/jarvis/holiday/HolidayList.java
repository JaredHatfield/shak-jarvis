package com.unitvectory.shak.jarvis.holiday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * List of the holidays.
 * 
 * @author Jared Hatfield
 * 
 */
public class HolidayList {

	/**
	 * the holidays
	 */
	private Map<Integer, Map<Integer, Map<Integer, List<String>>>> holidays;

	/**
	 * the messages
	 */
	private ResourceBundle messages;

	/**
	 * Initializes a new instance of the HolidayList class.
	 */
	public HolidayList() {
		// Get the messages
		this.messages = ResourceBundle.getBundle("Holidays",
				Locale.getDefault());

		// Populate the years
		this.holidays = new TreeMap<Integer, Map<Integer, Map<Integer, List<String>>>>();
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		this.populateYear(thisYear);
	}

	/**
	 * Gets the list of holidays for a specific day.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param day
	 *            the day
	 * @return the list of holidays
	 */
	public synchronized List<String> getHolidays(int year, int month, int day) {
		List<String> holidayNames = new ArrayList<String>();

		// Localize all of the events
		List<String> holidayEvents = this.getEvents(year, month, day);
		for (String holidayEvent : holidayEvents) {
			holidayNames.add(this.messages.getString(holidayEvent));
		}

		return holidayNames;
	}

	/**
	 * Gets the list of holidays for a specific day.
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param day
	 *            the day
	 * @return the list of holidays
	 */
	List<String> getEvents(int year, int month, int day) {
		List<String> events = new ArrayList<String>();

		// Make sure the year is populated
		if (!this.holidays.containsKey(year)) {
			this.populateYear(year);
		}

		// Make sure the month and day have content
		if (!this.holidays.get(year).containsKey(month)) {
			return events;
		} else if (!this.holidays.get(year).get(month).containsKey(day)) {
			return events;
		}

		events = this.holidays.get(year).get(month).get(day);
		return events;
	}

	/**
	 * Adds a holiday to the list
	 * 
	 * @param year
	 *            the year
	 * @param month
	 *            the month
	 * @param day
	 *            the day
	 * @param name
	 *            the name
	 */
	void add(int year, int month, int day, String name) {
		this.holidays.get(year).get(month + 1).get(day).add(name);
	}

	/**
	 * Populates a year with holidays.
	 * 
	 * @param yearNumber
	 *            the year
	 */
	private void populateYear(int yearNumber) {
		// Build the memory structure
		Map<Integer, Map<Integer, List<String>>> year = new TreeMap<Integer, Map<Integer, List<String>>>();
		this.holidays.put(yearNumber, year);

		for (int monthNumber = 1; monthNumber <= 12; monthNumber++) {
			Map<Integer, List<String>> month = new TreeMap<Integer, List<String>>();
			year.put(monthNumber, month);

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, yearNumber);
			calendar.set(Calendar.MONTH, monthNumber - 1);
			int numDays = calendar.getActualMaximum(Calendar.DATE);

			for (int dayNumber = 1; dayNumber <= numDays; dayNumber++) {
				List<String> day = new ArrayList<String>();
				month.put(dayNumber, day);
			}
		}

		// populate the fixed holidays
		FixedHolidays.populate(yearNumber, this);

		// populate the variable holidays
		VariableHolidays.populate(yearNumber, this);
	}
}
