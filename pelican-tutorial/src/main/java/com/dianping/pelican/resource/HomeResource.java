package com.dianping.pelican.resource;

import com.dianping.pelican.model.Pagelet;
import com.dianping.pelican.pagelet.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liming_liu on 15/7/22.
 */
@Path("/")
public class HomeResource {

    static {
        BasicConfigurator.configure();
        PropertyConfigurator.configure("");

        DOMConfigurator.configure("classpath:log/log4j.xml");
    }

    HomePage homePage;
    HomeFirst homeFirst;
    HomeSecond homeSecond;
    HomeFirstSubA homeFirstSubA;
    HomeFirstSubB homeFirstSubB;

    private void renewPagelet() {
        homePage = new HomePage();
        homePage.setView("/WEB-INF/pagelet/home/homePage.ftl");

        homeFirst = new HomeFirst();
        homeFirst.setView("/WEB-INF/pagelet/home/homeFirst.ftl");

        homeFirstSubA = new HomeFirstSubA();
        homeFirstSubA.setView("/WEB-INF/pagelet/home/empty.ftl");
        homeFirstSubB = new HomeFirstSubB();
        homeFirstSubB.setView("/WEB-INF/pagelet/home/empty.ftl");
        List<Pagelet> homeFirstSub = new ArrayList<Pagelet>();
        homeFirstSub.add(homeFirstSubA);
        homeFirstSub.add(homeFirstSubB);
        homeFirst.setSubPagelets(homeFirstSub);

        homeSecond = new HomeSecond();
        homeSecond.setView("/WEB-INF/pagelet/home/homeSecond.ftl");

        List<Pagelet> homePageSub = new ArrayList<Pagelet>();
        homePageSub.add(homeFirst);
        homePageSub.add(homeSecond);
        homePage.setSubPagelets(homePageSub);
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String home(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        renewPagelet();

        return homePage.render(new HashMap<String, Object>(), request, response);
    }
}
