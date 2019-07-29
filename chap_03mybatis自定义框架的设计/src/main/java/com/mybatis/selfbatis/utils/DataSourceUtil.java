package com.mybatis.selfbatis.utils;

import com.mybatis.selfbatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author 13967
 * @date 2019/7/29 18:40
 * 用于创建数据源的工具类
 */
public class DataSourceUtil {

    /**
     * 用于获取一个连接
     * @param cfg
     * @return
     */
    public static Connection getConnection(Configuration cfg){

        try{
            Class.forName(cfg.getDriver());
            return DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
