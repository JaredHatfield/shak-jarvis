package com.unitvectory.shak.jarvis.holiday;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the holiday list.
 * 
 * @author Jared Hatfield
 * 
 */
public class HolidayListTest {

	private static HolidayList holiday;

	@BeforeClass
	public static void oneTimeSetUp() {
		holiday = new HolidayList();
	}

	@Test
	public void newYearTest() {
		this.isHoliday(2013, 1, 1, "NEW_YEARS_DAY");
		this.isHoliday(2014, 1, 1, "NEW_YEARS_DAY");
	}

	@Test
	public void valentinesDayTest() {
		this.isHoliday(2013, 2, 14, "VALENTINES_DAY");
		this.isHoliday(2014, 2, 14, "VALENTINES_DAY");
	}

	@Test
	public void memorialDayTest() {
		this.isHoliday(2011, 5, 30, "MEMORIAL_DAY");
		this.isHoliday(2012, 5, 28, "MEMORIAL_DAY");
		this.isHoliday(2013, 5, 27, "MEMORIAL_DAY");
		this.isHoliday(2014, 5, 26, "MEMORIAL_DAY");
		this.isHoliday(2015, 5, 25, "MEMORIAL_DAY");
		this.isHoliday(2016, 5, 30, "MEMORIAL_DAY");
	}

	@Test
	public void programmersDayTest() {
		this.isHoliday(2012, 9, 12, "PROGRAMERS_DAY");
		this.isHoliday(2014, 9, 13, "PROGRAMERS_DAY");
	}

	@Test
	public void mlkTest() {
		this.isHoliday(2013, 1, 21, "MLK_DAY");
		this.isHoliday(2014, 1, 20, "MLK_DAY");
		this.isHoliday(2015, 1, 19, "MLK_DAY");
	}

	private void isHoliday(int year, int month, int day, String name) {
		List<String> list = holiday.getEvents(year, month, day);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(name, list.get(0));
		list = holiday.getHolidays(year, month, day);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertTrue(list.get(0).length() > 0);
	}
}
