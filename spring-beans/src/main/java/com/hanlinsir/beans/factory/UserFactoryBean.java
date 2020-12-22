package com.hanlinsir.beans.factory;

import com.luhanlin.ioc.domain.User;
import org.springframework.beans.factory.FactoryBean;

/**
 * {@link User} 的 FactoryBean
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public class UserFactoryBean implements FactoryBean<User> {
    @Override
    public User getObject() throws Exception {
        return User.createUser();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
