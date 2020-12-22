package com.luhanlin.ioc.dependency.injection;

import com.luhanlin.ioc.repository.UserRepository;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 依赖注入示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class DependencyInjectionTest {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-injection-context.xml");

        UserRepository userRepository = applicationContext.getBean("userRepository", UserRepository.class);

        System.out.println(userRepository);

        // 验证 是否是同一个上下文
        ObjectFactory userFactory = userRepository.getObjectFactory();

        System.out.println(userFactory.getObject() == applicationContext); // true

        /**
         * {@link org.springframework.beans.factory.BeanFactory} 与 {@link ApplicationContext} 不是同一个Bean容器
         */
        System.out.println(userRepository.getBeanFactory() == applicationContext); // false

    }
}
