package com.unitvectory.shak.jarvis.pushtospeech;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fake client for the Push to Speech API.
 * 
 * @author Jared Hatfield
 *
 */
public class PushToSpeechFake implements PushToSpeech {

	private Map<String, List<String>> text;

	public PushToSpeechFake() {
		this.text = new HashMap<String, List<String>>();
	}

	public boolean speak(String deviceid, String text) {
		synchronized (this) {
			List<String> history = this.text.get(deviceid);
			if (history == null) {
				history = new ArrayList<String>();
				this.text.put(deviceid, history);
			}

			history.add(text);
			return true;
		}
	}

	public List<String> getHistory(String deviceid) {
		return this.text.get(deviceid);
	}
}
