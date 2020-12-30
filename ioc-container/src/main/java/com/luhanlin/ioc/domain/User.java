package com.luhanlin.ioc.domain;

import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 实体类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
public class User implements BeanNameAware {

    private Long id;
    private String name;
    private Integer age;
    private transient String beanName;

    public static User createUser() {
        User user = new User();
        user.setId(System.nanoTime());
        user.setName("lin");
        user.setAge(2);
        return user;
    }

    @PostConstruct
    public void init() {
        System.out.println("User Bean [" + beanName + "] 初始化...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("User Bean [" + beanName + "] 销毁中...");
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}

