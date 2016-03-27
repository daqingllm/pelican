package com.dianping.pelican.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块配置
 * 1.0.7 name、view
 * Created by liming_liu on 15/12/31.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Config {

    /**
     * 默认为view
     * @return
     */
    String value() default "";

    String name() default "";

    String view() default "";
}
