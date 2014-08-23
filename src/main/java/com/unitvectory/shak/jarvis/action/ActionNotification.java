package com.unitvectory.shak.jarvis.action;

/**
 * The action notificationJ
 * 
 * @author Jared Hatfield
 * 
 */
public class ActionNotification {

	/**
	 * the event
	 */
	private String event;

	/**
	 * the notification
	 */
	private String notification;

	/**
	 * the speak flag
	 */
	private boolean speak;

	/**
	 * the home id
	 */
	private int home;

	/**
	 * the push flag
	 */
	private boolean push;

	/**
	 * the PushOver token
	 */
	private String pushOverToken;

	/**
	 * Creates a new instance of the ActionNotification class.
	 */
	private ActionNotification() {
	}

	/**
	 * Builds a push to speech notification
	 * 
	 * @param event
	 *            the event
	 * @param notification
	 *            the notification
	 * @param speak
	 *            the speak flag
	 * @param home
	 *            the home id
	 */
	public static ActionNotification buildPushToSpeech(String event,
			String notification, boolean speak, int home) {
		ActionNotification action = new ActionNotification();
		action.event = event;
		action.notification = notification;
		action.speak = speak;
		action.home = home;
		return action;
	}

	/**
	 * Builds a PushOver notification
	 * 
	 * @param event
	 *            the event
	 * @param notification
	 *            the notification
	 * @param push
	 *            the push flag
	 * @param pushOverToken
	 *            the PushOver token
	 */
	public static ActionNotification buildPushOver(String event,
			String notification, boolean push, String pushOverToken) {
		ActionNotification action = new ActionNotification();
		action.event = event;
		action.notification = notification;
		action.push = push;
		action.pushOverToken = pushOverToken;
		return action;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @return the notification
	 */
	public String getNotification() {
		return notification;
	}

	/**
	 * @return the speak
	 */
	public boolean isSpeak() {
		return speak;
	}

	/**
	 * @param speak
	 *            the speak to set
	 */
	public void setSpeak(boolean speak) {
		this.speak = speak;
	}

	/**
	 * @return the home
	 */
	public int getHome() {
		return home;
	}

	/**
	 * @return the push
	 */
	public boolean isPush() {
		return push;
	}

	/**
	 * @return the pushOverToken
	 */
	public String getPushOverToken() {
		return pushOverToken;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActionNotification [event=" + event + ", notification="
				+ notification + ", speak=" + speak + ", home=" + home + "]";
	}

}
