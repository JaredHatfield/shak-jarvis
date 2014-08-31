package com.unitvectory.shak.jarvis.model;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * The JSON publish request.
 * 
 * @author Jared Hatfield
 * 
 */
public class JsonPublishRequest {

	/**
	 * The Message that was published.
	 */
	private Message message;

	/**
	 * The data contained in the JSON payload.
	 */
	private Map<String, String> data;

	/**
	 * The timestamp.
	 */
	private String timestamp;

	/**
	 * The valid request.
	 */
	private boolean valid;

	/**
	 * Creates a new instance of the JsonPublishRequest class.
	 * 
	 * @param message
	 *            an sqs message
	 */
	public JsonPublishRequest(Message message) {
		this.message = message;
		if (message == null) {
			this.parseBody(null);
		} else {
			this.parseBody(message.getBody());
		}
	}

	/**
	 * Creates a new instance of the JsonPublishRequest class.
	 * 
	 * @param body
	 *            the json body.
	 */
	public JsonPublishRequest(String body) {
		this.parseBody(body);
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @return the data
	 */
	public Map<String, String> getData() {
		return data;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Parses the JSON body of the SQS message.
	 * 
	 * @param body
	 *            the JSON body
	 */
	private void parseBody(String body) {
		this.data = new TreeMap<String, String>();
		if (body == null) {
			this.valid = false;
			return;
		}

		try {
			// Parse the SQS JSON
			JSONObject snsObject = new JSONObject(body);

			// Get the message
			if (!snsObject.has("Message")) {
				this.valid = false;
				return;
			}

			// Parse the message JSON
			String snsMessage = snsObject.getString("Message");
			this.timestamp = snsObject.getString("Timestamp");

			JSONObject payloadObject = new JSONObject(snsMessage);

			// Save all of the fields
			@SuppressWarnings("unchecked")
			Iterator<String> names = payloadObject.keys();
			while (names.hasNext()) {
				String name = names.next();
				this.data.put(name, payloadObject.getString(name));
			}

			this.valid = true;
		} catch (JSONException e) {
			this.valid = false;
		}
	}
}
