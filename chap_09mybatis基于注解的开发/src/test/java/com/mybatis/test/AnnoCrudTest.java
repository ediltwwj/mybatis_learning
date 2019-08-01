package com.mybatis.test;

//import com.mybatis.dao.UserDao;
//import com.mybatis.domain.User;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;

/**
 * @author 13967
 * @date 2019/8/1 16:29
 * 单表Crud的注解测试
 */
// 此段代码和chap_02代码一样，只能用在是实体类属性和表的字段一致的情况
//public class AnnoCrudTest {
//
//    private InputStream in;
//    private SqlSessionFactory factory;
//    private SqlSession session;
//    private UserDao userDao;
//
//    @Before
//    public void init() throws IOException {
//
//        in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        factory = new SqlSessionFactoryBuilder().build(in);
//        session = factory.openSession();
//        userDao = session.getMapper(UserDao.class);
//    }
//
//    @After
//    public void destory() throws IOException{
//
//        session.commit();
//        session.close();
//        in.close();
//    }
//
//    @Test
//    public void testSaveUser(){
//
//        User user = new User();
//        user.setUsername("小吴");
//        user.setAddress("福州市台江区");
//        user.setSex("女");
//        user.setBirthday(new Date());
//
//        userDao.saveUser(user);
//    }
//
//    @Test
//    public void testUpdateUser(){
//
//        User user = new User();
//        user.setId(55);
//        user.setUsername("小吴");
//        user.setAddress("福州市马尾区");
//        user.setSex("女");
//        user.setBirthday(new Date());
//
//        userDao.updateUser(user);
//    }
//}
