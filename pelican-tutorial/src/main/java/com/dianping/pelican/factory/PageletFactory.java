package com.dianping.pelican.factory;

import com.dianping.pelican.pagelet.HomeFirst;
import com.dianping.pelican.pagelet.HomePage;
import com.dianping.pelican.pagelet.HomeSecond;

import java.util.Arrays;

/**
 * Created by liming_liu on 15/7/22.
 */
public abstract class PageletFactory {

    public static HomePage createHomePagelet() {

        HomePage homePage = new HomePage();
        HomeFirst homeFirst = new HomeFirst();
        HomeSecond homeSecond = new HomeSecond();

        homePage.setView("/WEB-INF/pagelet/home/homePage.ftl");

        homeFirst.setName("first");
        homeFirst.setView("/WEB-INF/pagelet/home/homeFirst.ftl");

        homeSecond.setName("second");
        homeSecond.setView("/WEB-INF/pagelet/home/homeSecond.ftl");

        homePage.setSubPagelets(Arrays.asList(homeFirst, homeSecond));

        return homePage;
    }
}
