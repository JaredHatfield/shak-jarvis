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
	 * Creates a new instance of the ActionNotification class.
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
	public ActionNotification(String event, String notification, boolean speak,
			int home) {
		this.event = event;
		this.notification = notification;
		this.speak = speak;
		this.home = home;
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
