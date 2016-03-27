package com.dianping.pelican.exception;

/**
 * Created by liming_liu on 15/7/4.
 */
public class PelicanRuntimeException extends RuntimeException {

    public PelicanRuntimeException(String message) {
        super(message);
    }

    public PelicanRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
