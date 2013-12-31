package com.unitvectory.shak.jarvis.model.smartthings;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The SmartContact
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartContact extends SmartEvent {

    /**
     * the status
     */
    private char status;

    /**
     * Creates a new instance of the SmartContact class.
     * 
     * @param publish
     *            the publish
     * @throws SmartException
     */
    public SmartContact(SmartThingsPublish publish) throws SmartException {
        super("contact", publish.getHubId(), publish.getLocationId(), publish
                .getDeviceId(), publish.getId(), publish.getDate());

        if (!this.getName().equals(publish.getName())) {
            throw new SmartException("Incorrect name");
        }

        try {
            this.status = publish.getValue().toUpperCase().charAt(0);
        } catch (Exception e) {
            throw new SmartException("Bad data.", e);
        }
    }

    /**
     * @return the status
     */
    public char getStatus() {
        return status;
    }

}
