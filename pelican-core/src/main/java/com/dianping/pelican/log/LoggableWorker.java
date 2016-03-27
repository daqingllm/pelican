package com.dianping.pelican.log;

import com.dianping.pelican.model.Pagelet;

import java.lang.reflect.Method;

/**
 * Created by liming_liu on 15/11/9.
 */
public interface LoggableWorker {

    void beforeMethod(Pagelet pagelet, Class<?> clz, Method method, Object target, Object[] args) throws Exception;

    void afterMethod(Pagelet pagelet, Class<?> clz, Method method, Object target, Object[] args, Throwable throwable) throws Exception;

}
