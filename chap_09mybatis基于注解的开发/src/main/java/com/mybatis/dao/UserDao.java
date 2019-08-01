package com.mybatis.dao;

import com.mybatis.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 15:50
 */
public interface UserDao {

    /**
     * 查询所有用户
     * @return
     */
    @Select(value="select * from user")
    List<User> findAllUsers();
}
