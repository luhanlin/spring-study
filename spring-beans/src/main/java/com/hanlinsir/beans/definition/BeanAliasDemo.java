package com.hanlinsir.beans.definition;

import com.luhanlin.ioc.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Bean 别名示例，测试是否为同一对象
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class BeanAliasDemo {

    public static void main(String[] args) {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/bean-definitions-context.xml");
        User user = beanFactory.getBean("user", User.class);
        User linUser = beanFactory.getBean("lin-user", User.class);
        System.out.println("测试使用别名获取的bean 是否为同一个实例对象：" + (user == linUser));
    }
}
