package com.dianping.pelican.context.proxy.log;

import com.dianping.pelican.annotation.Loggable;
import com.dianping.pelican.context.proxy.PageletMethodAspectWorkerIterator;
import com.dianping.pelican.log.LoggableWorker;
import com.dianping.pelican.model.Pagelet;

import java.lang.reflect.Method;

/**
 * Created by liming_liu on 15/11/9.
 */
public class LoggablePageletMethodAspectWorkerIterator implements PageletMethodAspectWorkerIterator {

    private PageletMethodAspectWorkerIterator workerIterator;

    private LoggableWorker worker;

    public LoggablePageletMethodAspectWorkerIterator(LoggableWorker worker) {
        this.worker = worker;
    }

    @Override
    public boolean beforeMethod(Pagelet pagelet, Method method, Object target, Object[] args) throws Exception {
        Loggable loggable = method.getAnnotation(Loggable.class);
        if (loggable == null) {
            return true;
        }
        worker.beforeMethod(pagelet, null, method, target, args);
        return true;
    }

    @Override
    public boolean afterMethod(Pagelet pagelet, Method method, Object target, Object[] args, Throwable throwable) throws Exception {
        Loggable loggable = method.getAnnotation(Loggable.class);
        if (loggable == null) {
            return true;
        }
        worker.afterMethod(pagelet, null, method, target, args, throwable);
        return true;
    }

    @Override
    public PageletMethodAspectWorkerIterator next() {
        return null;
    }

    @Override
    public void setIterator(PageletMethodAspectWorkerIterator iterator) {
        this.workerIterator = iterator;
    }
}
