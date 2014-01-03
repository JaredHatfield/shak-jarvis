package com.unitvectory.shak.jarvis.model;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.smartthings.SmartAcceleration;
import com.unitvectory.shak.jarvis.model.smartthings.SmartBattery;
import com.unitvectory.shak.jarvis.model.smartthings.SmartContact;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;
import com.unitvectory.shak.jarvis.model.smartthings.SmartHumidity;
import com.unitvectory.shak.jarvis.model.smartthings.SmartIlluminance;
import com.unitvectory.shak.jarvis.model.smartthings.SmartLqi;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;
import com.unitvectory.shak.jarvis.model.smartthings.SmartPresence;
import com.unitvectory.shak.jarvis.model.smartthings.SmartRssi;
import com.unitvectory.shak.jarvis.model.smartthings.SmartTemperature;
import com.unitvectory.shak.jarvis.model.smartthings.SmartThreeAxis;

/**
 * The SmartThings publish information.
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThingsPublish {

    /**
     * The JsonPublishRequest used to created this object
     */
    private JsonPublishRequest publish;

    /**
     * the auth
     */
    private String auth;

    /**
     * the date
     */
    private String date;

    /**
     * the description
     */
    private String description;

    /**
     * the descriptionText
     */
    private String descriptionText;

    /**
     * the deviceId
     */
    private String deviceId;

    /**
     * the hubId
     */
    private String hubId;

    /**
     * the id
     */
    private String id;

    /**
     * the locationId
     */
    private String locationId;

    /**
     * the name
     */
    private String name;

    /**
     * the source
     */
    private String source;

    /**
     * the unit
     */
    private String unit;

    /**
     * the value
     */
    private String value;

    /**
     * Creates an instance of the SmartThingsPublish class.
     * 
     * @param publish
     *            The JSON publish
     */
    public SmartThingsPublish(JsonPublishRequest publish) {
        this.publish = publish;

        if (publish != null && publish.isValid()) {
            this.auth = publish.getData().get("auth");
            this.date = publish.getData().get("date");
            this.description = publish.getData().get("description");
            this.descriptionText = publish.getData().get("descriptionText");
            this.deviceId = publish.getData().get("deviceId");
            this.hubId = publish.getData().get("hubId");
            this.id = publish.getData().get("id");
            this.locationId = publish.getData().get("locationId");
            this.name = publish.getData().get("name");
            this.source = publish.getData().get("source");
            this.unit = publish.getData().get("unit");
            this.value = publish.getData().get("value");
        }
    }

    /**
     * Builds a SmartEvent object based on the publish.
     * 
     * @return the SmartEvent
     * @throws SmartException
     */
    public SmartEvent buildSmartEvent()
            throws SmartException {
        if (this.name == null) {
            throw new SmartException("No name set");
        } else if (this.name.equals("acceleration")) {
            return new SmartAcceleration(this);
        } else if (this.name.equals("battery")) {
            return new SmartBattery(this);
        } else if (this.name.equals("contact")) {
            return new SmartContact(this);
        } else if (this.name.equals("humidity")) {
            return new SmartHumidity(this);
        } else if (this.name.equals("illuminance")) {
            return new SmartIlluminance(this);
        } else if (this.name.equals("lqi")) {
            return new SmartLqi(this);
        } else if (this.name.equals("motion")) {
            return new SmartMotion(this);
        } else if (this.name.equals("presence")) {
            return new SmartPresence(this);
        } else if (this.name.equals("rssi")) {
            return new SmartRssi(this);
        } else if (this.name.equals("temperature")) {
            return new SmartTemperature(this);
        } else if (this.name.equals("threeAxis")) {
            return new SmartThreeAxis(this);
        } else {
            throw new SmartException("Unknown name " + this.name);
        }
    }

    /**
     * @return the publish
     */
    public JsonPublishRequest getPublish() {
        return publish;
    }

    /**
     * @param publish
     *            the publish to set
     */
    public void setPublish(JsonPublishRequest publish) {
        this.publish = publish;
    }

    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the descriptionText
     */
    public String getDescriptionText() {
        return descriptionText;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @return the hubId
     */
    public String getHubId() {
        return hubId;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
