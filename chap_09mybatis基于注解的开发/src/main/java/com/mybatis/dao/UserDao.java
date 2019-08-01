package com.mybatis.dao;

import com.mybatis.domain.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 15:50
 */
public interface UserDao {

    /**
     * 查询所有用户,并获得该用户的所有账户信息
     * @return
     */
    @Select("select * from user")
    @Results(id = "userMap", value={
            // id设为true表示主键,默认为false
            @Result(id=true, column = "id", property = "userId"),
            @Result(column = "username", property = "userName"),
            @Result(column = "sex", property = "userSex"),
            @Result(column = "address", property = "userAddress"),
            @Result(column = "birthday", property = "userBirthday"),
            @Result(property = "accounts", column = "id",
                    many=@Many(select = "com.mybatis.dao.AccountDao.findAccountByUid", fetchType = FetchType.LAZY))
    })
    List<User> findAllUsers();

    /**
     * 根据id查找单个用户
     * @param id
     * @return
     */
    @Select("select * from user where id=#{id}")
    @ResultMap(value = {"userMap"})
    User findUserById(Integer id);
}



// 此段代码和chap_02代码一样，只能用在是实体类属性和表的字段一致的情况
//public interface UserDao {
//
//    /**
//     * 查询所有用户
//     * @return
//     */
//    @Select(value = "select * from user")
//    List<User> findAllUsers();
//
//    /**
//     * 保存用户
//     * @param user
//     */
//    @Insert(value = "insert into user(username, address, sex, birthday) values(#{username}, #{address}, #{sex}, #{birthday})")
//    void saveUser(User user);
//
//    /**
//     * 更新用户
//     * @param user
//     */
//    @Update("update user set username=#{username}, sex=#{sex}, address=#{address}, birthday=#{birthday} where id=#{id}")
//    void updateUser(User user);
//}

