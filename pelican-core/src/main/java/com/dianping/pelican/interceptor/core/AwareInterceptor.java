package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.context.SubPageletInputAware;
import com.dianping.pelican.context.http.HttpRequestAware;
import com.dianping.pelican.context.http.HttpResponseAware;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by liming_liu on 15/9/17.
 */
public class AwareInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(AwareInterceptor.class);

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        Pagelet pagelet = invocation.getPagelet();

        if (pagelet instanceof HttpRequestAware) {
            ((HttpRequestAware) pagelet).setHttpRequest(invocation.getRequest());
        }
        if (pagelet instanceof HttpResponseAware) {
            ((HttpResponseAware) pagelet).setHttpResponse(invocation.getResponse());
        }

        PageletInvocation.ResultCode resultCode = invocation.invoke();

        if (pagelet instanceof SubPageletInputAware) {
            ((SubPageletInputAware) pagelet).setInputParamsForSubPagelet(
                    (Map<String, Object>) invocation.getContext().get(PageletInvocation.INPUT_KEY)
            );
        }

        return resultCode;
    }
}
