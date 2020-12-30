package com.hanlinsir.beans.scope;

import com.luhanlin.ioc.domain.User;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * 自定义 Scope {@link CustomThreadLocalScope} 示例
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class CustomThreadLocalScopeDemo {

    @Bean
    @Scope(CustomThreadLocalScope.SCOPE_THREAD_LOCAL)
    public User user() {
        return User.createUser();
    }

    public static void main(String[] args) {
        // 创建容器对象
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册 配置类
        applicationContext.register(CustomThreadLocalScopeDemo.class);

        // 注册 scope
        applicationContext.addBeanFactoryPostProcessor(beanFactory -> {
            beanFactory.registerScope(CustomThreadLocalScope.SCOPE_THREAD_LOCAL, new CustomThreadLocalScope());
        });

        // 启动容器
        applicationContext.refresh();

        scopedBeansByLookup(applicationContext);

        // 关闭容器
        applicationContext.close();
    }

    private static void scopedBeansByLookup(AnnotationConfigApplicationContext applicationContext) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                // user 是共享 Bean 对象
                User user = applicationContext.getBean("user", User.class);
                System.out.printf("[Thread id :%d] user = %s%n", Thread.currentThread().getId(), user);
                User user2 = applicationContext.getBean("user", User.class);
                System.out.printf("Second [Thread id :%d] user = %s%n", Thread.currentThread().getId(), user2);

            });

            // 启动线程
            thread.start();
            // 强制线程执行完成
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
