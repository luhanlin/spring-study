package com.luhanlin.spring.demo.controller;

import com.luhanlin.spring.demo.service.TestService;
import com.luhanlin.spring.framework.annotation.LUAutowired;
import com.luhanlin.spring.framework.annotation.LUController;
import com.luhanlin.spring.framework.annotation.LURequestMapping;
import com.luhanlin.spring.framework.annotation.LURequestParam;
import com.luhanlin.spring.framework.v2.webmvc.servlet.LUModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public LUModelAndView hello(@LURequestParam("name") String name, @LURequestParam("addr") String addr){

        Map<String,Object> model = new HashMap<String,Object>();
        model.put("name",name);
        model.put("addr", addr);
        return new LUModelAndView("first",model);
    }

    @LURequestMapping("/query")
    public LUModelAndView query(@LURequestParam("name") String name, @LURequestParam("addr") String addr){
        String result = null;
        try {
            result = testService.query(name,addr);
            return null;
        } catch (Exception e) {
            Map<String,Object> model = new HashMap<String,Object>();
            model.put("detail",e.getCause().getMessage());
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            return new LUModelAndView("500",model);
        }
    }
}
