package com.mybatis.test;

//import com.mybatis.dao.UserDao;
//import com.mybatis.domain.User;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import java.io.InputStream;
//import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 16:03
 */
// 此段代码和chap_02代码一样，只能用在是实体类属性和表的字段一致的情况
//public class AnnoTest {
//
//    /**
//     * 测试基于注解的mybatis环境搭建
//     * @param args
//     */
//    public static void main(String[] args) throws Exception{
//
//        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
//        SqlSession session = factory.openSession();
//        UserDao userDao = session.getMapper(UserDao.class);
//
//        List<User> users = userDao.findAllUsers();
//        for(User user : users){
//            System.out.println(user);
//        }
//
//        session.close();
//        in.close();
//    }
//}
