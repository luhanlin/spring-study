package com.luhanlin.spring.framework.v2.beans.support;

import com.luhanlin.spring.framework.v2.beans.LUBeanWrapper;
import com.luhanlin.spring.framework.v2.beans.config.LUBeanDefinition;
import com.luhanlin.spring.framework.v2.context.support.LUAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <类详细描述> 默认 BeanFactory
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 15:30]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LUDefaultListableBeanFactory extends LUAbstractApplicationContext {

    /**
     * Map of bean definition objects, keyed by bean name.
     */
    protected final Map<String, LUBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    protected final Map<String, Object> singletonBeanMap = new ConcurrentHashMap<>(256);
    /**
     * Cache of unfinished FactoryBean instances: FactoryBean name to BeanWrapper.
     */
    protected final ConcurrentMap<String, LUBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

}
