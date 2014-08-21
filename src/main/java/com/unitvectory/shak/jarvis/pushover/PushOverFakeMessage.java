package com.unitvectory.shak.jarvis.pushover;

/**
 * PushOver fake message.
 * 
 * @author Jared Hatfield
 *
 */
public class PushOverFakeMessage {

	private String user;

	private String text;

	private PushOverPriority priority;

	/**
	 * @param user
	 * @param text
	 * @param priority
	 */
	public PushOverFakeMessage(String user, String text,
			PushOverPriority priority) {
		super();
		this.user = user;
		this.text = text;
		this.priority = priority;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the priority
	 */
	public PushOverPriority getPriority() {
		return priority;
	}
}
