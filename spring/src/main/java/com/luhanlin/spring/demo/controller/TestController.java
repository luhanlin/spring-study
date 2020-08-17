package com.luhanlin.spring.demo.controller;

import com.luhanlin.spring.framework.annotation.LUAutowired;
import com.luhanlin.spring.framework.annotation.LUController;
import com.luhanlin.spring.framework.annotation.LURequestMapping;
import com.luhanlin.spring.framework.annotation.LURequestParam;
import com.luhanlin.spring.demo.service.TestService;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/8/8 12:40 下午
 */
@LUController
@LURequestMapping("test")
public class TestController {

    @LUAutowired
    private TestService testService;

    @LURequestMapping("hello")
    public String hello(@LURequestParam("content") String content){
        return "hello, " + content;
    }

}
