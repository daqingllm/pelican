package com.dianping.pelican.config;

import com.dianping.pelican.exception.PelicanInitializationException;
import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.interceptor.core.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Created by liming_liu on 15/7/3.
 */
public class InterceptorConfig {

    private final Map<String, List<Interceptor>> stackConfig = new HashMap<String, List<Interceptor>>();

    private final Map<String, Interceptor> interceptorMap = new HashMap<String, Interceptor>();

    private static InterceptorConfig instance = new InterceptorConfig();

    private InterceptorConfig() {
        interceptorMap.put("business", new BusinessInterceptor());
        interceptorMap.put("aware", new AwareInterceptor());
        interceptorMap.put("concurrent", new ConcurrentInterceptor());
        interceptorMap.put("future", new FutureInterceptor());
        interceptorMap.put("output_param", new OutputParamPrepareInterceptor());
        interceptorMap.put("view", new ViewInterceptor());
        interceptorMap.put("log", new LogInterceptor());
        interceptorMap.put("proxy", new ProxyInterceptor());
        initUserDefinedInterceptors();
        initDefaultStack();
        initUserDefinedStack();
    }

    private void initUserDefinedInterceptors() {
        Map<String, String> userDefinedInterceptors = Configuration.get("interceptors.interceptor",
                new HashMap<String, String>(), Map.class);
        if (MapUtils.isEmpty(userDefinedInterceptors)) {
            return;
        }

        String interceptorName = null;
        try {
            for (Map.Entry<String, String> entry : userDefinedInterceptors.entrySet()) {
                interceptorName = entry.getValue();
                Interceptor interceptor = (Interceptor) Class.forName(interceptorName).newInstance();
                interceptorMap.put(entry.getKey(), interceptor);
            }
        } catch (Exception e) {
            throw new PelicanInitializationException("Interceptor: " + interceptorName + " initialize failed!", e);
        }
    }

    private void initDefaultStack() {
        List<Interceptor> defaultInterceptors = new ArrayList<Interceptor>();
        defaultInterceptors.add(interceptorMap.get("view"));
        defaultInterceptors.add(interceptorMap.get("output_param"));
        defaultInterceptors.add(interceptorMap.get("future"));
        defaultInterceptors.add(interceptorMap.get("concurrent"));
        defaultInterceptors.add(interceptorMap.get("log"));
        defaultInterceptors.add(interceptorMap.get("aware"));
        defaultInterceptors.add(interceptorMap.get("proxy"));
        defaultInterceptors.add(interceptorMap.get("business"));
        stackConfig.put("default", defaultInterceptors);
    }

    private void initUserDefinedStack() {
        Map<String, String> stacks = Configuration.get("interceptors.stack",
                new HashMap<String, String>(), Map.class);
        if (MapUtils.isEmpty(stacks)) {
            return;
        }

        for (Map.Entry<String, String> entry : stacks.entrySet()) {
            if (StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            List<Interceptor> interceptorStack = new ArrayList<Interceptor>();
            for (String interceptorKey : entry.getValue().split("\\|")) {
                interceptorKey = interceptorKey.trim();
                if ("default".equals(interceptorKey)) {
                    interceptorStack.addAll(stackConfig.get("default"));
                    continue;
                }
                Interceptor interceptor = interceptorMap.get(interceptorKey);
                if (interceptor == null) {
                    throw new PelicanInitializationException("Interceptor key: " + interceptorKey + " not found!");
                }
                interceptorStack.add(interceptor);
            }
            stackConfig.put(entry.getKey(), interceptorStack);
        }
    }

    public static InterceptorConfig getInstance() {
        return instance;
    }

    public Iterator<Interceptor> getInterceptors(String name) {
        List<Interceptor> interceptors = stackConfig.get(name);
        if (CollectionUtils.isEmpty(interceptors)) {
            throw new PelicanRuntimeException("Interceptor stack is empty: " + name);
        }
        return interceptors.iterator();
    }
}
