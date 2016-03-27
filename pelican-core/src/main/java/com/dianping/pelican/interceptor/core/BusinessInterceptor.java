package com.dianping.pelican.interceptor.core;

import com.dianping.pelican.context.PageletInvocation;
import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.interceptor.Interceptor;
import com.dianping.pelican.model.Pagelet;
import com.dianping.pelican.util.BeanPopulatingUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/2.
 */
public class BusinessInterceptor implements Interceptor {

    private Logger logger = Logger.getLogger(BusinessInterceptor.class);

    @Override
    public PageletInvocation.ResultCode intercept(PageletInvocation invocation) throws Exception {
        PageletInvocation.ResultCode resultCode = invocation.invoke();
        if (!PageletInvocation.SUCCESS.equals(resultCode)) {
            return resultCode;
        }

        Pagelet pagelet = invocation.getPagelet();
        BeanPopulatingUtils.populateBeanWithMap(pagelet, (Map) invocation.getContext().get(PageletInvocation.INPUT_KEY));
        String returnCode = pagelet.execute();

        if (resultCode == null) {
            logger.warn("The return code of pagelet shouldn't be null! Pagelet: " + pagelet.getName());
            return invocation.NONE;
        }
        if (Pagelet.NONE.equals(returnCode)) {
            return invocation.NONE;
        } else if (Pagelet.ERROR.equals(returnCode)) {
            throw new PelicanRuntimeException("Business Error: " + pagelet.getName());
        }

        handleView(returnCode, pagelet);
        invocation.getContext().put(PageletInvocation.OUTPUT_KEY, BeanPopulatingUtils.extractBeanToMap(pagelet));

        return invocation.SUCCESS;
    }

    private void handleView(String returnCode, Pagelet pagelet) {
        if (Pagelet.SUCCESS.equals(returnCode) && StringUtils.isNotBlank(pagelet.getView())) {
            return;
        }
        if (pagelet.getViews() == null || pagelet.getViews().getProperty(returnCode) == null) {
            throw new PelicanRuntimeException("No matched view found! Return code: " + returnCode + ". Pagelet: " + pagelet.getName());
        }
        String viewPath = pagelet.getViews().getProperty(returnCode);
        if (StringUtils.isBlank(viewPath)) {
            throw new PelicanRuntimeException("No view for return code " + returnCode + ": " + pagelet.getName());
        }
        pagelet.setView(viewPath);

        Map<String, List<Pagelet>> viewPageletMap = pagelet.getViewPagelets();
        if (MapUtils.isEmpty(viewPageletMap)) {
            return;
        }
        List<Pagelet> viewPagelets = viewPageletMap.get(returnCode);
        if (CollectionUtils.isNotEmpty(viewPagelets)) {
            pagelet.setSubPagelets(viewPagelets);
        }
    }
}
