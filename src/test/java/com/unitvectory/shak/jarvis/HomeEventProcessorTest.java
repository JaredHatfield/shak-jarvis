package com.unitvectory.shak.jarvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.unitvectory.shak.jarvis.db.PersonLocationMemory;
import com.unitvectory.shak.jarvis.db.PushToSpeechMemory;
import com.unitvectory.shak.jarvis.db.ShakDatabase;
import com.unitvectory.shak.jarvis.db.SmartThingsMemory;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.model.RequestGenerator;
import com.unitvectory.shak.jarvis.pushover.PushOverFake;
import com.unitvectory.shak.jarvis.pushover.PushOverFakeMessage;
import com.unitvectory.shak.jarvis.pushtospeech.PushToSpeechFake;

/**
 * Test the event processor end-to-end.
 * 
 * @author Jared Hatfield
 *
 */
public class HomeEventProcessorTest {

	private SecureRandom random;

	private String hubId;

	private String locationId;

	private String frontDoorId;

	private String backDoorId;

	private int home;

	private String johnToken;

	private String johnPushOver;

	private String janeToken;

	private String janePushOver;

	private String pushDeviceId;

	public HomeEventProcessorTest() {
		random = new SecureRandom();
		this.hubId = this.getUUID();
		this.locationId = this.getUUID();
		this.frontDoorId = this.getUUID();
		this.backDoorId = this.getUUID();
		this.home = random.nextInt();
		this.pushDeviceId = this.getUUID();
		this.johnToken = this.getUUID();
		this.johnPushOver = this.getUUID();
		this.janeToken = this.getUUID();
		this.janePushOver = this.getUUID();
	}

	@Test
	public void locationTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();

		// John Left home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "home", 'N'));
		speechList.add("John has left home... ");

		// Jane Left home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "home", 'N'));
		speechList.add("Jane has left home... ");

		// Jane At work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "work", 'P'));
		speechList.add("Jane is arriving at work... ");

		// John At work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "work", 'P'));
		speechList.add("John is arriving at work... ");

		// John Left work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "work", 'N'));
		speechList.add("John has left work... ");

		// Jane Left work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "work", 'N'));
		speechList.add("Jane has left work... ");

		// John At home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "home", 'P'));
		speechList.add("John is arriving home... ");

		// Jane At home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "home", 'P'));
		speechList.add("Jane is arriving home... ");

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		String[] notification = {};
		this.verify(processor, speech, notification);
	}

	@Test
	public void contactTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();

		// Open the front door
		processor.processEvent(RequestGenerator
				.buildContactSmartEvent(new Date(), this.frontDoorId,
						this.hubId, this.locationId, true));
		speechList.add("Front Door is open... ");

		// Close the front door
		processor.processEvent(RequestGenerator.buildContactSmartEvent(
				new Date(), this.frontDoorId, this.hubId, this.locationId,
				false));
		speechList.add("Front Door is closed... ");

		// Open the back door
		processor
				.processEvent(RequestGenerator.buildContactSmartEvent(
						new Date(), this.backDoorId, this.hubId,
						this.locationId, true));
		speechList.add("Back Door is open... ");

		// Close the back door
		processor.processEvent(RequestGenerator
				.buildContactSmartEvent(new Date(), this.backDoorId,
						this.hubId, this.locationId, false));
		speechList.add("Back Door is closed... ");

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		String[] notification = {};
		this.verify(processor, speech, notification);
	}

	private HomeEventProcessor getProcessor() {
		// New processor
		HomeEventProcessor processor = new HomeEventProcessor(
				new ShakDatabase(), new PushToSpeechFake(), new PushOverFake());

		// Configure the person
		PersonLocationMemory pl = (PersonLocationMemory) processor
				.getDatabase().pl();
		pl.insertPerson(new PersonLocationDetails(this.johnToken, "John",
				"Doe", this.home, this.johnPushOver));
		pl.insertPerson(new PersonLocationDetails(this.janeToken, "Jane",
				"Doe", this.home, this.janePushOver));

		// Configure the front door
		SmartThingsMemory st = (SmartThingsMemory) processor.getDatabase().st();
		st.insertDeviceDetails(this.frontDoorId, this.locationId, this.hubId,
				this.home, "Front Door", "Contact", false, false, false);
		st.insertDeviceDetails(this.backDoorId, this.locationId, this.hubId,
				this.home, "Back Door", "Contact", false, false, false);

		// Configure the push device
		PushToSpeechMemory pts = (PushToSpeechMemory) processor.getDatabase()
				.pts();
		pts.insert(this.home, this.pushDeviceId);

		// All done
		return processor;
	}

	private void verify(HomeEventProcessor processor, String[] expectedSpeech,
			String[] expectedNotification) {
		assertNotNull(expectedSpeech);
		PushToSpeechFake pushToSpeech = (PushToSpeechFake) processor
				.getPushToSpeech();
		List<String> speech = pushToSpeech.getHistory(pushDeviceId);
		assertNotNull(speech);
		assertEquals(expectedSpeech.length, speech.size());
		for (int i = 0; i < expectedSpeech.length; i++) {
			assertEquals(expectedSpeech[i], speech.get(i));
		}

		assertNotNull(expectedNotification);
		PushOverFake pushOver = (PushOverFake) processor.getPushover();
		List<PushOverFakeMessage> notifications = pushOver.getMessages();
		assertEquals(expectedNotification.length, notifications.size());
		for (int i = 0; i < expectedNotification.length; i++) {
			assertEquals(expectedNotification[i], notifications.get(i)
					.getText());
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}
}
