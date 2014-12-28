package com.unitvectory.shak.jarvis.db.model;

/**
 * The weather details
 * 
 * @author Jared Hatfield
 *
 */
public class WeatherDetails {

	/**
	 * the summary
	 */
	private String summary;

	/**
	 * the min temperature
	 */
	private int temperatureMin;

	/**
	 * the max temperature
	 */
	private int temperatureMax;

	/**
	 * Creates a new instance of the WeatherDetails class.
	 */
	public WeatherDetails() {
	}

	/**
	 * Creates a new instance of the WeatherDetails class.
	 * 
	 * @param summary
	 *            the summary
	 * @param temperatureMin
	 *            the min temperature
	 * @param temperatureMax
	 *            the max temperature
	 */
	public WeatherDetails(String summary, int temperatureMin, int temperatureMax) {
		super();
		this.summary = summary;
		this.temperatureMin = temperatureMin;
		this.temperatureMax = temperatureMax;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @return the temperatureMin
	 */
	public int getTemperatureMin() {
		return temperatureMin;
	}

	/**
	 * @return the temperatureMax
	 */
	public int getTemperatureMax() {
		return temperatureMax;
	}
}
