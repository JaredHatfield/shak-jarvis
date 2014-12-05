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
	 * @return the result
	 */
	public PushToSpeechResult speak(String deviceid, String text);
}
