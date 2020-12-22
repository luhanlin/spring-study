package com.hanlinsir.beans.instantiation;

import com.hanlinsir.beans.factory.DefaultUserFactory;
import com.hanlinsir.beans.factory.UserFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 单实例注册示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class SingletonBeanRegistrationDemo {

    public static void main(String[] args) {
//        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-instantiation-context.xml");
        // TODO 此处使用ClassPathXmlApplicationContext 进行容器环境启动，注册出错，后续进行查找原因探究
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        // 将被注册的实例对象
        DefaultUserFactory userFactory = new DefaultUserFactory();
        SingletonBeanRegistry beanFactory = applicationContext.getBeanFactory();
        // 注册实例，如果配置文件中已经配置相同名称，则注册失败
//        beanFactory.registerSingleton("userFactory", userFactory);
        beanFactory.registerSingleton("userFactory2", userFactory);
        // 启动容器
        applicationContext.refresh();

        // 依赖查找
        UserFactory userFactoryByLookup = applicationContext.getBean("userFactory2", UserFactory.class);
        System.out.println("userFactory  == userFactoryByLookup : " + (userFactory == userFactoryByLookup));

        applicationContext.close();
    }
}
