package com.luhanlin.spring.framework.v2.webmvc.servlet;

import lombok.Data;

import java.util.Map;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-19 14:17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class LUModelAndView {

    private String viewName;

    private Map<String, ?> model;

    public LUModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public LUModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
}
