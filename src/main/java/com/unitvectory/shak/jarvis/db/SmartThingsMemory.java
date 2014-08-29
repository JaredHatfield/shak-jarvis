package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart things in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class SmartThingsMemory implements SmartThingsDAO {

	private Map<String, SmartThingsDeviceDetails> devices;

	private Map<String, SmartEvent> recentEvent;

	private Map<String, SmartEvent> previousEvent;

	public SmartThingsMemory() {
		this.devices = new HashMap<String, SmartThingsDeviceDetails>();
		this.recentEvent = new HashMap<String, SmartEvent>();
		this.previousEvent = new HashMap<String, SmartEvent>();
	}

	public InsertResult insertSmartEvent(SmartEvent event) {
		synchronized (this) {
			String id = this.getEventId(event);

			// Track the previous event
			SmartEvent previous = this.recentEvent.get(id);
			if (previous != null) {
				this.previousEvent.put(id, previous);
			}

			// Store the recent event
			this.recentEvent.put(id, event);
			return InsertResult.Success;
		}
	}

	public SmartThingsDeviceDetails getDeviceDetails(SmartEvent event)
			throws SQLException {
		synchronized (this) {
			return this.devices.get(this.getEventId(event));
		}
	}

	public SmartEvent getPreviousEvent(SmartEvent event) throws SQLException {
		synchronized (this) {
			return this.previousEvent.get(this.getEventId(event));
		}
	}

	public void insertDeviceDetails(String deviceid, String locationid,
			String hubid, int home, String name, String type, boolean inside,
			boolean outside, boolean target) {
		synchronized (this) {
			SmartThingsDeviceDetails details = new SmartThingsDeviceDetails(
					deviceid, locationid, hubid, home, name, type, inside,
					outside, target);
			String id = "/" + deviceid + "/" + locationid + "/" + hubid;
			id = DigestUtils.sha1Hex(id);
			this.devices.put(id, details);
		}
	}

	private String getEventId(SmartEvent event) {
		String id = "/" + event.getDeviceId() + "/" + event.getLocationId()
				+ "/" + event.getHubId();
		id = DigestUtils.sha1Hex(id);
		return id;
	}
}
