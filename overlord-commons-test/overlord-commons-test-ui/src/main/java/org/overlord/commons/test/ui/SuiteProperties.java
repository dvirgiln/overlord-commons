package org.overlord.commons.test.ui;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class SuiteProperties {

    private static Configuration configuration;

    private final static String DEFAULT_PROPERTIES = "test.properties";

    public static Object getProperty(String key) throws ConfigurationException {
        if (configuration == null) {
            initialize(DEFAULT_PROPERTIES);
        }
        return configuration.getProperty(key);
    }

    public static void initialize(String file_name) throws ConfigurationException {
        if (configuration == null)
            configuration = new PropertiesConfiguration(file_name);
    }

}
