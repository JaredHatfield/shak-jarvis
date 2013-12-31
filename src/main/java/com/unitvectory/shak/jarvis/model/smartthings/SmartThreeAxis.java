package com.unitvectory.shak.jarvis.model.smartthings;

import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;

/**
 * The SmartThreeAxis
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartThreeAxis extends SmartEvent {

    /**
     * the valX
     */
    private double valX;

    /**
     * the valY
     */
    private double valY;

    /**
     * the valZ
     */
    private double valZ;

    /**
     * Creates a new instance of the SMartThreeAxis class
     * 
     * @param publish
     *            the publish
     * @throws SmartException
     */
    public SmartThreeAxis(SmartThingsPublish publish) throws SmartException {
        super("threeAxis", publish.getHubId(), publish.getLocationId(), publish
                .getDeviceId(), publish.getId(), publish.getDate());

        if (!this.getName().equals(publish.getName())) {
            throw new SmartException("Incorrect name");
        }

        String[] vals = publish.getValue().split(",");
        if (vals.length != 3) {
            throw new SmartException("Wrong number of values");
        }

        try {
            this.valX = Double.parseDouble(vals[0]);
            this.valY = Double.parseDouble(vals[1]);
            this.valZ = Double.parseDouble(vals[2]);
        } catch (Exception e) {
            throw new SmartException("Bad data.", e);
        }

    }

    /**
     * @return the valX
     */
    public double getValX() {
        return valX;
    }

    /**
     * @return the valY
     */
    public double getValY() {
        return valY;
    }

    /**
     * @return the valZ
     */
    public double getValZ() {
        return valZ;
    }

}
