package com.unitvectory.shak.jarvis.model;

/**
 * The person location publish
 * 
 * @author Jared Hatfield
 * 
 */
public class PersonLocationPublish {

    /**
     * The JsonPublishRequest used to created this object
     */
    private JsonPublishRequest publish;

    /**
     * the token
     */
    private String token;

    /**
     * the location
     */
    private String location;

    /**
     * the status
     */
    private char status;

    /**
     * Creates a new instance of the PersonLocationPublish class.
     * 
     * @param publish
     *            the publish
     */
    public PersonLocationPublish(JsonPublishRequest publish) {
        this.publish = publish;
        this.token = publish.getData().get("token");
        this.location = publish.getData().get("location");
        String statusString = publish.getData().get("status");
        if (statusString == null || statusString.length() == 0) {
            status = ' ';
        } else {
            status = statusString.charAt(0);
        }
    }

    /**
     * @return the publish
     */
    public JsonPublishRequest getPublish() {
        return publish;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the status
     */
    public char getStatus() {
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Location " + this.token + " " + this.status + " "
                + this.location;
    }

}
