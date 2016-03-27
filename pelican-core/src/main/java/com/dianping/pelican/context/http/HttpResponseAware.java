package com.dianping.pelican.context.http;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liming_liu on 15/9/17.
 */
public interface HttpResponseAware {

    void setHttpResponse(HttpServletResponse response);
}
