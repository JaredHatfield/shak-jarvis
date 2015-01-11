package com.unitvectory.shak.jarvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.junit.Test;

import com.unitvectory.shak.jarvis.db.PersonLocationMemory;
import com.unitvectory.shak.jarvis.db.PushToSpeechMemory;
import com.unitvectory.shak.jarvis.db.ShakDatabase;
import com.unitvectory.shak.jarvis.db.SmartThingsMemory;
import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.db.model.WeatherDetails;
import com.unitvectory.shak.jarvis.model.RequestGenerator;
import com.unitvectory.shak.jarvis.pushover.PushOverFake;
import com.unitvectory.shak.jarvis.pushover.PushOverFakeMessage;
import com.unitvectory.shak.jarvis.pushover.PushOverPriority;
import com.unitvectory.shak.jarvis.pushtospeech.PushToSpeechFake;
import com.unitvectory.shak.jarvis.util.SynchronousExecutor;

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

	private String frontPorchId;

	private String kitchenId;

	private int home;

	private String janeToken;

	private String janePushOver;

	private String johnToken;

	private String johnPushOver;

	private String pushDeviceId;

	public HomeEventProcessorTest() {
		random = new SecureRandom();
		this.hubId = this.getUUID();
		this.locationId = this.getUUID();
		this.frontDoorId = this.getUUID();
		this.backDoorId = this.getUUID();
		this.frontPorchId = this.getUUID();
		this.kitchenId = this.getUUID();
		this.home = random.nextInt();
		this.pushDeviceId = this.getUUID();
		this.janeToken = "A-" + this.getUUID();
		this.janePushOver = this.getUUID();
		this.johnToken = "B-" + this.getUUID();
		this.johnPushOver = this.getUUID();
	}

	@Test
	public void goodMorningTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();
		PersonLocationMemory pl = (PersonLocationMemory) processor
				.getDatabase().pl();

		// Kitchen motion (John arrived home; Jane left work)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-10-31 01:00:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("{VAILED_THREAT}");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));

		// John At home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-10-31 01:01:00"), this.johnToken, "home", 'P'));
		speechList.add("John is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving home... ", PushOverPriority.QUIET));

		// Jane At home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-10-31 01:02:00"), this.janeToken, "home", 'P'));
		speechList.add("Jane is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving home... ", PushOverPriority.QUIET));

		// Kitchen motion (John arrived home; Jane left work)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-10-31 01:02:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Welcome home Jane... Welcome home John... ");

		// Good morning motion
		pl.addWeather("2014-10-31", new WeatherDetails("Sunny.", 70, 80));
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-10-31 11:00:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Good morning... " + "Today is Halloween... "
				+ "The weather today is... " + "Sunny... "
				+ "With a low of 70 and a high of 80... ");

		// More motion during the day
		for (int i = 12; i < 24; i++) {
			processor.processEvent(RequestGenerator.buildMotionSmartEvent(
					date("2014-10-31 " + i + ":00:00"), this.kitchenId,
					this.hubId, this.locationId, true));
		}

		// Good morning motion
		pl.addWeather("2014-11-01", new WeatherDetails("Cloudy.", 71, 81));
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-11-01 11:00:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Good morning... " + "The weather today is... "
				+ "Cloudy... " + "With a low of 71 and a high of 81... ");

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
	}

	@Test
	public void invalidLocationTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();

		// John (invalid)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:00:00"), this.johnToken, "home", 'Q'));

		// John (invalid)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:01:00"), this.johnToken, "foo", 'N'));

		// John Left home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:02:00"), this.johnToken, "home", 'N'));
		speechList.add("John has left home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John has left home... ", PushOverPriority.QUIET));

		// John Left home (duplicate)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:03:00"), this.johnToken, "home", 'N'));

		// John (invalid)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:04:00"), this.johnToken, "home", 'Q'));

		// John (invalid)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:05:00"), this.johnToken, "foo", 'N'));

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
	}

	@Test
	public void locationTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();

		// Kitchen motion (John arrived home; Jane left work)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 19:00:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("{VAILED_THREAT}");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));

		// John Left home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:00:00"), this.johnToken, "home", 'N'));
		speechList.add("John has left home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John has left home... ", PushOverPriority.QUIET));

		// Jane Left home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:01:00"), this.janeToken, "home", 'N'));
		speechList.add("Jane has left home... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane has left home... ", PushOverPriority.QUIET));

		// Jane left home (duplicate)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:01:00"), this.janeToken, "home", 'N'));

		// Jane At work
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:02:00"), this.janeToken, "work", 'P'));
		speechList.add("Jane is arriving at work... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving at work... ", PushOverPriority.QUIET));

		// Jane At work (duplicate)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:02:30"), this.janeToken, "work", 'P'));

		// John At work
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:03:00"), this.johnToken, "work", 'P'));
		speechList.add("John is arriving at work... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving at work... ", PushOverPriority.QUIET));

		// John Left work
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:04:00"), this.johnToken, "work", 'N'));
		speechList.add("John has left work... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John has left work... ", PushOverPriority.QUIET));

		// John Left work (duplicate)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:04:30"), this.johnToken, "work", 'N'));

		// Jane Left work
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:05:00"), this.janeToken, "work", 'N'));
		speechList.add("Jane has left work... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane has left work... ", PushOverPriority.QUIET));

		// John At home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:06:00"), this.johnToken, "home", 'P'));
		speechList.add("John is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving home... ", PushOverPriority.QUIET));

		// John At home (duplicate)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:06:30"), this.johnToken, "home", 'P'));

		// Kitchen motion (John arrived home; Jane left work)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:10:30"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Welcome home John... Jane left work 5 minutes ago... ");

		// Jane At home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:19:00"), this.janeToken, "home", 'P'));
		speechList.add("Jane is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving home... ", PushOverPriority.QUIET));

		// Kitchen motion (Jane arrived home)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:20:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Welcome home Jane... ");

		// Jane At home (again)
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:21:00"), this.janeToken, "home", 'P'));

		// Kitchen motion (Jane still at home)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:22:00"), this.kitchenId, this.hubId,
				this.locationId, true));

		// More motion
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:30:00"), this.kitchenId, this.hubId,
				this.locationId, true));

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
		processor.processEvent(RequestGenerator.buildContactSmartEvent(
				date("2014-08-28 20:00:00"), this.frontDoorId, this.hubId,
				this.locationId, true));
		speechList.add("Front Door is open... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Front Door is open... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Front Door is open... ", PushOverPriority.QUIET));

		// Close the front door
		processor.processEvent(RequestGenerator.buildContactSmartEvent(
				date("2014-08-28 20:01:00"), this.frontDoorId, this.hubId,
				this.locationId, false));
		speechList.add("Front Door is closed... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Front Door is closed... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Front Door is closed... ", PushOverPriority.QUIET));

		// Open the back door
		processor.processEvent(RequestGenerator.buildContactSmartEvent(
				date("2014-08-28 20:02:00"), this.backDoorId, this.hubId,
				this.locationId, true));
		speechList.add("Back Door is open... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Back Door is open... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Back Door is open... ", PushOverPriority.QUIET));

		// Close the back door
		processor.processEvent(RequestGenerator.buildContactSmartEvent(
				date("2014-08-28 20:03:00"), this.backDoorId, this.hubId,
				this.locationId, false));
		speechList.add("Back Door is closed... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Back Door is closed... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Back Door is closed... ", PushOverPriority.QUIET));

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
	}

	@Test
	public void motionTest() {
		HomeEventProcessor processor = this.getProcessor();
		List<String> speechList = new ArrayList<String>();
		List<PushOverFakeMessage> pushList = new ArrayList<PushOverFakeMessage>();

		// Jane At work
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:00:00"), this.janeToken, "work", 'P'));
		speechList.add("Jane is arriving at work... ");
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Jane is arriving at work... ", PushOverPriority.QUIET));

		// Front porch motion
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:01:00"), this.frontPorchId, this.hubId,
				this.locationId, true));
		speechList.add("Front Porch motion... ");

		// Front porch inactive
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:02:00"), this.frontPorchId, this.hubId,
				this.locationId, false));

		// Kitchen motion (no one home)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:03:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("{VAILED_THREAT}");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));
		pushList.add(new PushOverFakeMessage(this.johnPushOver,
				"Unexpected motion in the Kitchen... ", PushOverPriority.QUIET));

		// John At home
		processor.processEvent(RequestGenerator.buildLocation(
				date("2014-08-28 20:04:00"), this.johnToken, "home", 'P'));
		speechList.add("John is arriving home... ");
		pushList.add(new PushOverFakeMessage(this.janePushOver,
				"John is arriving home... ", PushOverPriority.QUIET));

		// Kitchen motion (someone home)
		processor.processEvent(RequestGenerator.buildMotionSmartEvent(
				date("2014-08-28 20:05:00"), this.kitchenId, this.hubId,
				this.locationId, true));
		speechList.add("Welcome home John... Jane is still at work... ");

		// Verify the output
		String[] speech = speechList.toArray(new String[speechList.size()]);
		PushOverFakeMessage[] push = pushList
				.toArray(new PushOverFakeMessage[pushList.size()]);
		this.verify(processor, speech, push);
	}

	private HomeEventProcessor getProcessor() {
		// New processor
		HomeEventProcessor processor = new HomeEventProcessor(
				new ShakDatabase(), new PushToSpeechFake(), new PushOverFake(),
				new SynchronousExecutor());

		// Configure the people
		PersonLocationMemory pl = (PersonLocationMemory) processor
				.getDatabase().pl();
		pl.insertPerson(new PersonLocationDetails(this.johnToken, "John",
				"Doe", this.home, this.johnPushOver));
		pl.insertPerson(new PersonLocationDetails(this.janeToken, "Jane",
				"Doe", this.home, this.janePushOver));

		// Configure the doors
		SmartThingsMemory st = (SmartThingsMemory) processor.getDatabase().st();
		st.insertDeviceDetails(this.frontDoorId, this.locationId, this.hubId,
				this.home, "Front Door", "Contact", false, false, false);
		st.insertDeviceDetails(this.backDoorId, this.locationId, this.hubId,
				this.home, "Back Door", "Contact", false, false, false);

		// Configure the motion sensors
		st.insertDeviceDetails(this.frontPorchId, this.locationId, this.hubId,
				this.home, "Front Porch", "Motion", false, true, false);
		st.insertDeviceDetails(this.kitchenId, this.locationId, this.hubId,
				this.home, "Kitchen", "Motion", true, false, true);

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
		for (int i = 0; i < Math.min(expectedSpeech.length, speech.size()); i++) {
			assertEquals(expectedSpeech[i], speech.get(i));
		}

		assertEquals(expectedSpeech.length, speech.size());

		assertNotNull(expectedNotification);
		PushOverFake pushOver = (PushOverFake) processor.getPushover();
		List<PushOverFakeMessage> notifications = pushOver.getMessages();
		for (int i = 0; i < Math.min(expectedNotification.length,
				notifications.size()); i++) {
			assertEquals(expectedNotification[i].getUser(), notifications
					.get(i).getUser());
			assertEquals(expectedNotification[i].getText(), notifications
					.get(i).getText());
			assertEquals(expectedNotification[i].getPriority(), notifications
					.get(i).getPriority());
		}

		assertEquals(expectedNotification.length, notifications.size());
	}

	private Date date(String dateString) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("UTC"));
			return df.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	private String getUUID() {
		return UUID.randomUUID().toString();
	}
}
