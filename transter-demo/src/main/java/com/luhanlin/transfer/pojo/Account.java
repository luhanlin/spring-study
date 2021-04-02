package com.luhanlin.transfer.pojo;

import lombok.Data;

/**
 * 账户
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@Data
public class Account {

    private Integer id;
    private String cardNo;
    private String name;
    private int money;
}
