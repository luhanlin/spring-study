package com.luhanlin.spring.demo.aspect;

import com.luhanlin.spring.framework.v2.aop.aspect.LUJoinPoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(LUJoinPoint joinPoint) {
    }

    //在调用一个方法之后，执行after方法
    public void after(LUJoinPoint joinPoint) {
    }

    public void afterThrowing(LUJoinPoint joinPoint, Throwable ex) {
    }

}
