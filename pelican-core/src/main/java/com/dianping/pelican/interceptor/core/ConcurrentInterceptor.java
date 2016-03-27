package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.concurrent.Executor;
import com.dianping.pelican.concurrent.ThreadPool;
import com.dianping.pelican.config.Configuration;
import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import com.dianping.pelican.util.PageletNameBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by liming_liu on 15/7/4.
 */
public class ConcurrentInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(ConcurrentInterceptor.class);

    private final ThreadPool threadPool = Executor.getInstance();

    private final int timeout = Configuration.get("concurrent.timeout", 1000, Integer.class);

    /**
     * 定义子模块渲染结果key
     */
    private final String SUB_PAGELET_OUTPUT_KEY = "TASK";

    @Override
    public PageletInvocation.ResultCode intercept(final PageletInvocation invocation) throws Exception {
        invocation.getContext().put(PageletInvocation.TASKS_KEY, new HashMap<String, String>());

        PageletInvocation.ResultCode resultCode = invocation.invoke();
        if (!PageletInvocation.SUCCESS.equals(resultCode)) {
            return resultCode;
        }
        Pagelet pagelet = invocation.getPagelet();
        if (CollectionUtils.isEmpty(pagelet.getSubPagelets())) {
            return resultCode;
        }

        //暂时不准备支持pipe
        Map<ConcurrentKey, Future<String>> barriers = new HashMap<ConcurrentKey, Future<String>>();
        for (final Pagelet subPagelet : pagelet.getSubPagelets()) {
            ConcurrentKey key = new ConcurrentKey();
            if (StringUtils.isNotBlank(subPagelet.getName())) {
                key.pageletName = subPagelet.getName();
            } else  {
                key.pageletName = PageletNameBuilder.buildName(subPagelet.getClass());
            }
            if (subPagelet.getTimeoutMilliseconds() > 0) {
                key.timeout = subPagelet.getTimeoutMilliseconds();
            } else {
                key.timeout = timeout;
            }
            barriers.put(key, threadPool.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return subPagelet.render((Map<String, Object>) invocation.getContext()
                                    .get(PageletInvocation.INPUT_KEY),
                            invocation.getRequest(), invocation.getResponse());
                }
            }));
        }
        for (ConcurrentKey key : barriers.keySet()) {
            Future<String> pageletTask = barriers.get(key);
            try {
                String pageletResult = pageletTask.get(key.timeout, TimeUnit.MILLISECONDS);
                ((Map<String, String>) invocation.getContext().get(PageletInvocation.TASKS_KEY))
                        .put(key.pageletName, pageletResult);
            } catch (InterruptedException e) {
                logger.error("Pagelet thread is interrupted: " + pagelet.getName(), e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                logger.error("Pagelet render error: " + key.pageletName, e);
            } catch (TimeoutException e) {
                logger.error("Pagelet timeout: " + key.pageletName, e);
            }
        }
        ((Map<String, Object>)invocation.getContext().get(PageletInvocation.OUTPUT_KEY))
                .put(SUB_PAGELET_OUTPUT_KEY, invocation.getContext().get(PageletInvocation.TASKS_KEY));

        return PageletInvocation.ResultCode.SUCCESS;
    }

    private class ConcurrentKey {
        public String pageletName;
        public int timeout;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (!(o instanceof ConcurrentKey)) return false;

            ConcurrentKey that = (ConcurrentKey) o;

            return new EqualsBuilder()
                    .append(timeout, that.timeout)
                    .append(pageletName, that.pageletName)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(pageletName)
                    .append(timeout)
                    .toHashCode();
        }
    }
}
