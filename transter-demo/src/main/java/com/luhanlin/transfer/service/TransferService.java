package com.luhanlin.transfer.service;

/**
 * 交易服务接口
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
public interface TransferService {

    void transfer(String fromCardNo,String toCardNo,int money) throws Exception;

}
