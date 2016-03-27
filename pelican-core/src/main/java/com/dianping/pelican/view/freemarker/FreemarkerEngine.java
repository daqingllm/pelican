package com.dianping.pelican.view.freemarker;

import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.util.WebSourceLoader;
import com.dianping.pelican.view.ViewEngine;
import freemarker.cache.CacheStorage;
import freemarker.cache.SoftCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by liming_liu on 15/7/4.
 */
public class FreemarkerEngine implements ViewEngine {

    private ConfigureLoader loader = new ConfigureLoader();

    private CacheStorage cache = new SoftCacheStorage();

    @Override
    public String render(String view, Map<String, Object> params) {
        Template template = null;
        if (!com.dianping.pelican.config.Configuration.get("cache", true, Boolean.class)) {
            template = createTemplate(view);
        } else {
            template = (Template) cache.get(view);
            if (template == null) {
                template = createTemplate(view);
                cache.put(view, template);
            }
        }

        StringWriter writer = new StringWriter();
        try {
            template.process(params, writer);
            writer.flush();
        } catch (Exception e) {
            throw new PelicanRuntimeException("View render failed: " + view, e);
        }

        return writer.toString();
    }

    private Template createTemplate(String view) {
        Template template = null;
        Reader viewReader = WebSourceLoader.readSource(view);
        if (viewReader == null) {
            throw new PelicanRuntimeException("View not found: " + view);
        }
        Configuration config = loader.load();
        try {
            template = new Template(view, viewReader, config);
            template.setEncoding("UTF-8");
        } catch (IOException e) {
            throw new PelicanRuntimeException("View initialize failed: " + view, e);
        }

        return template;
    }
}
