package com.dianping.pelican.renderer;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.config.Configuration;
import com.dianping.pelican.config.InterceptorConfig;
import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import com.dianping.pelican.util.PageletNameBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/2.
 */
public abstract class PageletRenderer {

    private static Logger logger = Logger.getLogger(PageletRenderer.class);

    public static String render(Pagelet pagelet, Map<String, Object> params,
                         HttpServletRequest request, HttpServletResponse response) {

        PageletInvocation invocation = null;
        PageletInvocation.ResultCode resultCode = null;
        try {
            Config config = pagelet.getClass().getAnnotation(Config.class);
            if (StringUtils.isEmpty(pagelet.getName())) {
                pagelet.setName(PageletNameBuilder.buildName(pagelet.getClass()));
            }
            if (StringUtils.isEmpty(pagelet.getView()) && config != null) {
                String value = config.value();
                String view = config.view();
                if (StringUtils.isNotBlank(view)) {
                    pagelet.setView(view);
                } else if (StringUtils.isNotBlank(value)) {
                    pagelet.setView(value);
                }
            }
            Iterator<Interceptor> interceptors = InterceptorConfig.getInstance().getInterceptors(pagelet.getStack());
            invocation = new PageletInvocation(pagelet, params, interceptors, request, response);
            resultCode = invocation.invoke();
        } catch (Exception e) {
            logger.error(invocation.getPagelet().getName() + " render error!", e);
            if ("print".equals(Configuration.get("exception", "log", String.class))) {
                return ExceptionUtils.getFullStackTrace(e);
            } else {
                return "";
            }
        }

        if (PageletInvocation.SUCCESS.equals(resultCode)) {
            return invocation.getPageletResult();
        } else if (PageletInvocation.NONE.equals(resultCode)) {
            return "";
        } else if (PageletInvocation.ERROR.equals(resultCode)) {
            if ("print".equals(Configuration.get("exception", "log", String.class))) {
                return ExceptionUtils.getFullStackTrace(invocation.getException());
            } else {
                return "";
            }
        } else {
            throw new PelicanRuntimeException("Unknown return code!");
        }
    }
}
