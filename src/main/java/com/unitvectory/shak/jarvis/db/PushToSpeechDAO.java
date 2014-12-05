package com.unitvectory.shak.jarvis.db;

import java.util.List;

/**
 * The push to speech DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface PushToSpeechDAO {

	/**
	 * Gets the list of device ids.
	 * 
	 * @param home
	 *            the home id
	 * @return the device ids.
	 */
	List<String> getPushDeviceIds(int home);

	/**
	 * Insert an action into the push to speech history.
	 * 
	 * @param deviceId
	 *            the device id
	 * @param event
	 *            the event
	 * @param text
	 *            the text
	 */
	void insertHistory(String deviceId, String event, String text);
}
