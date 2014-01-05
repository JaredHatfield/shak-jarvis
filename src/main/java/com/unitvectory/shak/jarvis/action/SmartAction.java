package com.unitvectory.shak.jarvis.action;

import java.util.List;

import com.unitvectory.shak.jarvis.db.DatabaseEventCache;
import com.unitvectory.shak.jarvis.db.SmartThingsDAO;
import com.unitvectory.shak.jarvis.model.smartthings.SmartEvent;

/**
 * The smart action
 * 
 * @author Jared Hatfield
 * 
 */
public abstract class SmartAction {

    /**
     * the smart things dao
     */
    private SmartThingsDAO st;

    /**
     * Creates a new instance of the SmartAction class.
     * 
     * @param st
     *            the smart things dao
     */
    public SmartAction(SmartThingsDAO st) {
        this.st = st;
    }

    /**
     * Gets the smart things dao
     * 
     * @return the smart things dao
     */
    protected SmartThingsDAO getSmartThingsDAO() {
        return this.st;
    }

    /**
     * Gets the list of actions for an event.
     * 
     * @param cache
     *            the cache
     * @param event
     *            the event
     * @return the list of actions
     */
    public abstract List<ActionNotification> getActions(
            DatabaseEventCache cache, SmartEvent event);
}
