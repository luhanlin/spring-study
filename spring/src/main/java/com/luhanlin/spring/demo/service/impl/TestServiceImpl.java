package com.luhanlin.spring.demo.service.impl;

import com.luhanlin.spring.framework.annotation.LUService;
import com.luhanlin.spring.demo.service.TestService;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2020/8/8 5:14 下午
 */
@LUService
public class TestServiceImpl implements TestService {
    @Override
    public void test() {

    }

    @Override
    public String query(String name, String addr) throws Exception {
        throw new Exception("故意抛出的异常");
    }
}
