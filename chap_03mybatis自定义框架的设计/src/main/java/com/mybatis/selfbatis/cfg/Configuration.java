package com.mybatis.selfbatis.cfg;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 13967
 * @date 2019/7/29 17:48
 * 自定义Mybatis的配置类
 */
public class Configuration {

    private String driver;
    private String url;
    private String username;
    private String password;

    private Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    public Map<String, Mapper> getMappers() {
        return mappers;
    }

    public void setMappers(Map<String, Mapper> mappers) {
        // 直接赋值，当mappers当中有多个mapper的时候，会被覆盖，永远只有一个mapper
        // this.mappers = mappers;
        // 使用追加以添加全部mapper
        this.mappers.putAll(mappers);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
