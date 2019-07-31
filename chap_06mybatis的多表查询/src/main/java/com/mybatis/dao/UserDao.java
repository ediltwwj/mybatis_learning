package com.mybatis.dao;

import com.mybatis.domain.User;

import java.util.List;

/**
 * @author 13967
 * @date 2019/7/30 17:27
 */
public interface UserDao {

    /**
     * 查询所有,同时获取到用户下所有账号
     * @return
     */
    List<User> findAllUsers();
}
