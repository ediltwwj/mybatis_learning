package com.mybatis.dao;

import com.mybatis.domain.QueryVo;
import com.mybatis.domain.User;

import java.util.List;

/**
 * @author 13967
 * @date 2019/7/30 17:27
 */
public interface UserDao {

    /**
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 根据id查询用户信息
     * @param userId
     */
    User findUserById(Integer userId);

    /**
     * 根据名称模糊查找用户信息
     * @param username
     * @return
     */
    List<User> findUsersByName(String username);

    /**
     * 根据queryVo查询中的条件查询用户
     * @param vo
     * @return
     */
    List<User> findUserByVo(QueryVo vo);

    /**
     * 根据传入参数条件
     * @param user 查询的条件,有可能有用户名，有可能有性别...
     * @return
     */
    List<User> findUserByCondition(User user);

    /**
     * 根据queryVo提供的id集合，查询用户信息
     * select * from user where id in {41, 42, 43};
     * @param vo
     * @return
     */
    List<User> findUserInIds(QueryVo vo);
}
