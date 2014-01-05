package com.unitvectory.shak.jarvis.action;

/**
 * The action notificationJ
 * 
 * @author Jared Hatfield
 * 
 */
public class ActionNotification {

    /**
     * the event
     */
    private String event;

    /**
     * the notification
     */
    private String notification;

    /**
     * Creates a new instance of the ActionNotification class.
     * 
     * @param event
     *            the event
     * @param notification
     *            the notification
     */
    public ActionNotification(String event, String notification) {
        this.event = event;
        this.notification = notification;
    }

    /**
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * @return the notification
     */
    public String getNotification() {
        return notification;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Event " + this.event + ": " + this.notification;
    }

}
