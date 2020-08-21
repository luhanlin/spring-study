package com.luhanlin.spring.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LURequestParam {

    String value() default "";

}
