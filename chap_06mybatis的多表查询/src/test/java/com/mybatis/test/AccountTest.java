package com.mybatis.test;

import com.mybatis.dao.AccountDao;
import com.mybatis.dao.UserDao;
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
 * @date 2019/7/31 14:57
 */
public class AccountTest {

    private InputStream in;
    private SqlSession sqlSession;
    private AccountDao accountDao;

    // 用于在测试方法执行之前执行
    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = factory.openSession();
        accountDao = sqlSession.getMapper(AccountDao.class);
    }

    // 用于在测试方法执行之后执行
    @After
    public void destroy() throws Exception{

        // 加上这句话才可以正常操作数据库
        sqlSession.commit();
        sqlSession.close();
        in.close();
    }

    /**
     * 测试查询所有账户
     */
    @Test
    public void testFindAllAccounts(){

        List<Account> accounts = accountDao.findAllAccounts();
        for(Account account : accounts){
            System.out.println("------每个account用户的信息------");
            System.out.println(account);
            System.out.println(account.getUser());
        }
    }
}
