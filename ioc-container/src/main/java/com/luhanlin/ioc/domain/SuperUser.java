package com.luhanlin.ioc.domain;

import com.luhanlin.ioc.annotation.Super;
import lombok.Data;
import lombok.ToString;

/**
 * {@link User} 超集
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Super
@Data
@ToString(callSuper = true)
public class SuperUser extends User {
    private String address;
}
