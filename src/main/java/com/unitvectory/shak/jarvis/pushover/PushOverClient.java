package com.unitvectory.shak.jarvis.pushover;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * The PushOver client.
 * 
 * @author Jared Hatfield
 *
 */
public class PushOverClient implements PushOver {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(PushOverClient.class);

	private static final String URL = "https://api.pushover.net/1/messages.json";

	private String token;

	public PushOverClient(String token) {
		this.token = token;
	}

	public boolean sendMessage(String user, String message,
			PushOverPriority priority) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(URL);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("token", this.token));
			nameValuePairs.add(new BasicNameValuePair("user", user));
			nameValuePairs.add(new BasicNameValuePair("message", message));
			nameValuePairs.add(new BasicNameValuePair("priority", priority
					.getValue() + ""));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "utf-8"));
			HttpResponse response = client.execute(post);
			return response.getStatusLine().getStatusCode() == 200;
		} catch (Exception e) {
			log.error("Unable to send pushover.", e);
			return false;
		}
	}
}
