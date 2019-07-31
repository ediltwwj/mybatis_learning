package com.mybatis.test;

import com.mybatis.dao.UserDao;
import com.mybatis.domain.QueryVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 13967
 * @date 2019/7/30 17:31
 *
 * 测试Mybatis的CRUD操作
 */
public class MybatisTest {

    private InputStream in;
    private SqlSession sqlSession;
    private UserDao userDao;

    // 用于在测试方法执行之前执行
    @Before
    public void init() throws IOException {

        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
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
     * 查找所有用户
     */
    @Test
    public void testFindAll(){

        List<User> users = userDao.findAll();
        for(User user : users){
            System.out.println(user);
        }
    }

    /**
     * 测试根据id查找用户
     */
    @Test
    public void testFindUserById(){

        User user = userDao.findUserById(42);
        System.out.println(user);
    }

    /**
     * 根据名称模糊查找用户
     */
    @Test
    public void testFindUsersByName(){

        // 在UserDao.xml中的SQL语句没有使用%,要在这里体现
        List<User> users = userDao.findUsersByName("%王%");
        for(User user : users){
            System.out.println(user);
        }
    }

    /**
     * 使用包装类
     * 测试使用QueryVo作为查询条件
     */
    @Test
    public void testFindUserByQueryVo(){

        QueryVo queryVo = new QueryVo();
        User user = new User();
        user.setUsername("%王%");
        queryVo.setUser(user);

        List<User> users = userDao.findUserByVo(queryVo);
        for(User u : users){
            System.out.println(u);
        }
    }

    /**
     * 根据动态条件查询
     */
    @Test
    public void testFindUserByCondition(){

        User user = new User();
        user.setUsername("老王");
        user.setSex("女");  // 如果使用where标签，可以注释这条语句，也不会报错

        List<User> users = userDao.findUserByCondition(user);
        for(User u : users){
            System.out.println(u);
        }
    }

    /**
     * 测试foreach标签的使用
     */
    @Test
    public void testFindUserInIds(){

        QueryVo vo = new QueryVo();
        List<Integer> list = new ArrayList<Integer>();
        list.add(41);
        list.add(42);
        list.add(46);
        vo.setIds(list);

        List<User> users = userDao.findUserInIds(vo);
        for(User user : users){
            System.out.println(user);
        }
    }
}
