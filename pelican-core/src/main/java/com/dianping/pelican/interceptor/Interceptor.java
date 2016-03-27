package com.dianping.pelican.interceptor;

import com.dianping.pelican.context.PageletInvocation;

/**
 * Created by liming_liu on 15/7/2.
 */
public interface Interceptor {

    PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception;
}
