package com.dianping.pelican.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.DefaultObjectWrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/8.
 */
public class JSPSupport {
    public static Map<String,Object> getSupport(HttpServletRequest request,ServletContext servletContext){

        Map<String,Object> context = new HashMap<String, Object>();

        TaglibFactory taglibs = (TaglibFactory) servletContext.getAttribute(".freemarker.JspTaglibs");
        if( taglibs==null ){
            taglibs= new TaglibFactory(servletContext);
        }
        context.put("JspTaglibs", taglibs);

        ServletContextHashModel servletContextHashModel = (ServletContextHashModel)servletContext.getAttribute(".freemarker.Application");
        context.put("Application", servletContextHashModel);

        DefaultObjectWrapper wrapper = new DefaultObjectWrapper();
        context.put("Request", new HttpRequestHashModel(request, wrapper));

        return context;
    }
}
