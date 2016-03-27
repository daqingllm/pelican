package com.dianping.pelican.context.proxy;

import com.dianping.pelican.model.Pagelet;

import java.lang.reflect.Method;

/**
 * Created by liming_liu on 15/11/9.
 */
public interface PageletMethodAspectWorkerIterator {

    boolean beforeMethod(Pagelet pagelet, Method method, Object target, Object[] args) throws Exception;

    boolean afterMethod(Pagelet pagelet, Method method, Object target, Object[] args, Throwable throwable) throws Exception;

    PageletMethodAspectWorkerIterator next();

    void setIterator(PageletMethodAspectWorkerIterator iterator);
}
