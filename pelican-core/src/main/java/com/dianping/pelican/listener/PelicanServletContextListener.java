package com.dianping.pelican.listener;

import com.dianping.pelican.concurrent.Executor;
import com.dianping.pelican.exception.PelicanInitializationException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by liming_liu on 15/7/4.
 */
public class PelicanServletContextListener implements ServletContextListener {

    private static ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        if (servletContext == null) {
            throw new PelicanInitializationException("Servlet context initialize failed!");
        }
        //init thread pool
        Executor.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        servletContext = null;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
