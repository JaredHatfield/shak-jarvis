package com.unitvectory.shak.jarvis.model.smartthings;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The SmartLqi
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartLqi extends SmartEvent {

    /**
     * the value
     */
    private double value;

    /**
     * Creates a new instance of the SmartLqi class.
     * 
     * @param publish
     *            the publish
     * @throws SmartException
     */
    public SmartLqi(SmartThingsPublish publish) throws SmartException {
        super("lqi", publish.getHubId(), publish.getLocationId(), publish
                .getDeviceId(), publish.getId(), publish.getDate());

        if (!this.getName().equals(publish.getName())) {
            throw new SmartException("Incorrect name");
        }

        try {
            this.value = Double.parseDouble(publish.getValue());
        } catch (Exception e) {
            throw new SmartException("Bad data.", e);
        }
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

}
