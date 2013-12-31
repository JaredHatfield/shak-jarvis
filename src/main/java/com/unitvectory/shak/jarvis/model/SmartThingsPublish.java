package com.unitvectory.shak.jarvis.model;

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
