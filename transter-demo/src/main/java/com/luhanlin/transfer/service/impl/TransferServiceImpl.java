package com.luhanlin.transfer.service.impl;

import com.luhanlin.transfer.annotation.LuAutowired;
import com.luhanlin.transfer.annotation.LuService;
import com.luhanlin.transfer.annotation.LuTransactional;
import com.luhanlin.transfer.dao.AccountDao;
import com.luhanlin.transfer.factory.BeanFactory;
import com.luhanlin.transfer.pojo.Account;
import com.luhanlin.transfer.service.TransferService;
import com.luhanlin.transfer.utils.ConnectionUtils;
import com.luhanlin.transfer.utils.TransactionManager;

/**
 * 服务层实现
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@LuService
@LuTransactional
public class TransferServiceImpl implements TransferService {

//    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    @LuAutowired
    private AccountDao accountDao;
    @LuAutowired
    private TransactionManager transactionManager;

//    public void setAccountDao(AccountDao accountDao) {
//        this.accountDao = accountDao;
//    }
//
//    public void setTransactionManager(TransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        accountDao.updateAccountByCardNo(to);
        int c = 1/0;
        accountDao.updateAccountByCardNo(from);
    }
}
