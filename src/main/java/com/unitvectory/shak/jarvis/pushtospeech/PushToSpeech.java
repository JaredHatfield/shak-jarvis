package com.unitvectory.shak.jarvis.pushtospeech;

/**
 * The Push to Speech interface.
 * 
 * @author Jared Hatfield
 *
 */
public interface PushToSpeech {

	/**
	 * Read text aloud.
	 * 
	 * @param deviceid
	 *            the device id
	 * @param text
	 *            the text
	 * @return true if successful; otherwise false
	 */
	public boolean speak(String deviceid, String text);
}
