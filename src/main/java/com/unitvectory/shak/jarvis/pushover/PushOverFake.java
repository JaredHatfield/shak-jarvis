package com.unitvectory.shak.jarvis.pushover;

import java.util.ArrayList;
import java.util.List;

/**
 * Fake PushOver client used for testing.
 * 
 * @author Jared Hatfield
 *
 */
public class PushOverFake implements PushOver {

	private List<PushOverFakeMessage> messages;

	public PushOverFake() {
		this.messages = new ArrayList<PushOverFakeMessage>();
	}

	public boolean sendMessage(String user, String message,
			PushOverPriority priority) {
		this.messages.add(new PushOverFakeMessage(user, message, priority));
		return true;
	}

	/**
	 * @return the messages
	 */
	public List<PushOverFakeMessage> getMessages() {
		return messages;
	}
}
