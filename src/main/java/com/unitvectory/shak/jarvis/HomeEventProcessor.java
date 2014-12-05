package com.unitvectory.shak.jarvis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.action.ActionNotification;
import com.unitvectory.shak.jarvis.action.PersonLocationAction;
import com.unitvectory.shak.jarvis.action.SmartContactAction;
import com.unitvectory.shak.jarvis.action.SmartMotionAction;
import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.InsertResult;
import com.unitvectory.shak.jarvis.db.ShakDatabase;
import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;
import com.unitvectory.shak.jarvis.model.smartthings.SmartContact;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;
import com.unitvectory.shak.jarvis.pushover.PushOver;
import com.unitvectory.shak.jarvis.pushover.PushOverPriority;
import com.unitvectory.shak.jarvis.pushtospeech.PushToSpeech;
import com.unitvectory.shak.jarvis.pushtospeech.PushToSpeechResult;

/**
 * The home event processor
 * 
 * @author Jared Hatfield
 * 
 */
public class HomeEventProcessor {

	/**
	 * the log
	 */
	private static Logger log = Logger.getLogger(HomeEventProcessor.class);

	/**
	 * the database
	 */
	private ShakDatabase database;

	/**
	 * the person location action
	 */
	private PersonLocationAction locationAction;

	/**
	 * the smart contact action
	 */
	private SmartContactAction contactAction;

	/**
	 * the smart motion action
	 */
	private SmartMotionAction motionAction;

	/**
	 * the push to speech
	 */
	private PushToSpeech pushToSpeech;

	/**
	 * the pushover client
	 */
	private PushOver pushover;

	/**
	 * Creates a new instance of the HomeEventProcessor class.
	 * 
	 * @param database
	 *            the database
	 * @param pushToSpeech
	 *            the push to speech client
	 * @param pushover
	 *            the pushover client
	 */
	public HomeEventProcessor(ShakDatabase database, PushToSpeech pushToSpeech,
			PushOver pushover) {
		this.database = database;
		this.pushToSpeech = pushToSpeech;
		this.contactAction = new SmartContactAction();
		this.motionAction = new SmartMotionAction();
		this.locationAction = new PersonLocationAction();
		this.pushover = pushover;
	}

	/**
	 * process a smart event
	 * 
	 * @param request
	 *            the request
	 * @return true if successful; otherwise false
	 */
	public boolean processEvent(JsonPublishRequest request) {
		if (!request.isValid()) {
			return false;
		}

		String type = request.getData().get("type");
		if (type == null) {
			return false;
		}

		List<ActionNotification> notifications = new ArrayList<ActionNotification>();

		if (type.equals("smartthings")) {
			List<ActionNotification> smartNotifications = this
					.processSmartEvent(request);
			if (smartNotifications == null) {
				return false;
			}

			this.append(notifications, smartNotifications);
		} else if (type.equals("location")) {
			List<ActionNotification> locationNotifications = this
					.processLocationEvent(request);
			if (locationNotifications == null) {
				return false;
			}

			this.append(notifications, locationNotifications);
		} else {
			log.info("Unknown event type " + type);
		}

		// Lots of events
		for (ActionNotification notification : notifications) {
			log.info(notification);

			if (this.pushover != null & notification.isPush()) {
				// Send PushOver
				this.pushover.sendMessage(notification.getPushOverToken(),
						notification.getNotification(), PushOverPriority.QUIET);
			} else if (notification.isSpeak()) {
				// Sent PushToSpeech
				List<String> deviceIds = this.database.pts().getPushDeviceIds(
						notification.getHome());
				if (deviceIds == null) {
					continue;
				}

				for (String deviceid : deviceIds) {
					PushToSpeechResult result = this.pushToSpeech.speak(
							deviceid, notification.getNotification());
					if (result.isResult()) {
						String event = notification.getEvent();
						String text = result.getOutputText();
						this.database.pts()
								.insertHistory(deviceid, event, text);
					}
				}
			}
		}

		return true;
	}

	/**
	 * Get the shak database.
	 * 
	 * @return the database
	 */
	public ShakDatabase getDatabase() {
		return database;
	}

	/**
	 * Get the push to speech client.
	 * 
	 * @return the push to speech client
	 */
	public PushToSpeech getPushToSpeech() {
		return pushToSpeech;
	}

	/**
	 * @return the pushover
	 */
	public PushOver getPushover() {
		return pushover;
	}

	/**
	 * Get the list of events given a location.
	 * 
	 * @param request
	 *            the request
	 * @return the list of notification
	 */
	private List<ActionNotification> processLocationEvent(
			JsonPublishRequest request) {
		PersonLocationPublish location = new PersonLocationPublish(request);

		InsertResult insertResult = this.database.pl().insertLocation(location);
		log.info(insertResult + " - " + location);
		switch (insertResult) {
		case Duplicate:
			// A duplicate event needs no more processing
			return null;
		case Error:
			// An error should not be processed
			return null;
		case Success:
			// Just keep going...
			break;
		default:
			break;
		}

		DatabaseEventCache cache = new DatabaseEventCache(this.database);

		List<ActionNotification> notifications = new ArrayList<ActionNotification>();
		this.append(notifications,
				this.locationAction.getActions(cache, location));

		// TODO: Process the location
		return notifications;
	}

	/**
	 * Get the list of events given a smart event.
	 * 
	 * @param request
	 *            the request
	 * @return the list of notifications
	 */
	private List<ActionNotification> processSmartEvent(
			JsonPublishRequest request) {
		SmartThingsPublish smart = new SmartThingsPublish(request);
		SmartEvent event = null;
		try {
			event = smart.buildSmartEvent();
		} catch (SmartException e) {
			log.error("Failed to parse SmartPublish request", e);
			return null;
		}

		// Insert the event into the database
		InsertResult insertResult = this.database.st().insertSmartEvent(event);
		log.info(insertResult + " - " + event.getName());
		switch (insertResult) {
		case Duplicate:
			// A duplicate event needs no more processing
			return null;
		case Error:
			// An error should not be processed
			return null;
		case Success:
			// Just keep going...
			break;
		default:
			break;
		}

		// The cache
		DatabaseEventCache cache = new DatabaseEventCache(this.database);

		// Get the list of actions
		List<ActionNotification> notifications = new ArrayList<ActionNotification>();
		if (event instanceof SmartMotion) {
			this.append(notifications,
					this.motionAction.getActions(cache, event));
		} else if (event instanceof SmartContact) {
			this.append(notifications,
					this.contactAction.getActions(cache, event));
		}

		return notifications;
	}

	/**
	 * Appends one array to another.
	 * 
	 * @param master
	 * @param children
	 */
	private void append(List<ActionNotification> master,
			List<ActionNotification> children) {
		if (master == null) {
			return;
		} else if (children == null) {
			return;
		}

		for (ActionNotification child : children) {
			master.add(child);
		}
	}
}
