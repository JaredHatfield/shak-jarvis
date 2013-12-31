package com.unitvectory.shak.jarvis.model.smartthings;

import com.unitvectory.shak.jarvis.exception.SmartException;

/**
 * The SmartEvent
 * 
 * @author Jared Hatfield
 * 
 */
public abstract class SmartEvent {

    /**
     * the name
     */
    private String name;

    /**
     * the hubId
     */
    private String hubId;

    /**
     * the locationId
     */
    private String locationId;

    /**
     * the deviceId
     * 
     */
    private String deviceId;

    /**
     * the eventId
     */
    private String eventId;

    /**
     * the date
     */
    private String date;

    /**
     * Creates a new instance of the SmartEvent class.
     * 
     * @param name
     *            the name
     * @param hubId
     *            the hubId
     * @param locationId
     *            the locationId
     * @param deviceId
     *            the deviceId
     * @param eventId
     *            the eventId
     * @param date
     *            the date
     * @throws SmartException
     */
    public SmartEvent(String name, String hubId, String locationId,
            String deviceId, String eventId, String date) throws SmartException {
        this.name = name;
        this.hubId = hubId;
        this.locationId = locationId;
        this.deviceId = deviceId;
        this.eventId = eventId;
        this.date = date;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the hubId
     */
    public String getHubId() {
        return hubId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @return the eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }
}
