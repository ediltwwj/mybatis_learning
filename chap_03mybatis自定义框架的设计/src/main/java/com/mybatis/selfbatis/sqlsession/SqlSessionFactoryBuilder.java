package com.mybatis.selfbatis.sqlsession;

import com.mybatis.selfbatis.cfg.Configuration;
import com.mybatis.selfbatis.sqlsession.defaults.DefaultSqlSessionFactory;
import com.mybatis.selfbatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * @author 13967
 * @date 2019/7/29 17:14
 * 用于创建一个SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {

    /**
     * 根据参数字节输入流来构建一个SqlSessionFactory工厂
     * @param config
     * @return
     */
    public SqlSessionFactory build(InputStream config){

        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);
        return new DefaultSqlSessionFactory(cfg);
    }
}
