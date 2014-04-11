package com.unitvectory.shak.jarvis.action;

import java.util.ArrayList;
import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.model.SmartThingsDeviceDetails;
import com.unitvectory.shak.jarvis.model.smartthings.SmartContact;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart contact action
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartContactAction extends SmartAction {

    /**
     * Creates a new instance of the SmartContactAction class.
     * 
     */
    public SmartContactAction() {
    }

    @Override
    public List<ActionNotification> getActions(DatabaseEventCache cache,
            SmartEvent event) {
        List<ActionNotification> notifications =
                new ArrayList<ActionNotification>();

        if (!(event instanceof SmartContact)) {
            return notifications;
        }

        SmartContact contact = (SmartContact) event;

        SmartThingsDeviceDetails details = cache.getDeviceDetails(event);

        if (details == null) {
            return notifications;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(details.getName());
        sb.append(" is ");
        if (contact.getStatus() == 'O') {
            sb.append("open... ");
        } else if (contact.getStatus() == 'C') {
            sb.append("closed... ");
        } else {
            sb.append("unknown... ");
        }

        notifications.add(new ActionNotification("CONTACT", sb.toString(),
                true, details.getHome()));

        return notifications;
    }
}
