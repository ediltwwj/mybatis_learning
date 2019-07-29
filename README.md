# Mybatis学习笔记   
## 目录   

[toc] 后续生成目录

### 1、mybatis入门  
  + mybatis的环境搭建   
    - 第一步: 创建maven工程并导入依赖（主要是mybatis和mysql）  
    - 第二步: 创建实体类和dao的接口  
    - 第三步: 创建mybatis的主配置文件通常命名为SqlMapConfig.xml）    
    a. mysql环境配置   
    b. 指定映射文件配置  
    - 第四步: 创建映射配置文件（取名UserDao.xml为了和接口类名保持一致，取UserMapper.xml也是可以的   
    a. mybatis的映射配置文件位置必须和dao接口的包结构相同  
    b. 映射配置文件的mapper标签中namespace属性的取值必须是dao接口的全限定类名  
    c. 映射配置文件的操作配置（select），id属性的取值必须是dao接口的方法名  
    d. 遵循以上三点，后续开发就无须再写dao的实现类  
  + mybatis的入门案例  
    - 第一步: 读取配置文件（SqlMapConfig.xml）  
    - 第二步: 创建SqlSessionFactory工厂  
    - 第三步: 创建SqlSession  
    - 第四步: 创建Dao接口的代理对象  
    - 第五步: 执行dao中的方法  
    - 第六步: 释放资源  
    - **基于XML的注意事项**  
      a. 不要忘记在映射配置文件中告知mybatis要将结果封装到哪个实体类(resultType)  
      b. 配置的方式: 指定实体类的全限定类名  
    - **基于注解的注意事项**  
      a. 把UserDao.xml移除，在dao接口的方法上使用@Select注解，并指定SQL语句  
      b. 同时需要在SqlMapConfig.xml中的mapper配置时，使用class属性指定dao接口的全限定类名  
  ```
    <!-- xml -->
    <select id="findAll" resultType="com.mybatis.domain.User">  
    <mapper resource="com/mybatis/dao/UserDao.xml"/> 
    
    <!-- annotations -->
    @Select("select * from user")
    List<User> findAll();
    <mapper class="com.mybatis.dao.UserDao"/>
  ```
  
    
      
        
      
      
        
  