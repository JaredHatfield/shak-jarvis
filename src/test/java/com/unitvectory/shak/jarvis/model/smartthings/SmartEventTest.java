package com.unitvectory.shak.jarvis.model.smartthings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;
import com.unitvectory.shak.jarvis.util.ResourceHelper;

/**
 * Tests the SmartEvent classes.
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartEventTest {

	/**
	 * The delta to compare numbers
	 */
	private static final double DELTA = 0e-11;

	/**
	 * Tests the RSSI
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartRssiSuccessTest() throws SmartException {
		SmartThingsPublish smart = this.loadSmart("/messagebodyrssi.json");
		SmartRssi rssi = new SmartRssi(smart);
		assertEquals("rssi", rssi.getName());
		assertEquals(-39, rssi.getValue(), DELTA);
	}

	/**
	 * Tests the LQI
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartLqiSuccessTest() throws SmartException {
		SmartThingsPublish smart = this.loadSmart("/messagebodylqi.json");
		SmartLqi lqi = new SmartLqi(smart);
		assertEquals("lqi", lqi.getName());
		assertEquals(100, lqi.getValue(), DELTA);
	}

	/**
	 * Tests the Battery
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartBatterySuccessTest() throws SmartException {
		SmartThingsPublish smart = this.loadSmart("/messagebodybattery.json");
		SmartBattery battery = new SmartBattery(smart);
		assertEquals("battery", battery.getName());
		assertEquals(75, battery.getValue(), DELTA);
	}

	/**
	 * Tests the Illuminance
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartIlluminanceSuccessTest() throws SmartException {
		SmartThingsPublish smart = this
				.loadSmart("/messagebodyilluminance.json");
		SmartIlluminance illuminance = new SmartIlluminance(smart);
		assertEquals("illuminance", illuminance.getName());
		assertEquals(3, illuminance.getValue(), DELTA);
	}

	/**
	 * Tests the Temperature
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartTemperatureSuccessTest() throws SmartException {
		SmartThingsPublish smart = this
				.loadSmart("/messagebodytemperature.json");
		SmartTemperature temperature = new SmartTemperature(smart);
		assertEquals("temperature", temperature.getName());
		assertEquals(55, temperature.getValue(), DELTA);
		assertEquals('F', temperature.getUnit());
	}

	/**
	 * Tests the Motion
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartMotionSuccessTest() throws SmartException {
		SmartThingsPublish smart = this.loadSmart("/messagebodymotion.json");
		SmartMotion motion = new SmartMotion(smart);
		assertEquals("motion", motion.getName());
		assertEquals('A', motion.getStatus());
	}

	/**
	 * Tests the Switch
	 * 
	 * @throws SmartException
	 */
	@Test
	public void smartSwitchSuccessTest() throws SmartException {
		SmartThingsPublish smart = this.loadSmart("/messagebodyswitch.json");
		SmartSwitch mySwitch = new SmartSwitch(smart);
		assertEquals("switch", mySwitch.getName());
		assertFalse(mySwitch.getValue());
	}

	/**
	 * Loads the smart object
	 * 
	 * @param name
	 *            the file
	 * @return the smart object
	 */
	private SmartThingsPublish loadSmart(String name) {
		String json = ResourceHelper.load(name);
		assertNotNull(json);
		assertTrue(json.length() > 0);

		// Create the object
		JsonPublishRequest request = new JsonPublishRequest(json);
		assertNotNull(request);
		assertTrue(request.isValid());
		assertNotNull(request.getData());

		SmartThingsPublish smart = new SmartThingsPublish(request);
		assertNotNull(smart);

		return smart;
	}
}
