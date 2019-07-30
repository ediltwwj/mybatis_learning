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
     * 保存用户
     * @param user
     */
    void saveUser(User user);


    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据id删除用户
     * @param userId
     */
    void deleteUserById(Integer userId);

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
     * 查询总用户数
     * @return
     */
    int findTotalUserNumber();

    /**
     * 根据queryVo查询中的条件查询用户
     * @param vo
     * @return
     */
    List<User> findUserByVo(QueryVo vo);
}
