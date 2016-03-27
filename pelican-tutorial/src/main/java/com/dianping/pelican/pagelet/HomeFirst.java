package com.dianping.pelican.pagelet;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.annotation.Loggable;
import com.dianping.pelican.context.http.HttpRequestAware;
import com.dianping.pelican.model.Pagelet;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liming_liu on 15/7/22.
 */
@Config(view = "/WEB-INF/pagelet/home/homeFirst.ftl", name = "first")
public class HomeFirst extends Pagelet implements HttpRequestAware {

    @Getter
    private String title;

    private HttpServletRequest request;

    @Loggable
    private Foo foo = new FooImpl();

    @Override
    public String execute() throws Exception {
        title = "Hello Pelican!";
        foo.printFoo();
        waitAMoment(100, "wait!!!!");
        return SUCCESS;
    }

    @Loggable
    public void waitAMoment(int time, String log) throws InterruptedException {
        Thread.sleep(time);
        System.out.println(log);
    }

    public void setHttpRequest(HttpServletRequest request) {
        this.request = request;
    }
}
