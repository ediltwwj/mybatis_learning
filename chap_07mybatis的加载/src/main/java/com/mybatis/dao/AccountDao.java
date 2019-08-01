package com.mybatis.dao;

import com.mybatis.domain.Account;

import java.util.List;

/**
 * @author 13967
 * @date 2019/7/31 14:53
 */
public interface AccountDao {

    /**
     * 一对一，多对一
     * 查询所有账户,并获得账户当前所属用户信息
     * @return
     */
    List<Account> findAllAccounts();

    /**
     * 根据用户id查询账户信息
     * @param uid
     * @return
     */
    List<Account> findAccountByUid(Integer uid);
}
