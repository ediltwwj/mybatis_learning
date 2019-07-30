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
  在实际开发中，都是越简便越好，所以都是采用不写dao实现类的方式。不管是XML还是注解配置，虽然Mybatis支持这种写法。
  
### 2、自定义mybatis框架  
  + 分析  
    a. 根据配置文件的信息创建Connection对象，可以进行注册驱动，获取连接  
    b. 获取预处理对象PreparedStatement，此时需要SQL语句，即conn.preStatement(sql);  
    c. 执行查询，即ResultSet resultSet = preparedStatement.executeQuery();  
    d. 遍历结果集用于封装  
    ```
        List<E> list = new ArrayList();
        while(resultSet.next()){
            E element = (E)Class.forName(配置的全限定类名).newInstance();
            /*
                封装思路:
                    我们的实体类属性和表中的列名是一致的，于是我们就可以把表的列名看成实体类的属性名称。
                    就可以使用反射的方式来获取每个属性，并把值赋进去。
                    
            */
            
            // 把element添加到list中
            list.add(element);
        }
    ```  
    e. 返回list 
    - 要让上面的方法执行，我们需要给方法提供两个信息  
      a. 连接信息  
      b. 映射信息（包含两部分，组合起来定义成一个对象）  
        + 执行的SQL语句  
        + 封装结果的实体类全限定类名  
    - 最后，根据dao接口字节码创建dao的代理对象，并调用上诉方法  
    ```
        // 4、使用SqlSession创建Dao接口的代理对象  
        UserDao userDao = session.getMapper(UserDao.class);
        
        // 根据dao接口的字节码创建dao的代理对象  
        public <T> T getMapper(Class<T> daoInterfaceClass){
            /**
                类加载器: 它使用的和被代理对象是相同的类加载器;
                代理对象要实现的接口: 和被代理对象实现相同的接口;  
                如何代理: 它就是增强的方法，我们需要自己来提供,
                        此处是一个InvocationHandler的接口，我们需要写一个该接口的实现类，
                        在实现类中调用selectList方法
            */
            Proxy.newProxyInstance(类加载器，代理对象要实现的接口字节码数组，如何代理);
        }
    ```
    [思路解析,点击此处](https://www.cnblogs.com/622-yzl/p/11003636.html)  
    
### 3、mybatis的crud单表操作  
  + 基本步骤  
    - 第一步: 编写dao接口(抽象方法)  
    - 第二步: 编写dao接口的映射文件  
    - 第三步: 调用dao接口的方法  
    ```
        // 查看chap_03的源代码
        // 1、编写dao接口抽象方法(UserDao.java)  
        public interface UserDao{
            User findUserById(Integer userId);
        }
        // 2、编写方法对应的映射(UserDao.xml)
        <select id="findUserById" parameterType="java.lang.Integer" resultType="com.mybatis.domain.User">
            select * from user where id=#{userid};
        </select>
        // 3、调用crud方法
        public void testFindUserById(){
    
            User user = userDao.findUserById(42);
            System.out.println(user);
        }   
    ```  
  + 模糊查询    
    - 第一种 : 采用PreparedStatement的参数占位符  
    ``` 
        <select id="findUserByName" parameterType="string" resultType="com.mybatis.domain.User">
            select * from user where username like #{value}
        </select>
    ```
    ```
        List<User> users = userDao.findUsersByName("%王%");       
    ```  
    - 第二种 : 采用Statement对象的字符串拼接  
    ```
         <select id="findUserByName" parameterType="string" resultType="com.mybatis.domain.User">
             select * from user where username like '%${value}%'
         </select>
    ```
    ```
         List<User> userList = userDao.findUserByName("%王%");
    ```
    推荐使用第二种方式  
  + 使用实体包装类作为参数   
  ```
      <select id="findUserByVo" parameterType="com.mybatis.domain.QueryVo" resultType="com.mybatis.domain.User">
          <!-- {username}被认为是QueryVo中的属性，显然找不到 -->
          <!-- select * from user where username like #{username}; -->
          <!-- {user.username}的user是QueryVo中的属性 -->
          select * from user where username like #{user.username};
      </select>  
  ```  
  ```
      public class QueryVo {
      
          private User user;
      
          public User getUser() {
              return user;
          }
      
          public void setUser(User user) {
              this.user = user;
          }
      }
  ```  
  + 获取保存记录当中自增长的id  
  ```
      <!-- 保存用户,id自增长 -->
      <insert id="saveUser" parameterType="com.mybatis.domain.User">
          <!-- 配置插入操作后，获取插入数据对应实体类的id名称 -->
          <!-- keyProperty对应实体类的Id，keyColumn对应数据库Id，order执行时机 -->
          <selectKey keyProperty="id" keyColumn="id" resultType="int" order="AFTER">
              select last_insert_id();
          </selectKey>
          insert into user(username, birthday, sex, address) values(#{username},#{birthday},#{sex},#{address});
      </insert>
  ```
  + 关于实体类属性和数据库列名不一致的解决方案  
    - 第一种 : 通过使用别名，在SQL语句层面解决问题  
    ```
        // 运行效率高，开发效率慢
        select username as userName from user;
    ```  
    - 第二种 : 使用配置文件  
    ```
        // 由于多解析resultMap，导致运行效率慢，但开发效率高  
         <!-- 配置查询结果的列名和实体类的属性名的对应关系 -->
         <!-- id是配置映射关系的标识，type是说明属于哪个实体类的映射 -->
         <resultMap id="userMap" type="com.mybatis.domain.User">
             <!-- 主键字段的对应，其中property为实体类中的属性名，column为数据库中的列名 -->
             <id property="userId" column="id"/>
             <!-- 非主键字段的对应 -->
             <result property="userName" column="username"/>
             <result property="userAddress" column="address"/>
             <result property="userSex" column="sex"/>
             <result property="userBirthday" column="birthday"/>
         </resultMap>
         
         <!-- 注意：使用映射配置后，需要改一下select标签中的原属性resultType为resultMap，值是配置映射的id -->
         <select id="findAll" resultMap="userMap">
             select * from user
         </select>
    ```  
    