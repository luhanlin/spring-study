package com.luhanlin.spring.framework.v2.beans;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-18 12:10]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LUFactoryBean<T> {

    T getObject();

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }
}
