package com.mybatis.test;

import com.mybatis.dao.UserDao;
import com.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 13967
 * @date 2019/8/1 18:44
 * 注解的二级缓存测试
 */
public class SecondCacheTest {

    private InputStream in;
    private SqlSessionFactory factory;
    private SqlSession session;
    private UserDao userDao;

    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        session = factory.openSession();
        userDao = session.getMapper(UserDao.class);
    }

    @After
    public void destory() throws IOException{

        session.commit();
        session.close();
        in.close();
    }

    /**
     * 使用findUserById测试注解的二级缓存
     */
    @Test
    public void testSecondCache(){

        User user1 = userDao.findUserById(1);
        System.out.println(user1);

        // 释放一级缓存
        session.close();
        // 再次打开session
        session = factory.openSession();
        userDao = session.getMapper(UserDao.class);
        User user2 = userDao.findUserById(1);
        System.out.println(user2);
    }
}
