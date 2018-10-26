package com.lsaippa.movies.utilities;


import com.lsaippa.movies.BuildConfig;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.Properties;

public class Configuration extends Properties{

    private static Configuration instance = null;
    public String getProperty(final String key) {
        final String property = BuildConfig.API_KEY;
        if(property == null) {
            throw new InvalidParameterException(MessageFormat.format("Missing value for key {0}!", key));
        }
        return property;
    }

    public static Configuration getInstance() {
        if(instance == null ){
            instance = new Configuration();
        }
        return instance;
    }


}
