package com.unitvectory.shak.jarvis.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * Helper class for loading resources for testing.
 * 
 * @author Jared Hatfield
 * 
 */
public class ResourceHelper {

	/**
	 * The singleton instance.
	 */
	private static ResourceHelper instance;

	/**
	 * Loads a resource file as a string.
	 * 
	 * @param name
	 *            the file name
	 * @return the string content of the file
	 */
	public static synchronized String load(String name) {
		if (instance == null) {
			instance = new ResourceHelper();
		}

		return instance.read(name);
	}

	/**
	 * Loads a resource file as a string.
	 * 
	 * @param name
	 *            the file name
	 * @return the string content of the file
	 */
	private String read(String name) {
		try {
			InputStream in = this.getClass().getResourceAsStream(name);
			return IOUtils.toString(in, "UTF-8");
		} catch (IOException e) {
			return null;
		}
	}
}
