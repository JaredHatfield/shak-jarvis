package com.unitvectory.shak.jarvis.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.amazonaws.services.sqs.model.Message;
import com.unitvectory.shak.jarvis.util.ResourceHelper;

/**
 * Tests the JsonPublishRequest class
 * 
 * @author Jared Hatfield
 * 
 */
public class JsonPublishRequestTest {

	/**
	 * Test the parser being given a null value
	 */
	@Test
	public void testNullStringParse() {
		String myString = null;
		JsonPublishRequest request = new JsonPublishRequest(myString);
		assertNotNull(request);
		assertFalse(request.isValid());
		assertNotNull(request.getData());
	}

	/**
	 * Test the parser being given a null value
	 */
	@Test
	public void testNullMessageParse() {
		Message myMessage = null;
		JsonPublishRequest request = new JsonPublishRequest(myMessage);
		assertNotNull(request);
		assertFalse(request.isValid());
		assertNotNull(request.getData());
	}

	/**
	 * Test the parser when things go perfectly.
	 */
	@Test
	public void testValidParse() {
		// Load the test JSON
		String json = ResourceHelper.load("/messagebody.json");
		assertNotNull(json);
		assertTrue(json.length() > 0);

		// Create the object
		JsonPublishRequest request = new JsonPublishRequest(json);
		assertNotNull(request);
		assertTrue(request.isValid());
		assertNotNull(request.getData());

		String auth = request.getData().get("auth");
		assertEquals("foobar", auth);

		String date = request.getData().get("date");
		assertEquals("2013-12-30T16:03:08.224Z", date);

		String description = request.getData().get("description");
		assertEquals(
				"raw:08EF170A59FF, dni:08EF, battery:17, batteryDivisor:0A, rssi:59, lqi:FF",
				description);

		String descriptionText = request.getData().get("descriptionText");
		assertEquals("Sensor was -39 dBm", descriptionText);

		String deviceId = request.getData().get("deviceId");
		assertEquals("2fffffff-fffff-ffff-ffff-fffffffffff", deviceId);

		String hubId = request.getData().get("hubId");
		assertEquals("3fffffff-fffff-ffff-ffff-fffffffffff", hubId);

		String id = request.getData().get("id");
		assertEquals("1fffffff-fffff-ffff-ffff-fffffffffff", id);

		String locationId = request.getData().get("locationId");
		assertEquals("4fffffff-fffff-ffff-ffff-fffffffffff", locationId);

		String name = request.getData().get("name");
		assertEquals("rssi", name);

		String source = request.getData().get("source");
		assertEquals("DEVICE", source);

		String unit = request.getData().get("unit");
		assertEquals("dBm", unit);

		String value = request.getData().get("value");
		assertEquals("-39", value);
	}
}
