package com.dianping.pelican.util;

import com.dianping.pelican.exception.PelicanRuntimeException;
import com.dianping.pelican.listener.PelicanServletContextListener;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by liming_liu on 15/7/4.
 */
public abstract class WebSourceLoader {

    public static InputStreamReader readSource(String path) {
        try {
            return new InputStreamReader(PelicanServletContextListener.
                    getServletContext().getResourceAsStream(path), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new PelicanRuntimeException("UTF-8 is unsupported!", e);
        } catch (Exception e) {
            throw new PelicanRuntimeException("Load path error: " + path, e);
        }
    }
}
