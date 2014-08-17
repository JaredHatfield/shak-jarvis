package com.unitvectory.shak.jarvis.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unitvectory.shak.jarvis.db.model.PersonLocationDetails;
import com.unitvectory.shak.jarvis.model.PersonLocationPublish;

/**
 * The person location in memory database.
 * 
 * @author Jared Hatfield
 *
 */
public class PersonLocationMemory implements PersonLocationDAO {

    private Map<String, PersonLocationDetails> people;

    private List<PersonLocationPublish> locations;

    public PersonLocationMemory() {
        this.people = new HashMap<String, PersonLocationDetails>();
        this.locations = new ArrayList<PersonLocationPublish>();
    }

    public PersonLocationDetails getPerson(String token) {
        return this.people.get(token);
    }

    public InsertResult insertLocation(PersonLocationPublish publish) {
        // TODO: This is a horrible analog to the necessary data structure for
        // this interface
        this.locations.add(publish);
        return InsertResult.Success;
    }

}
