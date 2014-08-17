package com.unitvectory.shak.jarvis.db.model;

/**
 * The smart things device details
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThingsDeviceDetails {

	/**
	 * the pid
	 */
	private int pid;

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
	 * @return the pid
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            the pid to set
	 */
	public void setPid(int pid) {
		this.pid = pid;
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
