package com.dianping.pelican.context;

import java.util.Map;

/**
 *
 * Created by liming_liu on 15/7/8.
 */
public interface SubPageletInputAware {

    void setInputParamsForSubPagelet(Map<String, Object> params);
}
