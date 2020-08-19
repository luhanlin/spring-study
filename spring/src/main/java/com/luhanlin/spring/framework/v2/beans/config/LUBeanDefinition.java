package com.luhanlin.spring.framework.v2.beans.config;

import lombok.Data;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 15:32]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Data
public class LUBeanDefinition {

    private String beanClassName;

    private boolean lazyInit = false;

    private String factoryBeanName;
}
