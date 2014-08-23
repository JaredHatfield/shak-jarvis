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
import com.unitvectory.shak.jarvis.pushover.PushOverPriority;
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
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();

		// John Left home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "home", 'N'));
		speechList.add("John has left home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John has left home... ", PushOverPriority.QUIET));

		// Jane Left home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "home", 'N'));
		speechList.add("Jane has left home... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane has left home... ", PushOverPriority.QUIET));

		// Jane At work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "work", 'P'));
		speechList.add("Jane is arriving at work... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving at work... ", PushOverPriority.QUIET));

		// John At work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "work", 'P'));
		speechList.add("John is arriving at work... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving at work... ", PushOverPriority.QUIET));

		// John Left work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "work", 'N'));
		speechList.add("John has left work... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John has left work... ", PushOverPriority.QUIET));

		// Jane Left work
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "work", 'N'));
		speechList.add("Jane has left work... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane has left work... ", PushOverPriority.QUIET));

		// John At home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.johnToken, "home", 'P'));
		speechList.add("John is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving home... ", PushOverPriority.QUIET));

		// Jane At home
		processor.processEvent(RequestGenerator.buildLocation(new Date(),
				this.janeToken, "home", 'P'));
		speechList.add("Jane is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving home... ", PushOverPriority.QUIET));

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
	}

	@Test
	public void contactTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();

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
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
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
			PushOverFakeMessage[] expectedNotification) {
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
			assertEquals(expectedNotification[i].getUser(), notifications
					.get(i).getUser());
			assertEquals(expectedNotification[i].getText(), notifications
					.get(i).getText());
			assertEquals(expectedNotification[i].getPriority(), notifications
					.get(i).getPriority());

		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}
}
