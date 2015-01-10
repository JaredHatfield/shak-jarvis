package com.unitvectory.shak.jarvis.db;

import java.util.HashMap;
import java.util.Map;

import com.unitvectory.shak.jarvis.db.model.LatchValue;

/**
 * The latch in memory database
 * 
 * @author Jared Hatfield
 *
 */
public class LatchMemory implements LatchDAO {

	/**
	 * the data
	 */
	private Map<Integer, Map<String, Boolean>> data;

	/**
	 * Creates a new instance of the LatchMemory class.
	 */
	public LatchMemory() {
		this.data = new HashMap<Integer, Map<String, Boolean>>();
	}

	public LatchValue getLatch(int home, String name) {
		synchronized (this) {
			Integer homeInt = Integer.valueOf(home);
			Map<String, Boolean> homeValues = this.data.get(homeInt);
			if (homeValues == null) {
				return LatchValue.UNKNOWN;
			}

			Boolean val = homeValues.get(name);
			if (val == null) {
				return LatchValue.UNKNOWN;
			}

			if (val.booleanValue()) {
				return LatchValue.TRUE;
			} else {
				return LatchValue.FALSE;
			}
		}
	}

	public void setLatch(int home, String name, boolean value) {
		synchronized (this) {
			Integer homeInt = Integer.valueOf(home);
			Map<String, Boolean> homeValues = this.data.get(homeInt);
			if (homeValues == null) {
				homeValues = new HashMap<String, Boolean>();
				this.data.put(homeInt, homeValues);
			}

			homeValues.put(name, Boolean.valueOf(value));
		}
	}
}
