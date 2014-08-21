package com.unitvectory.shak.jarvis.pushover;

/**
 * The PushOver interface.
 * 
 * @author Jared Hatfield
 *
 */
public interface PushOver {

	/**
	 * Sends a message to pushover
	 * 
	 * @param user
	 *            the user
	 * @param message
	 *            the message
	 * @param priority
	 *            the priority
	 * @return true if successful; otherwise false
	 */
	boolean sendMessage(String user, String message, PushOverPriority priority);
}
