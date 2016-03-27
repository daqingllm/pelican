package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.annotation.Future;
import com.dianping.pelican.annotation.FutureImpl;
import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liming_liu on 15/11/12.
 */
public class FutureInterceptor implements Interceptor {

    private static Logger logger = Logger.getLogger(FutureInterceptor.class);

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        Pagelet pagelet = invocation.getPagelet();
        List<Field> futureFields = extractFutureFields(pagelet);
        PageletInvocation.ResultCode resultCode = invocation.invoke();
        if (CollectionUtils.isEmpty(futureFields)) {
            return resultCode;
        }

        Map<String, Object> futureOutputParams = extractFutureFields(pagelet, futureFields);
        ((Map) invocation.getContext().get(PageletInvocation.OUTPUT_KEY)).putAll(futureOutputParams);

        return resultCode;
    }

    private List<Field> extractFutureFields(Pagelet pagelet) {
        List<Field> result = new ArrayList<Field>();
        Field[] fields = pagelet.getClass().getDeclaredFields();
        for (Field field : fields) {
            Future future = field.getAnnotation(Future.class);
            if (future != null) {
                result.add(field);
            }
        }

        return result;
    }

    private Map<String, Object> extractFutureFields(Pagelet pagelet, List<Field> futureFields) {
        Map<String, Object> result = new HashMap<String, Object>();

        List<Pagelet> subPagelets = pagelet.getSubPagelets();
        if (CollectionUtils.isEmpty(subPagelets)) {
            return result;
        }
        for (Field field : futureFields) {
            Object value = findImplemention(field, subPagelets);
            if (value == null) {
                logger.error("Pagelet " + pagelet.getName() +
                        ": future field " + field.getName() + " has not been initialized!");
            }
            result.put(field.getName(), value);
        }
        return result;
    }

    private Object findImplemention(Field field, List<Pagelet> subPagelets) {
        if (CollectionUtils.isEmpty(subPagelets)) {
            return null;
        }
        for (Pagelet pagelet : subPagelets) {
            Field pageletField = null;
            Object value = null;
            try {
                pageletField = pagelet.getClass().getDeclaredField(field.getName());
                if (pageletField != null && pageletField.getAnnotation(FutureImpl.class) != null) {
                    pageletField.setAccessible(true);
                    value =  pageletField.get(pagelet);
                    if (value != null) {
                        return value;
                    }
                }
            } catch (Exception e) {
            }
            value = findImplemention(field, pagelet.getSubPagelets());
            if (value != null) {
                return value;
            }
        }
        return null;
    }
}
