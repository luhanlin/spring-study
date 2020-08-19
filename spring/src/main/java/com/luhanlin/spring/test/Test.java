package com.luhanlin.spring.test;

import com.luhanlin.spring.demo.controller.TestController;
import com.luhanlin.spring.framework.v2.context.support.LUDefaultApplicationContext;

/**
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2020-08-19 09:49]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class Test {

    public static void main(String[] args) {
        LUDefaultApplicationContext context = new LUDefaultApplicationContext(new String[]{"application.properties"});

        context.printBeanWrapperMap();
        System.out.println("-----------------------------------");
        context.printBeanDefinitionMap();
        TestController bean = (TestController) context.getBean(TestController.class);

    }
}
