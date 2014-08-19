package com.unitvectory.shak.jarvis.db.model;

/**
 * The smart things device details
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThingsDeviceDetails {

	/**
	 * the device id
	 */
	private String deviceid;

	/**
	 * the location id
	 */
	private String locationid;

	/**
	 * the hub id
	 */
	private String hubid;

	/**
	 * the home
	 */
	private int home;

	/**
	 * the name
	 */
	private String name;

	/**
	 * the type
	 */
	private String type;

	/**
	 * the inside flag
	 */
	private boolean inside;

	/**
	 * the outside flag
	 */
	private boolean outside;

	/**
	 * the target flag
	 */
	private boolean target;

	/**
	 * Creates a new instance of the SmartThingsDeviceDetails class.
	 */
	public SmartThingsDeviceDetails() {
	}

	/**
	 * Creates a new instance of the SmartThingsDeviceDetails class.
	 * 
	 * @param deviceid
	 *            the device id
	 * @param locationid
	 *            the location id
	 * @param hubid
	 *            the hub id
	 * @param home
	 *            the home
	 * @param name
	 *            the name
	 * @param type
	 *            the type
	 * @param inside
	 *            the inside flag
	 * @param outside
	 *            the outside flag
	 * @param target
	 *            the target flag
	 */
	public SmartThingsDeviceDetails(String deviceid, String locationid,
			String hubid, int home, String name, String type, boolean inside,
			boolean outside, boolean target) {
		super();
		this.deviceid = deviceid;
		this.locationid = locationid;
		this.hubid = hubid;
		this.home = home;
		this.name = name;
		this.type = type;
		this.inside = inside;
		this.outside = outside;
		this.target = target;
	}

	/**
	 * @return the deviceid
	 */
	public String getDeviceid() {
		return deviceid;
	}

	/**
	 * @param deviceid
	 *            the deviceid to set
	 */
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	/**
	 * @return the locationid
	 */
	public String getLocationid() {
		return locationid;
	}

	/**
	 * @param locationid
	 *            the locationid to set
	 */
	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}

	/**
	 * @return the hubid
	 */
	public String getHubid() {
		return hubid;
	}

	/**
	 * @param hubid
	 *            the hubid to set
	 */
	public void setHubid(String hubid) {
		this.hubid = hubid;
	}

	/**
	 * @return the home
	 */
	public int getHome() {
		return home;
	}

	/**
	 * @param home
	 *            the home to set
	 */
	public void setHome(int home) {
		this.home = home;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the inside
	 */
	public boolean isInside() {
		return inside;
	}

	/**
	 * @param inside
	 *            the inside to set
	 */
	public void setInside(boolean inside) {
		this.inside = inside;
	}

	/**
	 * @return the outside
	 */
	public boolean isOutside() {
		return outside;
	}

	/**
	 * @param outside
	 *            the outside to set
	 */
	public void setOutside(boolean outside) {
		this.outside = outside;
	}

	/**
	 * @return the target
	 */
	public boolean isTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(boolean target) {
		this.target = target;
	}
}
