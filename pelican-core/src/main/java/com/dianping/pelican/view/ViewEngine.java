package com.dianping.pelican.view;

import java.util.Map;

/**
 * Created by liming_liu on 15/7/4.
 */
public interface ViewEngine {

    String render(String view, Map<String, Object> params);
}
