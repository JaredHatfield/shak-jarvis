package com.unitvectory.shak.jarvis.db;

import java.util.List;

/**
 * The push to speech DAO
 * 
 * @author Jared Hatfield
 * 
 */
public interface PushToSpeechDAO {

    /**
     * Gets the list of device ids.
     * 
     * @param home
     *            the home id
     * @return the device ids.
     */
    List<String> getPushDeviceIds(int home);
}
