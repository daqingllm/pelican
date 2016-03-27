package com.dianping.pelican.log.impl;

import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.log.LoggableWorker;
import com.dianping.pelican.model.Pagelet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * Created by liming_liu on 15/11/9.
 */
public class DefaultLoggableWorker implements LoggableWorker {

    private Logger logger = Logger.getLogger(DefaultLoggableWorker.class);

    private ThreadLocal<Long> beginTime = new ThreadLocal<Long>();

    @Override
    public void beforeMethod(Pagelet pagelet, Class<?> clz, Method method, Object target, Object[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(pagelet.getName() + "#");
        if (method != null) {
            sb.append(method.getName() + "(");
        }
        if (args != null && args.length > 0) {
            sb.append(StringUtils.join(args, ", "));
        }
        sb.append(") invoke begin!");

        logger.info(sb.toString());
        beginTime.set(System.currentTimeMillis());
    }

    @Override
    public void afterMethod(Pagelet pagelet, Class<?> clz, Method method, Object target, Object[] args, Throwable throwable) throws Exception {
        long invokedTime = System.currentTimeMillis() - beginTime.get();
        StringBuilder sb = new StringBuilder();
        sb.append(pagelet.getName() + "#");
        if (method != null) {
            sb.append(method.getName() + "(");
        }
        if (args != null && args.length > 0) {
            sb.append(StringUtils.join(args, ", "));
        }
        String endInfo;
        if (throwable != null) {
            endInfo = sb.toString() + ") invoke error!";
            logger.error(endInfo);
        } else {
            endInfo = sb.toString() + ") invoke end!";
            logger.info(endInfo);
        }
        String invokeInfo = sb.toString() + ") last for " + invokedTime + "ms";
        logger.info(invokeInfo);
        if (throwable != null) {
            throw new PelicanRuntimeException("Loggable method invoke error! " + endInfo, throwable);
        }
    }
}
