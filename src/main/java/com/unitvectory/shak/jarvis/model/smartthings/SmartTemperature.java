package com.unitvectory.shak.jarvis.model.smartthings;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    /**
     * The history query.
     */
    private static final String HistoryQuery =
            "INSERT INTO smart_temperature_event (device, eventid, value, unit, occurred) VALUES (?,?,?,?,?)";

    @Override
    public String getHistoryQuery() {
        return HistoryQuery;
    }

    @Override
    public void setHistoryParams(PreparedStatement stmt, int device)
            throws SQLException {
        stmt.setInt(1, device);
        stmt.setString(2, this.getEventId());
        stmt.setDouble(3, this.getValue());
        stmt.setString(4, this.getUnit() + "");
        stmt.setTimestamp(5, this.getTimestamp());
    }

    /**
     * The recent query.
     */
    private static final String RecentQuery =
            "INSERT INTO smart_temperature_recent (device, eventid, value, unit, occurred) "
                    + "VALUES (?,?,?,?,?) "
                    + "ON DUPLICATE KEY UPDATE eventid = VALUES(eventid), "
                    + "value = VALUES(value), unit = VALUES(unit), occurred = VALUES(occurred)";

    @Override
    public String getRecentQuery() {
        return RecentQuery;
    }

    @Override
    public void setRecentParams(PreparedStatement stmt, int device)
            throws SQLException {
        stmt.setInt(1, device);
        stmt.setString(2, this.getEventId());
        stmt.setDouble(3, this.getValue());
        stmt.setString(4, this.getUnit() + "");
        stmt.setTimestamp(5, this.getTimestamp());
    }
}
