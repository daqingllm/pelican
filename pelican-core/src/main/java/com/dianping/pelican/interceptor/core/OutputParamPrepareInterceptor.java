package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: liming_liu
 * Date: 15/9/10
 * Time: 下午5:58
 * To change this template use File | Settings | File Templates.
 */
public class OutputParamPrepareInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(OutputParamPrepareInterceptor.class);

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        PageletInvocation.ResultCode resultCode = invocation.invoke();
        if (!PageletInvocation.SUCCESS.equals(resultCode)) {
            return resultCode;
        }

        prepareOutputParams(invocation);
        return resultCode;
    }

    private void prepareOutputParams(PageletInvocation invocation) {
        Map<String, Object> outputParams = (Map<String, Object>) invocation.getContext().get(PageletInvocation.OUTPUT_KEY);
        Map<String, Object> clonedParams = new HashMap<String, Object>();
        for (String paramKey : outputParams.keySet()) {
            char paramChars[] = paramKey.toCharArray();
            if (Character.isUpperCase(paramChars[0])) {
                paramChars[0] = Character.toLowerCase(paramChars[0]);
            } else if (Character.isLowerCase(paramChars[0])) {
                paramChars[0] = Character.toUpperCase(paramChars[0]);
            }
            String clonedParamKey = new String(paramChars);
            Object value = outputParams.get(paramKey);
            clonedParams.put(clonedParamKey, value);
        }
        outputParams.putAll(clonedParams);
    }
}
