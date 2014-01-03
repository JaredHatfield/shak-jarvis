package com.unitvectory.shak.jarvis.db;

/**
 * THe insert result.
 * 
 * @author Jared Hatfield
 * 
 */
public enum InsertResult {

    /**
     * The insert was successful.
     */
    Success,

    /**
     * There was an error.
     */
    Error,

    /**
     * The record was duplicated.
     */
    Duplicate
}
