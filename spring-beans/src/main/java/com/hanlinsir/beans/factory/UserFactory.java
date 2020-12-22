package com.hanlinsir.beans.factory;

import com.luhanlin.ioc.domain.User;

/**
 * 实例工厂类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface UserFactory {

    default User createUser() {
        return User.createUser();
    }
}
