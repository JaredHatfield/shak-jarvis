package com.unitvectory.shak.jarvis.holiday;

import java.util.Calendar;

/**
 * The holidays that occur on the same day every year.
 * 
 * @author Jared Hatfield
 * 
 */
class FixedHolidays {

    /**
     * Populates the list of holidays.
     * 
     * @param year
     *            the year
     * @param list
     *            the list
     */
    public static void populate(int year, HolidayList list) {
        list.add(year, Calendar.JANUARY, 1, "NEW_YEARS_DAY");
        list.add(year, Calendar.FEBRUARY, 2, "GROUNDHOG_DAY");
        list.add(year, Calendar.FEBRUARY, 12, "DARWIN_DAY");
        list.add(year, Calendar.FEBRUARY, 14, "VALENTINES_DAY");
        list.add(year, Calendar.MARCH, 8, "WOMENS_DAY");
        list.add(year, Calendar.MARCH, 14, "PI_DAY");
        list.add(year, Calendar.MARCH, 17, "SAINT_PATRICKS_DAY");
        list.add(year, Calendar.APRIL, 1, "APRIL_FOOLS_DAY");
        list.add(year, Calendar.MAY, 1, "MAY_DAY");
        list.add(year, Calendar.MAY, 4, "STAR_WARS_DAY");
        list.add(year, Calendar.MAY, 5, "CINCO_DE_MAYO");
        list.add(year, Calendar.JUNE, 14, "FLAG_DAY");
        list.add(year, Calendar.JUNE, 27, "HELEN_KELLER_DAY");
        list.add(year, Calendar.JULY, 4, "INDEPENDENCE_DAY");
        list.add(year, Calendar.JULY, 27, "SYS_ADMIN_DAY");
        list.add(year, Calendar.AUGUST, 26, "WOMENS_DAY");
        list.add(year, Calendar.SEPTEMBER, 11, "PATRIOT_DAY");
        list.add(year, Calendar.SEPTEMBER, 17, "CONSTITUTION_DAY");
        list.add(year, Calendar.SEPTEMBER, 19, "PIRATE_DAY");
        list.add(year, Calendar.SEPTEMBER, 22, "HOBBIT_DAY");
        list.add(year, Calendar.OCTOBER, 16, "ADA_LOVELACE_DAY");
        list.add(year, Calendar.OCTOBER, 23, "MOLE_DAY");
        list.add(year, Calendar.OCTOBER, 31, "HALLOWEEN");
        list.add(year, Calendar.NOVEMBER, 5, "BTTF_DAY");
        list.add(year, Calendar.NOVEMBER, 11, "VETERANS_DAY");
        list.add(year, Calendar.NOVEMBER, 30, "COMPUTER_SECURITY_DAY");
        list.add(year, Calendar.DECEMBER, 5, "NINJA_DAY");
        list.add(year, Calendar.DECEMBER, 6, "SAINT_NICHOLAS_DAY");
        list.add(year, Calendar.DECEMBER, 7, "PEARL_HARBOR_DAY");
        list.add(year, Calendar.DECEMBER, 24, "CHRISTMAS_EVE");
        list.add(year, Calendar.DECEMBER, 25, "CHRISTMAS");
        list.add(year, Calendar.DECEMBER, 31, "NEW_YEARS_EVE");
    }
}
