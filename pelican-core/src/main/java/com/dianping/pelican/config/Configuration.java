package com.dianping.pelican.config;

import java.io.InputStream;

/**
 * Created by liming_liu on 15/7/2.
 */
public abstract class Configuration {
    private static final Yaml yaml;

    static{
        InputStream input = Configuration.class.getResourceAsStream("/pelican.yaml");
        if (input == null) {
            yaml = null;
        } else {
            yaml = new Yaml(input);
        }
    }

    public static <T> T get(String expression,Class<T> clazz){
        if (yaml == null) {
            return null;
        } else {
            return yaml.get(expression, clazz);
        }
    }

    public static <T> T get(String expression, T defaultValue, Class<T> clazz){
        if (yaml == null) {
            return defaultValue;
        } else {
            return yaml.get(expression, defaultValue, clazz);
        }
    }
}
