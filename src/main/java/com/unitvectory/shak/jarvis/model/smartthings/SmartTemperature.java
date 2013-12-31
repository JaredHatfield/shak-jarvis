package com.unitvectory.shak.jarvis.model.smartthings;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The SmartTemperature
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartTemperature extends SmartEvent {

    /**
     * the value
     */
    private double value;

    /**
     * the unit
     */
    private char unit;

    /**
     * Creates a new instance of the SmartTemperature class.
     * 
     * @param publish
     *            the publish
     * @throws SmartException
     */
    public SmartTemperature(SmartThingsPublish publish) throws SmartException {
        super("temperature", publish.getHubId(), publish.getLocationId(),
                publish.getDeviceId(), publish.getId(), publish.getDate());

        if (!this.getName().equals(publish.getName())) {
            throw new SmartException("Incorrect name");
        }

        try {
            this.value = Double.parseDouble(publish.getValue());
            this.unit = publish.getUnit().charAt(0);
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

    /**
     * @return the unit
     */
    public char getUnit() {
        return unit;
    }

}
