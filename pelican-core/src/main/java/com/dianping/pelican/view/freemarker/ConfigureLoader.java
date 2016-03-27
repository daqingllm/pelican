package com.dianping.pelican.view.freemarker;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;

/**
 * Created by liming_liu on 15/7/13.
 */
public class ConfigureLoader {

    private Configuration configuration;

    public ConfigureLoader() {
        configuration = new Configuration();
        configuration.setTemplateUpdateDelay(600000);
        configuration.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
        configuration.setNumberFormat("#");
        configuration.setClassicCompatible(true);
        configuration.setDefaultEncoding("UTF-8");
    }

    public Configuration load() {
        return configuration;
    }
}
