package com.luhanlin.spring.framework.v2.aop.aspect;

import java.lang.reflect.AccessibleObject;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-20 09:28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LUJoinPoint {

    /**
     * Proceed to the next interceptor in the chain.
     * <p>The implementation and the semantics of this method depends
     * on the actual joinpoint type (see the children interfaces).
     * @return see the children interfaces' proceed definition
     * @throws Throwable if the joinpoint throws an exception
     */
    Object proceed() throws Throwable;

    /**
     * Return the object that holds the current joinpoint's static part.
     * <p>For instance, the target object for an invocation.
     * @return the object (can be null if the accessible object is static)
     */
    Object getThis();

    /**
     * Return the static part of this joinpoint.
     * <p>The static part is an accessible object on which a chain of
     * interceptors are installed.
     */
    AccessibleObject getStaticPart();

}
