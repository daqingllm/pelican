package com.dianping.pelican.exception;

/**
 * Created by liming_liu on 15/7/3.
 */
public class PelicanInitializationException extends RuntimeException {

    public PelicanInitializationException(String message) {
        super(message);
    }

    public PelicanInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
