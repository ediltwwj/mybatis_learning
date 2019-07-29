package com.mybatis.selfbatis.sqlsession.defaults;

import com.mybatis.selfbatis.cfg.Configuration;
import com.mybatis.selfbatis.cfg.Mapper;
import com.mybatis.selfbatis.sqlsession.SqlSession;
import com.mybatis.selfbatis.sqlsession.proxy.MapperProxy;
import com.mybatis.selfbatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author 13967
 * @date 2019/7/29 18:13
 * SqlSession接口的实现
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration cfg;
    private Connection connection;

    public DefaultSqlSession(Configuration cfg){
        this.cfg = cfg;
        connection = DataSourceUtil.getConnection(cfg);
    }

    /**
     * 用于创建代理对象
     * @param daoInterfaceClass dao的接口字节码
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T)Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass}, new MapperProxy(cfg.getMappers(), connection));
    }

    /**
     * 用于释放资源
     */
    public void close() {
        if(connection != null){
            try{
                connection.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
