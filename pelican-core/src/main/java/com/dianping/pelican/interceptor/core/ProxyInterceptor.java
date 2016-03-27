package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.context.proxy.PageletMethodAspectWorkerIterator;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liming_liu on 15/10/14.
 */
public class ProxyInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(ProxyInterceptor.class);

    private static PageletMethodAspectWorkerIterator workerIterator;

    private static Map<String, Class<Pagelet>> proxyCache = new ConcurrentHashMap<String, Class<Pagelet>>();

    public static void setPageletMethodAspectWorkerIterator(PageletMethodAspectWorkerIterator iterator) {
        if (workerIterator == null) {
            workerIterator = iterator;
        } else {
            workerIterator.setIterator(iterator);
        }
    }

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        Pagelet pagelet = invocation.getPagelet();
        if (noNeedToWrap()) {
            return invocation.invoke();
        }
        //before excute
        String proxyKey = pagelet.getClass().getName();
        List<Field> fields = extractPageletFields(pagelet);
        Map<String, Object> fieldValues = extractFieldValues(pagelet, fields);
        Class<Pagelet> proxyClass = proxyCache.get(proxyKey);
        if (proxyClass == null) {
            proxyClass = wrapPagelet(pagelet);
            proxyCache.put(proxyKey, proxyClass);
        }

//        Enhancer.registerCallbacks(proxyClass, new Callback[]{
//                new PageletProxyMethodInterceptor(pagelet)
//        });
        Pagelet proxy = proxyClass.newInstance();
        poluteFields(proxy, fields, fieldValues);

        invocation.setPagelet(proxy);
        PageletInvocation.ResultCode resultCode =  invocation.invoke();

        //after excute
        fieldValues = extractFieldValues(proxy, fields);
        poluteFields(pagelet, fields, fieldValues);
        invocation.setPagelet(pagelet);

        return resultCode;
    }

    private Map<String, Object> extractFieldValues(Pagelet pagelet, List<Field> fields)
            throws IllegalAccessException {
        Map<String, Object> values = new HashMap<String, Object>();
        for (Field field : fields) {
            Object value = field.get(pagelet);
            if (value != null) {
                values.put(field.getName(), value);
            }
        }
        return values;
    }

    private Class<Pagelet> wrapPagelet(final Pagelet pagelet) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(pagelet.getClass());
        enhancer.setCallbackType(PageletProxyMethodInterceptor.class);

        Class<Pagelet> proxyClass = enhancer.createClass();
        Enhancer.registerStaticCallbacks(proxyClass, new Callback[] {
                new PageletProxyMethodInterceptor(pagelet)
        });
        return proxyClass;
    }

    private Object invokeMethod(Pagelet pagelet, PageletMethodAspectWorkerIterator workerIterator,
                                Method method, Object target, Object[] args,
                                MethodProxy methodProxy) throws Throwable {
        PageletMethodAspectWorkerIterator iterator = workerIterator.next();
        Object result = null;
        boolean beforeSuccess = workerIterator.beforeMethod(pagelet, method, target, args);
        if (!beforeSuccess) {
            return null;
        }
        if (iterator != null) {
            try {
                result = invokeMethod(pagelet, iterator, method, target, args, methodProxy);
            } catch (Throwable t) {
                workerIterator.afterMethod(pagelet, method, target, args, t);
                throw t;
            }
            boolean afterSuccess = workerIterator.afterMethod(pagelet, method, target, args, null);
            if (!afterSuccess) {
                return null;
            }
        } else {
            try {
                result = methodProxy.invokeSuper(target, args);
            } catch (Throwable t) {
                workerIterator.afterMethod(pagelet, method, target, args, t);
                throw t;
            }
            boolean afterSuccess = workerIterator.afterMethod(pagelet, method, target, args, null);
            if (!afterSuccess) {
                return null;
            }
        }

        return result;
    }

    private List<Field> extractPageletFields(Pagelet pagelet) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : Pagelet.class.getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
        }
        for (Field field : pagelet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fields.add(field);
        }
        return fields;
    }

    private void poluteFields(Pagelet proxy, List<Field> fields, Map<String, Object> fieldValues)
            throws IllegalAccessException {
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            Object value = fieldValues.get(field.getName());
            if (value != null) {
                field.set(proxy, value);
            }
        }
    }

    private boolean noNeedToWrap() {
        if (workerIterator == null) {
            return true;
        }
        return false;
    }

    private class PageletProxyMethodInterceptor implements MethodInterceptor {

        private Pagelet pagelet;

        public PageletProxyMethodInterceptor(Pagelet pagelet) {
            this.pagelet = pagelet;
        }

        @Override
        public Object intercept(Object target, Method method, Object[] args,
                                MethodProxy methodProxy) throws Throwable {
            if ("toString".equals(method.getName()) || "hashCode".equals(method.getName())) {
                return methodProxy.invokeSuper(target, args);
            }
            return invokeMethod(pagelet, workerIterator, method, target, args, methodProxy);
        }
    }
}
