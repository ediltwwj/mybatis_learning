package com.mybatis.selfbatis.sqlsession.defaults;

import com.mybatis.selfbatis.cfg.Configuration;
import com.mybatis.selfbatis.sqlsession.SqlSession;
import com.mybatis.selfbatis.sqlsession.SqlSessionFactory;

/**
 * @author 13967
 * @date 2019/7/29 18:08
 * SqlSessionFactory接口的实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration cfg;

    public DefaultSqlSessionFactory(Configuration cfg){
        this.cfg = cfg;
    }

    /**
     * 用于创建一个新的数据库对象
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}
