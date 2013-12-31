package com.unitvectory.shak.jarvis.exception;

/**
 * The exception for a SmartThings problem
 * 
 * @author Jared Hatfield
 * 
 */
public class SmartException extends Exception {

    /**
     * The serial version UID
     */
    private static final long serialVersionUID = -7287813833566889922L;

    /**
     * Constructs a new exception with null as its detail message.
     */
    public SmartException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     *            the detail message.
     */
    public SmartException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message
     *            the detail message.
     * @param cause
     *            the cause
     */
    public SmartException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the
     * class and detail message of cause).
     * 
     * @param cause
     *            the cause
     */
    public SmartException(Throwable cause) {
        super(cause);
    }
}
