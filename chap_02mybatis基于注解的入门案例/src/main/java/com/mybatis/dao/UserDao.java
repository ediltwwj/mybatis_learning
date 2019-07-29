package com.mybatis.dao;

import com.mybatis.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 13967
 * @date 2019/7/28 18:06
 *
 * 用户的持久层接口
 */
public interface UserDao {

    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from user")
    List<User> findAll();
}
