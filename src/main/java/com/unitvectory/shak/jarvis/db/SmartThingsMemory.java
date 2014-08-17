package com.unitvectory.shak.jarvis.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart things in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class SmartThingsMemory implements SmartThingsDAO {

    private Map<String, SmartThingsDeviceDetails> devices;

    private List<SmartEvent> history;

    private Map<String, SmartEvent> recentEvent;

    public SmartThingsMemory() {
        this.devices = new HashMap<String, SmartThingsDeviceDetails>();
        this.history = new ArrayList<SmartEvent>();
        this.recentEvent = new HashMap<String, SmartEvent>();
    }

    public InsertResult insertSmartEvent(SmartEvent event) {
        this.history.add(event);
        this.recentEvent.put(this.getEventId(event), event);
        return InsertResult.Success;
    }

    public SmartThingsDeviceDetails getDeviceDetails(SmartEvent event)
            throws SQLException {
        return this.devices.get(this.getEventId(event));
    }

    public SmartEvent getPreviousEvent(SmartEvent event)
            throws SQLException {
        return this.recentEvent.get(this.getEventId(event));
    }

    private String getEventId(SmartEvent event) {
        String id = "/" + event.getDeviceId() + "/" + event.getLocationId()
                + "/" + event.getHubId();
        id = DigestUtils.sha1Hex(id);
        return id;
    }
}
