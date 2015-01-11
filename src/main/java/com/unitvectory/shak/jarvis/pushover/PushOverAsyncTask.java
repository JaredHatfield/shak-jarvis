package com.unitvectory.shak.jarvis.pushover;

/**
 * The push over async task.
 * 
 * @author Jared Hatfield
 *
 */
public class PushOverAsyncTask implements Runnable {

	/**
	 * the push over
	 */
	private PushOver pushOver;

	/**
	 * the user
	 */
	private String user;

	/**
	 * the message
	 */
	private String message;

	/**
	 * the priority
	 */
	private PushOverPriority priority;

	/**
	 * Creates a new instance of the PushOverAsyncTask class.
	 * 
	 * @param pushOver
	 *            the push over
	 * @param user
	 *            the user
	 * @param message
	 *            the message
	 * @param priority
	 *            the priority
	 */
	public PushOverAsyncTask(PushOver pushOver, String user, String message,
			PushOverPriority priority) {
		this.pushOver = pushOver;
		this.user = user;
		this.message = message;
		this.priority = priority;
	}

	public void run() {
		this.pushOver.sendMessage(this.user, this.message, this.priority);
	}
}
