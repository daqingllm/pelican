package com.dianping.pelican.pagelet;

import com.dianping.pelican.annotation.Config;
import com.dianping.pelican.model.Pagelet;

/**
 * Created by liming_liu on 15/11/13.
 */
@Config("/WEB-INF/pagelet/home/empty.ftl")
public class HomeFirstSubB extends Pagelet {
    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
