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
 * @date 2019/7/31 16:04
 */
public class UserTest {

    private InputStream in;
    private SqlSessionFactory factory;
    private SqlSession sqlSession;
    private UserDao userDao;

    // 用于在测试方法执行之前执行
    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        factory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = factory.openSession();
        userDao = sqlSession.getMapper(UserDao.class);
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
     * 测试一级缓存
     */
    @Test
    public void testFirstLevelCache(){

        User user1 = userDao.findUserById(1);
        System.out.println(user1);

        User user2 = userDao.findUserById(1);
        System.out.println(user2);

        // 查看地址是否相同
        System.out.println(user1==user2);

        System.out.println("-----------------");

        User user3 = userDao.findUserById(2);
        System.out.println(user3);

        // 销毁SqlSession对象，再获取
        // sqlSession.close();
        // sqlSession = factory.openSession();
        sqlSession.clearCache();
        userDao = sqlSession.getMapper(UserDao.class);

        User user4 = userDao.findUserById(2);
        System.out.println(user4);

        System.out.println(user3==user4);
    }

    /**
     * 测试缓存的同步
     */
    @Test
    public void testClearCache(){

        User user1 = userDao.findUserById(1);
        System.out.println(user1);

        user1.setUsername("小更");
        user1.setAddress("上海市静安区");

        // 更新操作会触发清空以及缓存
        userDao.updateUser(user1);

        User user2 = userDao.findUserById(1);
        System.out.println(user2);

        System.out.println(user1 == user2);
    }
}
