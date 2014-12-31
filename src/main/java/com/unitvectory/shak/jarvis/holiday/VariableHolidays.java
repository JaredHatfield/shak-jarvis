package com.unitvectory.shak.jarvis.holiday;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The holidays that occur on a different day each year.
 * 
 * @author Jared Hatfield
 * 
 */
class VariableHolidays {

	/**
	 * Populates the list of holidays.
	 * 
	 * @param year
	 *            the year
	 * @param list
	 *            the list
	 */
	public static void populate(int year, HolidayList list) {
		nth(list, 3, Calendar.MONDAY, Calendar.JANUARY, year, "MLK_DAY");
		nth(list, 3, Calendar.MONDAY, Calendar.FEBRUARY, year, "WASHINGTON");
		nth(list, 1, Calendar.SATURDAY, Calendar.MAY, year, "COMIC_BOOK_DAY");
		nth(list, 2, Calendar.SUNDAY, Calendar.MAY, year, "MOTHERS_DAY");
		nth(list, -1, Calendar.MONDAY, Calendar.MAY, year, "MEMORIAL_DAY");
		nth(list, 3, Calendar.SUNDAY, Calendar.JUNE, year, "FATHERS_DAY");
		nth(list, 1, Calendar.MONDAY, Calendar.SEPTEMBER, year, "LABOR_DAY");
		nthDayOfYear(list, year, 256, "PROGRAMERS_DAY");
		nth(list, 2, Calendar.MONDAY, Calendar.OCTOBER, year, "COLUMBUS_DAY");
		list.add(year, Calendar.NOVEMBER,
				getNthOfMonth(1, Calendar.MONDAY, Calendar.NOVEMBER, year) + 1,
				"ELECTION_DAY");
		nth(list, 4, Calendar.THURSDAY, Calendar.NOVEMBER, year, "THANKSGIVING");
		list.add(
				year,
				Calendar.NOVEMBER,
				getNthOfMonth(4, Calendar.THURSDAY, Calendar.NOVEMBER, year) + 1,
				"BLACK_FRIDAY");
	}

	/**
	 * Populates a holiday list for the nth day specified.
	 * 
	 * @param list
	 *            the list
	 * @param n
	 *            the nth day
	 * @param day_of_week
	 *            the day of the week
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @param name
	 *            the holiday name
	 */
	private static void nth(HolidayList list, int n, int day_of_week,
			int month, int year, String name) {
		list.add(year, month, getNthOfMonth(n, day_of_week, month, year), name);
	}

	/**
	 * Gets the Nth day of the month
	 * 
	 * @param n
	 *            the nth day
	 * @param day_of_week
	 *            the day of the week
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @return the Nth day
	 */
	private static int getNthOfMonth(int n, int day_of_week, int month, int year) {
		Calendar when = Calendar.getInstance();
		when.set(GregorianCalendar.MONTH, month);
		when.set(GregorianCalendar.DAY_OF_WEEK, day_of_week);
		when.set(GregorianCalendar.DAY_OF_WEEK_IN_MONTH, n);
		when.set(Calendar.YEAR, year);
		return when.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Sets the Nth day of the year as a holiday
	 * 
	 * @param list
	 *            the list
	 * @param year
	 *            the year
	 * @param n
	 *            the Nth
	 * @param name
	 *            the name
	 */
	private static void nthDayOfYear(HolidayList list, int year, int n,
			String name) {
		Calendar when = Calendar.getInstance();
		when.set(GregorianCalendar.DAY_OF_YEAR, n);
		when.set(Calendar.YEAR, year);
		when.get(Calendar.DAY_OF_MONTH);
		int month = when.get(Calendar.MONTH);
		int day = when.get(Calendar.DAY_OF_MONTH);
		list.add(year, month, day, name);
	}
}
