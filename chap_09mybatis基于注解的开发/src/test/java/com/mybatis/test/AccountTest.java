package com.mybatis.test;

import com.mybatis.dao.AccountDao;
import com.mybatis.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 17:51
 * 多表查询测试 一对一
 */
public class AccountTest {

    private InputStream in;
    private SqlSessionFactory factory;
    private SqlSession session;
    private AccountDao accountDao;

    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        accountDao = session.getMapper(AccountDao.class);
    }

    @After
    public void destory() throws IOException{

        session.commit();
        session.close();
        in.close();
    }

    @Test
    public void testFindAccountUser(){

        List<Account> accounts = accountDao.findAllAccountUser();
        for(Account account : accounts){
            System.out.println("---- 每个账户的信息 ----");
            System.out.println(account);
            System.out.println(account.getUser());
        }
    }
}
