package com.luhanlin.transfer.annotation;

/**
 * 自定义 service 组件注解
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LuService {

    String value() default "";
}
