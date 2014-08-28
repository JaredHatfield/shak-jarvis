package com.unitvectory.shak.jarvis.pushtospeech;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Substitutes random text
 * 
 * @author Jared Hatfield
 *
 */
public class RandomTextSubstitution {

	/**
	 * the random number generator
	 */
	private Random random;

	/**
	 * the dictionary of substitutions
	 */
	private Map<String, List<String>> dictionary;

	/**
	 * Creates a new instance of the RandomTextSubstitution class.
	 */
	public RandomTextSubstitution() {
		this.random = new SecureRandom();
		this.dictionary = new HashMap<String, List<String>>();

		// {VAILED_THREAT}
		List<String> vailedThreat = new ArrayList<String>();
		this.dictionary.put("{VAILED_THREAT}", vailedThreat);
		vailedThreat.add("Who goes there?");
		vailedThreat.add("What was that?");
		vailedThreat.add("You don't belong here.");
		vailedThreat.add("What are you doing here?");
		vailedThreat.add("Did you hear something?");
		vailedThreat.add("Systems activated.");
		vailedThreat.add("Unauthorized motion detected.");
	}

	public String substitute(String text) {
		if (text == null) {
			return null;
		}

		for (String key : this.dictionary.keySet()) {
			while (text.contains(key)) {
				List<String> list = this.dictionary.get(key);
				int index = this.random.nextInt(list.size());
				String replace = list.get(index);
				text = text.replace(key, replace);
			}
		}

		return text;
	}
}
