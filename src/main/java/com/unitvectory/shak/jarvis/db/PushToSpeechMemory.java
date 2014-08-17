package com.unitvectory.shak.jarvis.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The push to speech in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class PushToSpeechMemory implements PushToSpeechDAO {

    private Map<Integer, List<String>> devices;

    public PushToSpeechMemory() {
        this.devices = new HashMap<Integer, List<String>>();
    }

    public List<String> getPushDeviceIds(int home) {
        List<String> list = this.devices.get(new Integer(home));
        if (list == null) {
            list = new ArrayList<String>();
        }

        return Collections.unmodifiableList(list);
    }

}
