package com.unitvectory.shak.jarvis.pushover;

/**
 * The PushOver message priority
 * 
 * @author Jared Hatfield
 *
 */
public enum PushOverPriority {

	/**
	 * generate no notification/alert
	 */
	NO_ALERT(-2),

	/**
	 * lways send as a quiet notification
	 */
	QUIET(-1),

	/**
	 * default priority
	 */
	NORMAL(0),

	/**
	 * display as high-priority and bypass the user's quiet hours
	 */
	HIGH_PRIORITY(1);

	private int value;

	private PushOverPriority(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
