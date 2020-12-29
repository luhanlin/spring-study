package com.luhanlin.ioc.dependency.lookup.exception;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

/**
 * {@link BeanCreationException} 示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class BeanCreationExceptionDemo {

    public static void main(String[] args) {
        // 创建 BeanFactory 容器对象
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 BeanDefinition Bean Class 是一个 CharSequence 接口
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Test.class);
        applicationContext.registerBeanDefinition("errorBean", beanDefinitionBuilder.getBeanDefinition());

        // 启动容器
        applicationContext.refresh();
        // 关闭应用上下文
        applicationContext.close();
    }

    static class Test implements InitializingBean {

        @PostConstruct // CommonAnnotationBeanPostProcessor
        public void init() throws Throwable {
            throw new Throwable("init() : For purposes...");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            throw new Exception("afterPropertiesSet() : For purposes...");
        }
    }
}
