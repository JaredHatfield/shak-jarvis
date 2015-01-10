package com.unitvectory.shak.jarvis.db;

import com.unitvectory.shak.jarvis.db.model.LatchValue;

/**
 * The latch DAO
 * 
 * @author Jared Hatfield
 *
 */
public interface LatchDAO {

	/**
	 * Gets the value of a latch
	 * 
	 * @param home
	 *            the home id
	 * @param name
	 *            the latch name
	 * @return the latch value
	 */
	LatchValue getLatch(int home, String name);

	/**
	 * Sets the value of a latch
	 * 
	 * @param home
	 *            the home id
	 * @param name
	 *            the latch name
	 * @param value
	 *            the latch value
	 */
	void setLatch(int home, String name, boolean value);
}
