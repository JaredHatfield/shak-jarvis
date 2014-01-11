package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The database event cache
 * 
 * @author Jared Hatfield
 * 
 */
public class DatabaseEventCache {

    /**
     * the smart things dao
     */
    private SmartThingsDAO st;

    /**
     * the personal location DAO.
     */
    private PersonLocationDAO pl;

    /**
     * the location token name
     */
    private Map<String, String> locationTokenName;

    /**
     * the device details
     */
    private Map<String, SmartThingsDeviceDetails> deviceDetails;

    /**
     * Creates a new instance of the DatabaseEventCache class.
     * 
     * @param smartthings
     *            the smart things DAO
     * @param personlocation
     *            the person location DAO
     */
    public DatabaseEventCache(SmartThingsDAO smartthings,
            PersonLocationDAO personlocation) {
        this.st = smartthings;
        this.pl = personlocation;
        this.locationTokenName = new HashMap<String, String>();
        this.deviceDetails = new HashMap<String, SmartThingsDeviceDetails>();
    }

    /**
     * Gets the person name.
     * 
     * @param token
     *            the token
     * @return the name
     */
    public String getPersonName(String token) {
        String name = this.locationTokenName.get(token);
        if (name != null) {
            return name;
        }

        name = this.pl.getPersonName(token);
        this.locationTokenName.put(token, name);
        return name;
    }

    /**
     * Gets the device details
     * 
     * @param event
     *            the event
     * @return the device details
     */
    public SmartThingsDeviceDetails getDeviceDetails(SmartEvent event) {
        String key =
                event.getDeviceId() + event.getLocationId() + event.getHubId();
        if (this.deviceDetails.containsKey(key)) {
            return this.deviceDetails.get(key);
        }

        SmartThingsDeviceDetails details = null;
        try {
            details = this.st.getDeviceDetails(event);
        } catch (SQLException e) {
        }

        this.deviceDetails.put(key, details);
        return details;
    }

    /**
     * Clears the cache
     */
    public void clear() {
        this.locationTokenName.clear();
        this.deviceDetails.clear();
    }
}
