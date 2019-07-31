package com.mybatis.domain;

import java.io.Serializable;

/**
 * @author 13967
 * @date 2019/7/31 14:51
 */
public class Account implements Serializable {

    private Integer id;
    private Integer uid;
    private Double money;

    // 从表实体应该包含一个主表实体的对象引用
    // 以此体现一对多和多对一关系
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", uid=" + uid +
                ", money=" + money +
                '}';
    }
}
