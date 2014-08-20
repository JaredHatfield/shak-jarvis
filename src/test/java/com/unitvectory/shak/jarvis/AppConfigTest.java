package com.unitvectory.shak.jarvis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test loading the AppConfig.
 * 
 * @author Jared Hatfield
 *
 */
public class AppConfigTest {

	@Test
	public void parseConfigTest() {
		String path = AppConfigTest.class.getResource("/config.xml").getPath();
		AppConfig config = AppConfig.load(path);
		assertNotNull(config);
		assertEquals("AWS_ACCESS_KEY", config.getAwsAccessKey());
		assertEquals("AWS_SECRET_KEY", config.getAwsSecretKey());
		assertEquals("QUEUE_URL", config.getQueueUrl());
		assertEquals("DB_URL", config.getDbUrl());
		assertEquals("DB_USER", config.getDbUser());
		assertEquals("DB_PASSWORD", config.getDbPassword());
	}
}
