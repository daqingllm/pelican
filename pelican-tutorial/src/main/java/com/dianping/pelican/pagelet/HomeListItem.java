package com.dianping.pelican.pagelet;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.model.Pagelet;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by liming_liu on 15/11/26.
 */
@Config(name = "hahah", view = "/WEB-INF/pagelet/home/homeSecond.ftl")
public class HomeListItem extends Pagelet {

    @Getter
    @Setter
    private String item;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
