package com.luhanlin.ioc.domain;

import lombok.Data;

/**
 * 实体类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
public class User {

    private String name;
    private Integer age;

    public static User createUser() {
        User user = new User();
        user.setName("lin");
        user.setAge(2);
        return user;
    }
}

