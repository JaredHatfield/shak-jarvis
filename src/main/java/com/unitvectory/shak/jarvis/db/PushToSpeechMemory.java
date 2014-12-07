package com.unitvectory.shak.jarvis.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The push to speech in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class PushToSpeechMemory implements PushToSpeechDAO {

	private Map<Integer, List<String>> devices;

	public PushToSpeechMemory() {
		this.devices = new HashMap<Integer, List<String>>();
	}

	public List<String> getPushDeviceIds(int home) {
		synchronized (this) {
			List<String> list = this.devices.get(Integer.valueOf(home));
			if (list == null) {
				list = new ArrayList<String>();
			}

			return Collections.unmodifiableList(list);
		}
	}

	public void insert(int home, String device) {
		synchronized (this) {
			List<String> list = this.devices.get(Integer.valueOf(home));
			if (list == null) {
				list = new ArrayList<String>();
				this.devices.put(Integer.valueOf(home), list);
			}

			if (!list.contains(device)) {
				list.add(device);
			}
		}
	}

	public void insertHistory(String deviceId, String event, String text) {
		// Not used by the in memory implementation
	}
}
