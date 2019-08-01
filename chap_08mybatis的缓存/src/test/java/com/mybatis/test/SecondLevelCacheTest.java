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
 * @date 2019/8/1 12:00
 */
public class SecondLevelCacheTest {

    private InputStream in;
    private SqlSessionFactory factory;

    // 用于在测试方法执行之前执行
    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
    }

    // 用于在测试方法执行之后执行
    @After
    public void destroy() throws Exception{

        in.close();
    }

    @Test
    public void testSecondLevelCache(){

        SqlSession sqlSession1 = factory.openSession();
        UserDao dao1 = sqlSession1.getMapper(UserDao.class);
        User user1 = dao1.findUserById(1);
        System.out.println(user1);
        sqlSession1.close();

        SqlSession sqlSession2 = factory.openSession();
        UserDao dao2 = sqlSession2.getMapper(UserDao.class);
        User user2 = dao2.findUserById(1);
        System.out.println(user2);
        sqlSession2.close();

        // 二级缓存存储的是数据而不是对象所以出现false
        System.out.println(user1 == user2);
    }
}
