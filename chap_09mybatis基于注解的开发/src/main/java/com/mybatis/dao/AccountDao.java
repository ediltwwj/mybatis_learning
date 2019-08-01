package com.mybatis.dao;

import com.mybatis.domain.Account;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @author 13967
 * @date 2019/8/1 17:47
 */
public interface AccountDao {

    /**
     * 查询所有账户，并且获取每个账户对应所属的用户信息
     * @return
     */
    @Select("select * from account")
    @Results(id = "accountMap", value = {
            @Result(id = true, column = "id", property ="id" ),
            @Result(column = "uid", property = "uid"),
            @Result(column = "money", property = "money"),
            @Result(property = "user", column = "uid",
                    one=@One(select="com.mybatis.dao.UserDao.findUserById", fetchType= FetchType.EAGER))
    })
    List<Account> findAllAccountUser();

    /**
     * 根据用户ID查询所有有关账户
     * @param uid
     * @return
     */
    @Select("select * from account where uid = #{uid}")
    List<Account> findAccountByUid(Integer uid);
}
