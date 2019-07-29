package com.mybatis.test;

import com.mybatis.dao.UserDao;
import com.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author 13967
 * @date 2019/7/29 10:09
 *
 * mybatis的入门案例
 */
public class MybatisTest {

    /**
     * 入门案例
     * @param args
     */
    public static void main(String[] args)throws IOException {
        // 1、读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 2、创建SqlSessionFactor工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        // 3、使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        // 4、使用SqlSession创建Dao接口的代理对象
        UserDao userDao = session.getMapper(UserDao.class);
        // 5、使用代理对象执行方法
        List<User> users = userDao.findAll();
        for(User user : users){
            System.out.println(user);
        }
        // 6、释放资源
        session.close();
        in.close();
    }
}
