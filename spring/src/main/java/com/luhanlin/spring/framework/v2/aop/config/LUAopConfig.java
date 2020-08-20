package com.luhanlin.spring.framework.v2.aop.config;

import lombok.Data;

@Data
public class LUAopConfig {

    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;

}