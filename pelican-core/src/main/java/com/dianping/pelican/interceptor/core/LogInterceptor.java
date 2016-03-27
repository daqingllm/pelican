package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.annotation.Loggable;
import com.dianping.pelican.config.Configuration;
import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.context.proxy.log.LoggablePageletMethodAspectWorkerIterator;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.log.LoggableWorker;
import com.dianping.pelican.log.impl.DefaultLoggableWorker;
import com.dianping.pelican.model.Pagelet;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liming_liu on 15/10/13.
 */
public class LogInterceptor implements Interceptor {

    private static LoggableWorker loggableWorker;

    private static Logger logger = Logger.getLogger(LogInterceptor.class);

    static {
        String loggableWorkerClassName = Configuration.get("loggable.worker", "", String.class);
        if (StringUtils.isNotBlank(loggableWorkerClassName)) {
            try {
                Class<?> clz = Class.forName(loggableWorkerClassName);
                loggableWorker = (LoggableWorker) clz.newInstance();
            } catch (ClassNotFoundException e) {
                logger.error(loggableWorkerClassName + " load failed!", e);
                loggableWorker = new DefaultLoggableWorker();
            } catch (InstantiationException e) {
                logger.error(loggableWorkerClassName + " create instance failed!", e);
                loggableWorker = new DefaultLoggableWorker();
            } catch (IllegalAccessException e) {
                logger.error(loggableWorkerClassName + " create instance failed!", e);
                loggableWorker = new DefaultLoggableWorker();
            }
        } else {
            loggableWorker = new DefaultLoggableWorker();
        }

        Field[] fields = Pagelet.class.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }

        LoggablePageletMethodAspectWorkerIterator workerIterator =
                new LoggablePageletMethodAspectWorkerIterator(loggableWorker);
        ProxyInterceptor.setPageletMethodAspectWorkerIterator(workerIterator);
    }

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        Pagelet pagelet = invocation.getPagelet();
        wrapFields(pagelet);

        return invocation.invoke();
    }

    private void wrapFields(Pagelet pagelet) throws IllegalAccessException {
        Field[] fields = pagelet.getClass().getDeclaredFields();
        for (Field field : fields) {
            Loggable loggable = field.getAnnotation(Loggable.class);
            if (loggable != null) {
                field.setAccessible(true);
                field.set(pagelet, wrapField(pagelet, field.getType(), field.get(pagelet)));
            }
        }
    }

    private Object wrapField(Pagelet pagelet, Class<?> clz, Object value) {
        FieldInvocationHandler handler = new FieldInvocationHandler(pagelet, clz, value);
        return handler.getProxy();
    }

    private class FieldInvocationHandler implements InvocationHandler {

        private Pagelet pagelet;
        private Class<?> clz;
        private Object field;

        public FieldInvocationHandler(Pagelet pagelet, Class<?> clz, Object field) {
            this.pagelet = pagelet;
            this.clz = clz;
            this.field = field;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("toString".equals(method.getName()) || "hashCode".equals(method.getName()) ||
                    "finalize".equals(method.getName())) {
                return method.invoke(field, args);
            }
            Object result = null;
            loggableWorker.beforeMethod(pagelet, clz, method, proxy, args);
            try {
                result = method.invoke(field, args);
                loggableWorker.afterMethod(pagelet, clz, method, proxy, args, null);
            } catch (Exception e) {
                loggableWorker.afterMethod(pagelet, clz, method, proxy, args, e);
            }

            return result;
        }

        public Object getProxy(){
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    field.getClass().getInterfaces(), this);
        }
    }

}
