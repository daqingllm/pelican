package com.dianping.pelican.pagelet;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.annotation.FutureImpl;
import com.dianping.pelican.context.http.HttpResponseAware;
import com.dianping.pelican.model.Pagelet;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by liming_liu on 15/7/22.
 */
@Config(name = "second", view = "/WEB-INF/pagelet/home/homeSecond.ftl")
public class HomeSecond extends Pagelet implements HttpResponseAware {

    @Setter
    @Getter
    private String page;

    private HttpServletResponse response;

    @FutureImpl
    private String paramFromSecondPagelet;

    @Override
    public String execute() throws Exception {
        paramFromSecondPagelet = "second future param";
        if (response != null) {
            System.out.println("response is not empty");
        }
        return SUCCESS;
    }

    public void setHttpResponse(HttpServletResponse response) {
        this.response = response;
    }
}
