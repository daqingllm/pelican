package com.dianping.pelican.context.http;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liming_liu on 15/9/17.
 */
public interface HttpRequestAware {

    void setHttpRequest(HttpServletRequest request);
}
