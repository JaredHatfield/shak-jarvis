package com.unitvectory.shak.jarvis;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.unitvectory.shak.jarvis.action.ActionNotification;
import com.unitvectory.shak.jarvis.action.SmartContactAction;
import com.unitvectory.shak.jarvis.action.SmartMotionAction;
import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.InsertResult;
import com.unitvectory.shak.jarvis.db.SmartThingsDAO;
import com.unitvectory.shak.jarvis.exception.SmartException;
import com.unitvectory.shak.jarvis.model.JsonPublishRequest;
import com.unitvectory.shak.jarvis.model.SmartThingsPublish;
import com.unitvectory.shak.jarvis.model.smartthings.SmartContact;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;
import com.unitvectory.shak.jarvis.model.smartthings.SmartMotion;

/**
 * The home event processor
 * 
 * @author Jared Hatfield
 * 
 */
public class HomeEventProcessor {

    /**
     * the log
     */
    private static Logger log = Logger.getLogger(HomeEventProcessor.class);

    /**
     * the smart things DAO
     */
    private SmartThingsDAO st;

    /**
     * the smart contact action
     */
    private SmartContactAction contactAction;

    /**
     * the smart motion action
     */
    private SmartMotionAction motionAction;

    /**
     * Creates a new instance of the HomeEventProcessor class.
     * 
     * @param st
     *            the smart event DAO
     */
    public HomeEventProcessor(SmartThingsDAO st) {
        this.st = st;
        this.contactAction = new SmartContactAction(st);
        this.motionAction = new SmartMotionAction(st);
    }

    /**
     * process a smart event
     * 
     * @param request
     *            the request
     * @return true if successful; otherwise false
     */
    public boolean processEvent(JsonPublishRequest request) {
        if (!request.isValid()) {
            return false;
        }

        String type = request.getData().get("type");
        if (type == null) {
            return false;
        }

        List<ActionNotification> notifications =
                new ArrayList<ActionNotification>();

        if (type.equals("smartthings")) {
            List<ActionNotification> smartNotifications =
                    this.processSmartEvent(request);
            if (smartNotifications == null) {
                return false;
            }

            this.append(notifications, smartNotifications);
        }

        // Lots of events
        for (ActionNotification notification : notifications) {
            log.info(notification);
        }

        return true;
    }

    /**
     * Get the list of events given a smart event.
     * 
     * @param request
     *            the request
     * @return the list of notifications
     */
    private List<ActionNotification> processSmartEvent(
            JsonPublishRequest request) {
        SmartThingsPublish smart = new SmartThingsPublish(request);
        SmartEvent event = null;
        try {
            event = smart.buildSmartEvent();
        } catch (SmartException e) {
            log.error("Failed to parse SmartPublish request", e);
            return null;
        }

        // Insert the event into the database
        InsertResult insertResult = this.st.insertSmartEvent(event);
        log.info(insertResult + " - " + event.getName());
        switch (insertResult) {
            case Duplicate:
                // A duplicate event needs no more processing
                return null;
            case Error:
                // An error should not be processed
                return null;
            case Success:
                // Just keep going...
                break;
            default:
                break;
        }

        // The cache
        DatabaseEventCache cache = new DatabaseEventCache(this.st);

        // Get the list of actions
        List<ActionNotification> notifications =
                new ArrayList<ActionNotification>();
        if (event instanceof SmartMotion) {
            this.append(notifications,
                    this.motionAction.getActions(cache, event));
        } else if (event instanceof SmartContact) {
            this.append(notifications,
                    this.contactAction.getActions(cache, event));
        }

        return notifications;
    }

    /**
     * Appends one array to another.
     * 
     * @param master
     * @param children
     */
    private void append(List<ActionNotification> master,
            List<ActionNotification> children) {
        for (ActionNotification child : children) {
            master.add(child);
        }
    }
}
