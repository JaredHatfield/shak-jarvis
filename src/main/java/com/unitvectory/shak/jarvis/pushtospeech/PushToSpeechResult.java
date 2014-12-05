package com.unitvectory.shak.jarvis.pushtospeech;

/**
 * The PushToSpeech result
 * 
 * @author Jared Hatfield
 *
 */
public class PushToSpeechResult {

	private boolean result;

	private String originalText;

	private String outputText;

	/**
	 * Creates a new instance of the PushToSpeechResult class.
	 * 
	 * @param result
	 *            the result
	 * @param originalText
	 *            the original text
	 * @param outputText
	 *            the output text
	 */
	public PushToSpeechResult(boolean result, String originalText,
			String outputText) {
		this.result = result;
		this.originalText = originalText;
		this.outputText = outputText;
	}

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @return the originalText
	 */
	public String getOriginalText() {
		return originalText;
	}

	/**
	 * @return the outputText
	 */
	public String getOutputText() {
		return outputText;
	}
}
