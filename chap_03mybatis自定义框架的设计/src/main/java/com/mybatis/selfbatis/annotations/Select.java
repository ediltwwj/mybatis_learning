package com.mybatis.selfbatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询的注解
 */
// 保留时间，运行期
@Retention(RetentionPolicy.RUNTIME)
// 作用于方法
@Target(ElementType.METHOD)
public @interface Select {

    /**
     * 配置SQL语句
     * @return
     */
    String value();
}
