package com.unitvectory.shak.jarvis.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

/**
 * Generate example request payloads.
 * 
 * Used for testing.
 * 
 * @author Jared Hatfield
 * 
 */
public class RequestGenerator {

	public static JsonPublishRequest buildContactSmartEvent(Date date,
			String deviceId, String hubId, String locationId, boolean value) {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("name", "contact");
		if (value) {
			fields.put("value", "open");
		} else {
			fields.put("value", "closed");
		}

		return buildSmartEvent(date, deviceId, hubId, locationId, fields);
	}

	public static JsonPublishRequest buildMotionSmartEvent(Date date,
			String deviceId, String hubId, String locationId, boolean value) {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("name", "motion");
		if (value) {
			fields.put("value", "active");
		} else {
			fields.put("value", "inactive");
		}

		return buildSmartEvent(date, deviceId, hubId, locationId, fields);
	}

	public static JsonPublishRequest buildLocation(Date date, String token,
			String location, char status) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			String dateString = df.format(date);

			JSONObject message = new JSONObject();
			message.put("auth", "foobar");
			message.put("type", "location");
			message.put("token", token);
			message.put("location", location);
			message.put("status", status + "");
			message.put("date", dateString);

			return buildSQS(dateString, message);
		} catch (JSONException e) {
			return null;
		}
	}

	private static JsonPublishRequest buildSmartEvent(Date date,
			String deviceId, String hubId, String locationId,
			Map<String, String> fields) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			String dateString = df.format(date);

			JSONObject message = new JSONObject();
			message.put("auth", "foobar");
			message.put("type", "smartthings");
			message.put("id", UUID.randomUUID().toString());
			message.put("deviceId", deviceId);
			message.put("hubId", hubId);
			message.put("locationId", locationId);
			message.put("description", "NOT_SET");
			message.put("descriptionText", "NOT_SET");
			message.put("source", "DEVICE");
			message.put("date", dateString);
			for (Entry<String, String> field : fields.entrySet()) {
				message.put(field.getKey(), field.getValue());
			}

			return buildSQS(dateString, message);
		} catch (JSONException e) {
			return null;
		}
	}

	private static JsonPublishRequest buildSQS(String dateString,
			JSONObject message) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("Type", "Notification");
		json.put("MessageId", UUID.randomUUID().toString());
		json.put("TopicArn", "arn:aws:sns:us-east-1:00000000000:sensors");
		json.put("Timestamp", dateString);
		json.put("SignatureVersion", "0");
		json.put("Signature", "NOT_SET");
		json.put("SigningCertURL", "NOT_SET");
		json.put("UnsubscribeURL", "NOT_SET");
		json.put("Message", message.toString());
		return new JsonPublishRequest(json.toString());
	}
}
