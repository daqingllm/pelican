package com.dianping.pelican.util;

import com.dianping.pelican.exception.PelicanRuntimeException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ConverterFacade;

import java.util.Map;

/**
 * Created by liming_liu on 15/7/4.
 */
public class BeanPopulatingUtils {

    static {
        BeanUtilsBean.getInstance().getConvertUtils().register(new ConverterFacade(new ExStringConverter()), String.class);
    }

    /**
     * <p>
     * Populate the JavaBeans properties of the specified bean, based on the
     * specified name/value pairs.
     * </p>
     *
     * @param bean
     *            JavaBean whose properties are being populated
     * @param map
     *            Map keyed by property name, with the corresponding (String or
     *            String[]) value(s) to be set
     */
    public static void populateBeanWithMap(Object bean, Map map) {
        try {
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            throw new PelicanRuntimeException("Beans populate error! ", e);
        }
    }

    /**
     * <p>
     * Return the entire set of properties for which the specified bean provides
     * a read method.
     * </p>
     *
     * @param bean
     *            bean whose properties are to be extracted
     * @return The set of properties for the bean
     */
    public static Map extractBeanToMap(Object bean) {
        try {
            return PropertyUtils.describe(bean);
        } catch (Exception e) {
            throw new PelicanRuntimeException("Beans extract error!", e);
        }
    }
}
