package com.dianping.pelican.context;

import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/2.
 */
public class PageletInvocation {

    public static final ResultCode SUCCESS = ResultCode.SUCCESS;

    public static final ResultCode NONE = ResultCode.NONE;

    public static final ResultCode ERROR = ResultCode.ERROR;

    public static final String INPUT_KEY = "input_params";

    public static final String OUTPUT_KEY = "output_params";

    public static final String TASKS_KEY = "concurrent_tasks";

    private Pagelet pagelet;

    private final Map<String,Object> context;

    private ResultCode resultCode = SUCCESS;

    private final Iterator<Interceptor> interceptors;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String pageletResult;

    private Throwable exception;

    public PageletInvocation(Pagelet pagelet, Map<String, Object> params, Iterator<Interceptor> interceptors,
                             HttpServletRequest request, HttpServletResponse response) {
        this.pagelet = pagelet;
        this.context = new HashMap<String, Object>();
        this.context.put(INPUT_KEY, params);
        this.interceptors = interceptors;
        this.request = request;
        this.response = response;
    }

    public ResultCode invoke() throws Exception{
        if(interceptors.hasNext()){
            final Interceptor interceptor=interceptors.next();
            resultCode=interceptor.intercept(this);
        }
        return resultCode;
    }

    public Pagelet getPagelet() {
        return pagelet;
    }

    public void setPagelet(Pagelet pagelet) {
        this.pagelet = pagelet;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getPageletResult() {
        return pageletResult;
    }

    public void setPageletResult(String pageletResult) {
        this.pageletResult = pageletResult;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public enum ResultCode {
        SUCCESS,NONE,ERROR
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
