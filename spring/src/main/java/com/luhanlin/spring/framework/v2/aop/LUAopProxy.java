package com.luhanlin.spring.framework.v2.aop;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-20 09:15]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LUAopProxy {

    Object getProxy();

    Object getProxy(ClassLoader classLoader);
}
