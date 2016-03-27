package com.dianping.pelican.view;

import com.dianping.pelican.view.freemarker.FreemarkerEngine;

/**
 * Created by liming_liu on 15/7/4.
 */
public abstract class ViewEngineFactory {

    private static ViewEngine viewEngine;

    static {
        viewEngine = new FreemarkerEngine();
    }

    public static ViewEngine getViewEngine() {
        return viewEngine;
    }

}
