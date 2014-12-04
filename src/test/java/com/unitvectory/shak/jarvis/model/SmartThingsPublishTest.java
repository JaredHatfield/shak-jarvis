package com.unitvectory.shak.jarvis.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.unitvectory.shak.jarvis.util.ResourceHelper;

/**
 * Tests the SmartThingsPublish class
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThingsPublishTest {

	/**
	 * Test the parser being given a null value
	 */
	@Test
	public void testNullStringParse() {
		JsonPublishRequest myRequest = null;
		SmartThingsPublish smart = new SmartThingsPublish(myRequest);
		assertNotNull(smart);
		assertNull(smart.getPublish());
		assertNull(smart.getAuth());
		assertNull(smart.getDate());
		assertNull(smart.getDescription());
		assertNull(smart.getDescriptionText());
		assertNull(smart.getDeviceId());
		assertNull(smart.getHubId());
		assertNull(smart.getId());
		assertNull(smart.getLocationId());
		assertNull(smart.getName());
		assertNull(smart.getSource());
		assertNull(smart.getUnit());
		assertNull(smart.getValue());
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

		// Create the SmartThingsPublish
		SmartThingsPublish smart = new SmartThingsPublish(request);
		assertNotNull(smart);

		assertEquals("foobar", smart.getAuth());
		assertEquals("2013-12-30T16:03:08.224Z", smart.getDate());
		assertEquals(
				"raw:08EF170A59FF, dni:08EF, battery:17, batteryDivisor:0A, rssi:59, lqi:FF",
				smart.getDescription());
		assertEquals("Sensor was -39 dBm", smart.getDescriptionText());
		assertEquals("2fffffff-fffff-ffff-ffff-fffffffffff",
				smart.getDeviceId());
		assertEquals("3fffffff-fffff-ffff-ffff-fffffffffff", smart.getHubId());
		assertEquals("1fffffff-fffff-ffff-ffff-fffffffffff", smart.getId());
		assertEquals("4fffffff-fffff-ffff-ffff-fffffffffff",
				smart.getLocationId());
		assertEquals("rssi", smart.getName());
		assertEquals("DEVICE", smart.getSource());
		assertEquals("dBm", smart.getUnit());
		assertEquals("-39", smart.getValue());
	}
}
