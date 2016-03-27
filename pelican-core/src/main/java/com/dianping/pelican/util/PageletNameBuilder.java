package com.dianping.pelican.util;

import com.dianping.pelican.annotation.Config;
import org.apache.commons.lang.StringUtils;

/**
 * Created by liming_liu on 15/7/4.
 */
public abstract class PageletNameBuilder {

    public static String buildName(Class pageletClass) {
        Config config = (Config) pageletClass.getAnnotation(Config.class);
        if (config != null && StringUtils.isNotBlank(config.name())) {
            return config.name();
        }
        StringBuilder sb = new StringBuilder();
        String className = pageletClass.getSimpleName();
        sb.append(Character.toLowerCase(className.charAt(0))).append(className.substring(1));
        return sb.toString();
    }
}
