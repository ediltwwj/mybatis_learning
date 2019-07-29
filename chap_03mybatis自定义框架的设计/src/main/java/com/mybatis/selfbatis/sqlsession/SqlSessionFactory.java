package com.mybatis.selfbatis.sqlsession;

/**
 * @author 13967
 * @date 2019/7/29 17:18
 */
public interface SqlSessionFactory {

    /**
     * 用于打开一个新的SqlSession对象
     * @return
     */
    SqlSession openSession();
}
