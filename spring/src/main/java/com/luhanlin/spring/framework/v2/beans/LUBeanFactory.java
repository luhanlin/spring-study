package com.luhanlin.spring.framework.v2.beans;

/**
 * <类详细描述> 单例工厂类
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 12:10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LUBeanFactory {

    /**
     * 从IOC容器中获取实例对象
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    Object getBean(Class<?> className);
}
