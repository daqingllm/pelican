package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.listener.PelicanServletContextListener;
import com.dianping.pelican.model.Pagelet;
import com.dianping.pelican.view.ViewEngine;
import com.dianping.pelican.view.ViewEngineFactory;
import com.dianping.pelican.view.freemarker.JSPSupport;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created by liming_liu on 15/7/4.
 */
public class ViewInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(ViewInterceptor.class);

    private ViewEngine viewEngine = ViewEngineFactory.getViewEngine();

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        PageletInvocation.ResultCode resultCode = invocation.invoke();
        if (!PageletInvocation.SUCCESS.equals(resultCode)) {
            return resultCode;
        }

        Pagelet pagelet = invocation.getPagelet();
        Map<String, Object> outputParams = (Map<String, Object>) invocation.getContext().get(PageletInvocation.OUTPUT_KEY);
        outputParams.putAll(JSPSupport.getSupport(invocation.getRequest(), PelicanServletContextListener.getServletContext()));
        String viewResult = viewEngine.render(pagelet.getView(), outputParams);
        invocation.setPageletResult(viewResult);

        return PageletInvocation.ResultCode.SUCCESS;
    }
}
