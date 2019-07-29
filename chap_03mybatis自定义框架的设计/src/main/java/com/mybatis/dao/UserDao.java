package com.mybatis.dao;

import com.mybatis.domain.User;
import com.mybatis.selfbatis.annotations.Select;

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
    @Select("select * from user")  // 使用XML的话这句话删掉，且不需要annotations.Select注解
    List<User> findAll();
}
