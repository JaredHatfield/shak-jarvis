package com.unitvectory.shak.jarvis;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * The app configuration.
 * 
 * @author Jared Hatfield
 * 
 */
@Root(name = "config")
public class AppConfig {

    /**
     * the aws access key
     */
    @Element(name = "awsaccesskey")
    private String awsAccessKey;

    /**
     * the aws secret key
     */
    @Element(name = "awssecretkey")
    private String awsSecretKey;

    /**
     * the queue url
     */
    @Element(name = "queueurl")
    private String queueUrl;

    /**
     * the database user
     */
    @Element(name = "dbuser")
    private String dbUser;

    /**
     * the database password
     */
    @Element(name = "dbpassword")
    private String dbPassword;

    /**
     * the database url
     */
    @Element(name = "dburl")
    private String dbUrl;

    /**
     * Creates a new instance of the AppConfig class.
     */
    public AppConfig() {
    }

    /**
     * @return the awsAccessKey
     */
    public String getAwsAccessKey() {
        return awsAccessKey;
    }

    /**
     * @return the awsSecretKey
     */
    public String getAwsSecretKey() {
        return awsSecretKey;
    }

    /**
     * @return the queueUrl
     */
    public String getQueueUrl() {
        return queueUrl;
    }

    /**
     * @return the dbUser
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @return the dbPassword
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * @return the dbUrl
     */
    public String getDbUrl() {
        return dbUrl;
    }
}
