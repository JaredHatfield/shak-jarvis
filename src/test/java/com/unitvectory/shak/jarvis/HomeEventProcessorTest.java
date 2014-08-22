package com.unitvectory.shak.jarvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.unitvectory.shak.jarvis.db.PushToSpeechMemory;
import com.unitvectory.shak.jarvis.db.ShakDatabase;
import com.unitvectory.shak.jarvis.db.SmartThingsMemory;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
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

	private int home;

	private String pushDeviceId;

	public HomeEventProcessorTest() {
		random = new SecureRandom();
		this.hubId = this.getUUID();
		this.locationId = this.getUUID();
		this.frontDoorId = this.getUUID();
		this.home = random.nextInt();
		this.pushDeviceId = this.getUUID();
	}

	@Test
	public void contactTest() {
		HomeEventProcessor processor = this.getProcessor();

		// Open the door
		JsonPublishRequest requestOpen = RequestGenerator
				.buildContactSmartEvent(new Date(), this.frontDoorId,
						this.hubId, this.locationId, true);
		processor.processEvent(requestOpen);

		// Close the door
		JsonPublishRequest requestClose = RequestGenerator
				.buildContactSmartEvent(new Date(), this.frontDoorId,
						this.hubId, this.locationId, false);
		processor.processEvent(requestClose);

		// Verify the output
		String[] speech = { "Front Door is open... ",
				"Front Door is closed... " };
		String[] notification = {};
		this.verify(processor, speech, notification);
	}

	private HomeEventProcessor getProcessor() {
		// New processor
		HomeEventProcessor processor = new HomeEventProcessor(
				new ShakDatabase(), new PushToSpeechFake(), new PushOverFake());

		// Configure the front door
		SmartThingsMemory st = (SmartThingsMemory) processor.getDatabase().st();
		st.insertDeviceDetails(this.frontDoorId, this.locationId, this.hubId,
				this.home, "Front Door", "Contact", false, false, false);

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
