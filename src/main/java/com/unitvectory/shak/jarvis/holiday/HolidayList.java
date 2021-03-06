package com.unitvectory.shak.jarvis.holiday;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
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
	 * the list of holidays
	 */
	private List<String> available;

	/**
	 * the names
	 */
	private Map<String, String> names;

	/**
	 * Initializes a new instance of the HolidayList class.
	 */
	public HolidayList() {
		this.available = new ArrayList<String>();

		// Get the messages
		ResourceBundle resource = ResourceBundle.getBundle("Holidays",
				Locale.getDefault());

		// Store all of the messages in a map
		this.names = new HashMap<String, String>();
		Enumeration<String> keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			names.put(key, resource.getString(key));
		}

		// Populate the years
		this.holidays = new TreeMap<Integer, Map<Integer, Map<Integer, List<String>>>>();
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		this.populateYear(thisYear);
	}

	/**
	 * Gets the list of holidays for a specified day.
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the list of holidays
	 */
	public synchronized List<String> getHolidays(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return this.getHolidays(year, month, day);
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
			holidayNames.add(this.names.get(holidayEvent));
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
		if (!this.available.contains(name)) {
			this.available.add(name);
		}
	}

	/**
	 * Gets all of the holidays
	 * 
	 * @return the list of holidays
	 */
	List<String> getAllHolidays() {
		return Collections.unmodifiableList(this.available);
	}

	/**
	 * Gets all of the holiday names
	 * 
	 * @return the map of names
	 */
	Map<String, String> getAllHolidayNames() {
		return Collections.unmodifiableMap(this.names);
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
