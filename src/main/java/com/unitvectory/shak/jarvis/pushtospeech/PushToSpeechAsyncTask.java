package com.unitvectory.shak.jarvis.pushtospeech;

import com.unitvectory.shak.jarvis.action.ActionNotification;
import com.unitvectory.shak.jarvis.db.PushToSpeechDAO;

/**
 * The push to speech async task.
 * 
 * @author Jared Hatfield
 *
 */
public class PushToSpeechAsyncTask implements Runnable {

	/**
	 * the push to speech
	 */
	private PushToSpeech pushToSpeech;

	/**
	 * the push to speech dao
	 */
	private PushToSpeechDAO dao;

	/**
	 * the device id
	 */
	private String deviceid;

	/**
	 * the notification
	 */
	private ActionNotification notification;

	/**
	 * Creates a new instance of the PushToSpeechAsyncTask class.
	 * 
	 * @param pushToSpeech
	 *            the push to speech
	 * @param dao
	 *            the push to speech dao
	 * @param deviceid
	 *            the device id
	 * @param notification
	 *            the notification
	 */
	public PushToSpeechAsyncTask(PushToSpeech pushToSpeech,
			PushToSpeechDAO dao, String deviceid,
			ActionNotification notification) {
		this.pushToSpeech = pushToSpeech;
		this.dao = dao;
		this.deviceid = deviceid;
		this.notification = notification;
	}

	public void run() {
		PushToSpeechResult result = this.pushToSpeech.speak(this.deviceid,
				this.notification.getNotification());
		String event = notification.getEvent();
		String text = result.getOutputText();
		this.dao.insertHistory(deviceid, event, text);
	}
}
