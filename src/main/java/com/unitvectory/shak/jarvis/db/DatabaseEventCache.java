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
     * the device details
     */
    private Map<String, SmartThingsDeviceDetails> deviceDetails;

    /**
     * Creates a new instance of the DatabaseEventCache class.
     * 
     * @param smartthings
     *            the smart things dao
     */
    public DatabaseEventCache(SmartThingsDAO smartthings) {
        this.st = smartthings;
        this.deviceDetails = new HashMap<String, SmartThingsDeviceDetails>();
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
        this.deviceDetails.clear();
    }
}
