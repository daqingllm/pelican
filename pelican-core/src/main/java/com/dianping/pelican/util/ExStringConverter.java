package com.dianping.pelican.util;

import org.apache.commons.beanutils.converters.AbstractConverter;

/**
 * Created by liming_liu on 15/7/4.
 */
public class ExStringConverter extends AbstractConverter {
    @Override
    protected Object convertToType(Class type, Object value) throws Throwable {
        return value.toString();
    }

    @Override
    protected Class getDefaultType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return null;
        }
        return super.convert(type, value);
    }
}
