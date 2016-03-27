package com.dianping.pelican.model;

import com.dianping.pelican.renderer.PageletRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 页面控件
 * Created by liming_liu on 15/7/1.
 */
public abstract class Pagelet {

    /** 可由{@link com.dianping.pelican.annotation.Name}定义 */
    private String name;

    private List<Pagelet> subPagelets = new ArrayList<Pagelet>();

    private String stack = "default";

    /** 可由{@link com.dianping.pelican.annotation.View}定义 */
    private String view;

    private Properties views;

    private Map<String, List<Pagelet>> viewPagelets;

    private boolean pipe = false;

    private int timeoutMilliseconds;

    public abstract String execute() throws Exception;

    public static final String SUCCESS="success";

    public static final String NONE="none";

    public static final String ERROR="error";

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getStack() {
        return stack;
    }

    public final void setStack(String stack) {
        this.stack = stack;
    }

    public final List<Pagelet> getSubPagelets() {
        return subPagelets;
    }

    public final void setSubPagelets(List<Pagelet> subPagelets) {
        this.subPagelets = subPagelets;
    }

    public final String getView() {
        return view;
    }

    public final void setView(String view) {
        this.view = view;
    }

    public final Properties getViews() {
        return views;
    }

    public final void setViews(Properties views) {
        this.views = views;
    }

    public final Map<String, List<Pagelet>> getViewPagelets() {
        return viewPagelets;
    }

    public final void setViewPagelets(Map<String, List<Pagelet>> viewPagelets) {
        this.viewPagelets = viewPagelets;
    }

    public final boolean isPipe() {
        return pipe;
    }

    public final void setPipe(boolean pipe) {
        this.pipe = pipe;
    }

    public final int getTimeoutMilliseconds() {
        return timeoutMilliseconds;
    }

    public final void setTimeoutMilliseconds(int timeoutMilliseconds) {
        this.timeoutMilliseconds = timeoutMilliseconds;
    }

    public final String render(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {

        return PageletRenderer.render(this, params, request, response);
    }
}
