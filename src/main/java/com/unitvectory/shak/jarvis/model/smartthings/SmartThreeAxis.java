package com.unitvectory.shak.jarvis.model.smartthings;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    /**
     * The history query.
     */
    private static final String HistoryQuery =
            "INSERT INTO smart_threeaxis_event (device, eventid, valX, valY, valZ, occurred) VALUES (?,?,?,?,?,?)";

    @Override
    public String getHistoryQuery() {
        return HistoryQuery;
    }

    @Override
    public void setHistoryParams(PreparedStatement stmt, int device)
            throws SQLException {
        stmt.setInt(1, device);
        stmt.setString(2, this.getEventId());
        stmt.setDouble(3, this.getValX());
        stmt.setDouble(4, this.getValY());
        stmt.setDouble(5, this.getValZ());
        stmt.setTimestamp(6, this.getTimestamp());
    }

    /**
     * The recent query.
     */
    private static final String RecentQuery =
            "INSERT INTO smart_threeaxis_recent (device, eventid, valX, valY, valZ, occurred) "
                    + "VALUES (?,?,?,?,?,?) "
                    + "ON DUPLICATE KEY UPDATE eventid = VALUES(eventid), "
                    + "valX = VALUES(valX), valY = VALUES(valY), "
                    + "valZ = VALUES(valZ), occurred = VALUES(occurred)";

    @Override
    public String getRecentQuery() {
        return RecentQuery;
    }

    @Override
    public void setRecentParams(PreparedStatement stmt, int device)
            throws SQLException {
        stmt.setInt(1, device);
        stmt.setString(2, this.getEventId());
        stmt.setDouble(3, this.getValX());
        stmt.setDouble(4, this.getValY());
        stmt.setDouble(5, this.getValZ());
        stmt.setTimestamp(6, this.getTimestamp());
    }
}
