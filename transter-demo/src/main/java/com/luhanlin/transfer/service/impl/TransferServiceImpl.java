package com.luhanlin.transfer.service.impl;

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
public class TransferServiceImpl implements TransferService {

//    private AccountDao accountDao = (AccountDao) BeanFactory.getBean("accountDao");

    private AccountDao accountDao;
    private TransactionManager transactionManager;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {

        try {
            // 开启事务
            transactionManager.beginTransaction();

            Account from = accountDao.queryAccountByCardNo(fromCardNo);
            Account to = accountDao.queryAccountByCardNo(toCardNo);

            from.setMoney(from.getMoney()-money);
            to.setMoney(to.getMoney()+money);

            accountDao.updateAccountByCardNo(to);
            int c = 1/0;
            accountDao.updateAccountByCardNo(from);
            transactionManager.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback();
            throw e;
        }
    }
}
