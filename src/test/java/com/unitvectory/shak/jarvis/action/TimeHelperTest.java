package com.unitvectory.shak.jarvis.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.Test;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
import com.unitvectory.shak.jarvis.model.RequestGenerator;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;

/**
 * Tests the TimeHelper class.
 * 
 * @author Jared Hatfield
 *
 */
public class TimeHelperTest {

	@Test
	public void isMorningTest() {
		assertFalse("null calendar.", TimeHelper.isMorning(null));
		assertTrue("6:00 AM EST",
				TimeHelper.isMorning(this.getCalendar("2014-08-28 10:00:00")));
		assertFalse("5:59 AM EST",
				TimeHelper.isMorning(this.getCalendar("2014-08-28 09:59:59")));
		assertTrue("10:00 AM EST",
				TimeHelper.isMorning(this.getCalendar("2014-08-28 14:00:00")));
		assertTrue("10:59 AM EST",
				TimeHelper.isMorning(this.getCalendar("2014-08-28 14:59:59")));
		assertFalse("11:00 AM EST",
				TimeHelper.isMorning(this.getCalendar("2014-08-28 15:00:00")));
	}

	@Test
	public void isFirstMorningTest() {
		assertFalse("null calendar.", TimeHelper.isFirstMorning(null, null));
		assertTrue(
				"9:00 AM to 10:00 AM",
				TimeHelper.isFirstMorning(
						this.getCalendar("2014-08-28 10:00:00"),
						this.getCalendar("2014-08-28 09:00:00")));
		assertFalse(
				"10:00 AM to 11:00 AM",
				TimeHelper.isFirstMorning(
						this.getCalendar("2014-08-28 11:00:00"),
						this.getCalendar("2014-08-28 10:00:00")));
	}

	private Calendar getCalendar(String utcDate) {
		// A complicated, but realistic way to get a calendar
		SmartMotion motion = this.getEvent(utcDate);
		Calendar calendar = motion.getCalendar();

		// Assuming everything is EST for testing
		calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		return calendar;
	}

	private SmartMotion getEvent(String utcDate) {
		// Some generic ids just as filler
		String deviceId = UUID.randomUUID().toString();
		String hubId = UUID.randomUUID().toString();
		String locationId = UUID.randomUUID().toString();

		// Build the date
		Date date = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			date = df.parse(utcDate);
		} catch (ParseException e) {
			// Ignore
		}

		// Generate the request object based on the inputs
		JsonPublishRequest request = RequestGenerator.buildMotionSmartEvent(
				date, deviceId, hubId, locationId, true);

		// We generated a SmartThings style request
		SmartThingsPublish smart = new SmartThingsPublish(request);

		// We generated a motion request that we can finally return
		try {
			return new SmartMotion(smart);
		} catch (SmartException e) {
			return null;
		}
	}
}
