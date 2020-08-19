package com.luhanlin.spring.framework.v2.webmvc.servlet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-19 11:14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LUHandlerMapping {

    private Pattern urlPattern;  //正则

    private Method method;

    // 方法所在的controller类
    private Object controller;
}
