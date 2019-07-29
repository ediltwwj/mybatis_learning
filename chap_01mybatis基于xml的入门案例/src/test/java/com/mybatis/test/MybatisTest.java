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
        /** 1、读取配置文件
         *  不推荐使用绝对路径和相对路径，在服务器部署容易出问题
         *  推荐使用:
         *      （1）使用类加载器，它只能读取类路径的配置文件
         *      （2）使用ServletContext对象的getRealPath()方法
         */
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        /** 2、创建SqlSessionFactor工厂
         *  使用了构建者模式,把对象的创建细节隐藏，让使用者直接调用方法即可拿到对象
         */
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        /** 3、使用工厂生产SqlSession对象
         *  使用了工厂模式，优势是解耦（降低类之间的依赖关系）
         */
        SqlSession session = factory.openSession();
        /** 4、使用SqlSession创建Dao接口的代理对象
         *  使用了代理模式，优势是在不修改代码的基础上对已有方法增强
         */
        UserDao userDao = session.getMapper(UserDao.class);
        /**
         *  1-4 等同于InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml).getMapper(UserDao.class);
         *  分开写更加灵活
         */
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
