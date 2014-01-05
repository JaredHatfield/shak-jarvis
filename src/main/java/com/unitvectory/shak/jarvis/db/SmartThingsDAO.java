package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

@SuppressWarnings("javadoc")
public class SmartThingsDAO {

    private static Logger log = Logger.getLogger(SmartThingsDAO.class);

    private BasicDataSource ds;

    public SmartThingsDAO(BasicDataSource ds) {
        this.ds = ds;
    }

    public InsertResult insertSmartEvent(SmartEvent event) {
        int device =
                this.getSmartEventDevicePid(event.getHubId(),
                        event.getLocationId(), event.getDeviceId());
        if (device == -1) {
            return InsertResult.Error;
        }

        InsertResult historyResult =
                this.insertSmartEventHistory(event, device);
        switch (historyResult) {
            case Duplicate:
                return InsertResult.Duplicate;
            case Error:
                return InsertResult.Error;
            case Success:
                return this.insertSmartEventRecent(event, device);
            default:
                return InsertResult.Error;
        }
    }

    private InsertResult insertSmartEventRecent(SmartEvent event, int device) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            String query = event.getRecentQuery();
            if (query == null) {
                return InsertResult.Success;
            }

            stmt = con.prepareStatement(query);
            event.setRecentParams(stmt, device);
            stmt.execute();
            return InsertResult.Success;
        } catch (SQLException e) {
            log.error("Unable to insert SmartEventRecent", e);
            return InsertResult.Error;
        } finally {
            this.closeEverything(con, stmt, rs);
        }
    }

    private InsertResult insertSmartEventHistory(SmartEvent event, int device) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            String query = event.getHistoryQuery();
            if (query == null) {
                return InsertResult.Success;
            }

            stmt = con.prepareStatement(query);
            event.setHistoryParams(stmt, device);
            stmt.execute();
            return InsertResult.Success;
        } catch (MySQLIntegrityConstraintViolationException e) {
            return InsertResult.Duplicate;
        } catch (SQLException e) {
            log.error("Unable to insert SmartEventHistory", e);
            return InsertResult.Error;
        } finally {
            this.closeEverything(con, stmt, rs);
        }
    }

    private final static String GetSmartEventDevicePidQuery =
            "SELECT pid FROM smart_device WHERE hubid = ? AND locationid = ? AND deviceid = ? LIMIT 1";

    private int getSmartEventDevicePid(String hubId, String locationId,
            String deviceId) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt = con.prepareStatement(GetSmartEventDevicePidQuery);
            stmt.setString(1, hubId);
            stmt.setString(2, locationId);
            stmt.setString(3, deviceId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int pid = rs.getInt("pid");
                return pid;
            }
        } catch (SQLException e) {
            log.error("Unable to get SmartEventDevicePid", e);
            return -1;
        } finally {
            this.closeEverything(con, stmt, rs);
        }

        return this.insertSmartEventDevice(hubId, locationId, deviceId);
    }

    private final static String InsertSmartEventDeviceQuery =
            "INSERT INTO smart_device (hubid, locationid, deviceid, added) VALUES (?,?,?,NOW())";

    private int insertSmartEventDevice(String hubId, String locationId,
            String deviceId) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ds.getConnection();
            stmt =
                    con.prepareStatement(InsertSmartEventDeviceQuery,
                            Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, hubId);
            stmt.setString(2, locationId);
            stmt.setString(3, deviceId);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                int pid = rs.getInt(1);
                return pid;
            }

            return -1;
        } catch (SQLException e) {
            log.error("Unable to insert SmartEventDevice", e);
            return -1;
        } finally {
            this.closeEverything(con, stmt, rs);
        }
    }

    private void closeEverything(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
    }
}
