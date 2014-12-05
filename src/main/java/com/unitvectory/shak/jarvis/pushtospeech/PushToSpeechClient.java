package com.unitvectory.shak.jarvis.pushtospeech;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONObject;

/**
 * Client for using the Push to Speech API.
 * 
 * https://pushtospeech.appspot.com/
 * 
 * @author Jared Hatfield
 * 
 */
public class PushToSpeechClient implements PushToSpeech {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(PushToSpeechClient.class);

	/**
	 * The base API URL.
	 */
	private static final String BaseUrl = "https://pushtospeech.appspot.com/api/v1";

	/**
	 * the random text substitution
	 */
	private RandomTextSubstitution substitution;

	/**
	 * Create a new instance of the PushToSpeechClient class.
	 */
	public PushToSpeechClient() {
		this.substitution = new RandomTextSubstitution();
	}

	public PushToSpeechResult speak(String deviceid, String text) {
		String outText = this.substitution.substitute(text);
		boolean result;
		try {
			// Build the JSON string
			JSONObject requestJson = new JSONObject();
			requestJson.put("deviceid", deviceid);
			requestJson.put("text", outText);
			String jsonString = requestJson.toString();

			// Send the HTTP request
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost postRequest = new HttpPost(BaseUrl + "/speech");
			StringEntity input = new StringEntity(jsonString);
			input.setContentType("application/json;charset=UTF-8");
			postRequest.setEntity(input);
			input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			postRequest.setHeader("Accept", "application/json");
			postRequest.setEntity(input);

			HttpResponse response = httpClient.execute(postRequest);
			result = response.getStatusLine().getStatusCode() == 200;
		} catch (Exception e) {
			log.error("Push to Speech API call failed.", e);
			result = false;
		}

		return new PushToSpeechResult(result, text, outText);
	}
}
