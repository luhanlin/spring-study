package com.luhanlin.ioc.dependency.lookup;

import com.luhanlin.ioc.annotation.Super;
import com.luhanlin.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * 依赖查找示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class DependencyLookupTest {

    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");

        // 根据名称实时查找
        lookupByName(beanFactory);

        // 根据类型查找bean
        lookupByType(beanFactory);

        // 按照类型查找集合对象
        lookupCollectionByType(beanFactory);

        // 根据注解的方式进行BEAN的查找
        lookupCollectionByAnnotation(beanFactory);

        // 延迟查找
        lookupInLazy(beanFactory);
    }

    private static void lookupInLazy(BeanFactory beanFactory) {
        ObjectFactory objectFactory = (ObjectFactory) beanFactory.getBean("objectFactory");
        User user = (User) objectFactory.getObject();
        System.out.println("延迟查找：" + user);
    }

    private static void lookupByName(BeanFactory beanFactory) {
        User user = (User) beanFactory.getBean("user");
        System.out.println("根据名称实时查找：" + user);
    }

    private static void lookupCollectionByAnnotation(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = (Map) listableBeanFactory.getBeansWithAnnotation(Super.class);
            System.out.println("通过注解@Super 查找到的所有的 User 集合对象：" + users);
        }
    }

    private static void lookupCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = listableBeanFactory.getBeansOfType(User.class);
            System.out.println("通过类型 查找到的所有的 User 集合对象：" + users);
        }
    }

    private static void lookupByType(BeanFactory beanFactory) {
        User user = beanFactory.getBean(User.class);
        System.out.println("实时查找：" + user);
    }
}
