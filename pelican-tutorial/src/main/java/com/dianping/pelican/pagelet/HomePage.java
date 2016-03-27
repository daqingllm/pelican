package com.dianping.pelican.pagelet;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.annotation.Future;
import com.dianping.pelican.context.SubPageletInputAware;
import com.dianping.pelican.model.Pagelet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/22.
 */
@Config("/WEB-INF/pagelet/home/homePage.ftl")
public class HomePage extends Pagelet implements SubPageletInputAware {

    private Map<String, Object> map = new HashMap<String, Object>();
    private static int SUB_LENGTH = 10;

    @Future
    private String paramFromFirstPagelet;
    @Future
    private String paramFromSecondPagelet;

    @Getter
    private List<String> itemNames = new ArrayList<String>();

    private final int IX = 1;

    @Override
    public String execute() throws Exception {
        map.put("page", "HOME PAGE");
        List<Pagelet> subPagelets = getSubPagelets();
        if (subPagelets == null) {
            subPagelets = new ArrayList<Pagelet>();
            setSubPagelets(subPagelets);
        }
        for (int i=0; i<SUB_LENGTH; ++i) {
            subPagelets.add(createHomeListItem("item"+i));
        }

        return SUCCESS;
    }

    private HomeListItem createHomeListItem(String itemContent) {
        itemNames.add(itemContent);
        HomeListItem homeListItem = new HomeListItem();
        homeListItem.setName(itemContent);
        homeListItem.setItem(itemContent);
        homeListItem.setView("/WEB-INF/pagelet/home/homeListItem.ftl");
        return homeListItem;
    }

    public void setInputParamsForSubPagelet(Map<String, Object> map) {
        this.map = map;
    }
}
