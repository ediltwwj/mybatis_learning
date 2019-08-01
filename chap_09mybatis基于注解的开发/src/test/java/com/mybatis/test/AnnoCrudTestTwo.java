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
import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 17:23
 * 数据库表字段和实体类属性名不一致
 */
public class AnnoCrudTestTwo {

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

    @Test
    public void testFindAllUsers(){

        List<User> users = userDao.findAllUsers();
        for(User user : users){
            System.out.println(user);
        }
    }

    @Test
    public void testFindUserById(){

        User user = userDao.findUserById(1);
        System.out.println(user);
    }
}
