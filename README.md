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
  + 标签扩展  
    - properties  
      + 可以将配置信息单独写在一个文件中(jdbcConfig.properties)  
      + 引用该配置文件(resource引用或者url引用)  
      ```
      <configuration>
          <!-- 配置properties
               可以在标签内部配置连接数据库的信息，也可以通过属性引用外部配置文件信息(jdbcConfig.properties)
               resource属性（常用） : 用于指定配置文件的位置，是按照类路径的写法来写，并且必须存在类路径下。
               url属性 : 是要求按照url的写法来写地址。
                        URL：Uniform Resource Locator 统一资源定位符。它是可以唯一标识一个资源的位置。
                        它的写法:
                            http://localhost:8080/mybatisserver/demo1Servlet
                             协议      主机     端口       URI
                        URI:Uniform Resource Identifier 统一资源标识符。它是在应用中可以唯一定位一个资源的。
          -->
          <!-- resource路径这样写，则属性要和配置文件中的前缀一致-->
          <properties resource="jdbcConfig.properties"></properties>
          <!-- url方式 -->
          <!-- <properties url="file:///D:/mybatis_learning/chap_04%E4%BD%BF%E7%94%A8mybatis%E8%BF%9B%E8%A1%8CCURD/
                src/main/resources/jdbcConfig.properties"/> -->
      
          <!-- 配置环境 -->
          <environments default="mysql">
              <environment id="mysql">
                  <transactionManager type="JDBC"></transactionManager>
                  <dataSource type="POOLED">
                      <property name="driver" value="${jdbc.driver}"/>
                      <property name="url" value="${jdbc.url}"/>
                      <property name="username" value="${jdbc.username}"/>
                      <property name="password" value="${jdbc.password}"/>
                  </dataSource>
              </environment>
          </environments>
      
          <mappers>
              <mapper resource="com/mybatis/dao/UserDao.xml"></mapper>
          </mappers>
      </configuration>  
      ```  
      外部文件（jdbcConfig.properties）  
      ```
      jdbc.driver = com.mysql.jdbc.Driver
      jdbc.url = jdbc:mysql://localhost:3306/mybatis_01
      jdbc.username = root
      jdbc.password = 123456
      ```
    - typeAliases  
    ```
      <!-- typeAlias用于配置别名,它只能配置domain中类的别名 -->
      <typeAliases>
          <!-- typeAlias用于配置别名,type属性指定的是实体类全限定类名,alias属性当指定别名就不再区分大小写 -->
          <!-- <typeAlias type="com.mybatis.domain.User" alias="user"></typeAlias> -->
  
          <!-- 用于指定配置别名的包，当指定之后，该包下的实体类都会注册别名，且不再区分大小写 -->
          <package name="com.mybatis.domain"></package>
      </typeAliases>    
    ```
    ```
      // 未配置别名前  
      <update id="updateUser" parameterType="com.mybatis.domain.User">
            update user set username=#{username}, address=#{address}, sex=#{sex}, birthday=#{birthday} where id=#{id};
        </update>
    ```
    ```
      // 配置别名后
      <update id="updateUser" parameterType="user">
            update user set username=#{username}, address=#{address}, sex=#{sex}, birthday=#{birthday} where id=#{id};
        </update>
    ```  
    - mappers标签中的package  
    ```
    <mappers>
        <!-- <mapper resource="com/mybatis/dao/UserDao.xml"></mapper> -->
        <!-- package标签是用于指定dao接口所在的包，当指定之后就不需要再写mapper以及resource或者class了 -->
        <package name="com.mybatis.dao"></package>
    </mappers>
    ```  

### 4、mybatis的连接池以及事务原理  
  + 连接池  
    - 连接池就是用于存储连接的一个容器  
    - 容器就是集合对象，必须是线程安全，不能两个线程拿到同一连接，且必须实现队列特性（FIFO）   
    - 使用完连接，重新放回池中，放在最后  
    - 可以减少我们获取连接的次数，也就减少连接时间  
    - mybatis连接池提供了3种方式的配置:  
      + 配置的位置:  
        主配置文件SqlMapConfig.xml的dataSource标签,type属性就是表示采用何种连接池方式  
      + type属性的取值:  
        a. POOLED 使用连接池，从池中获取一个连接  
        b. UNPOOLED 不使用连接池，每次创建一个新的连接  
        c. JNDI  
    当需要连接的时候，去空闲池里面找有没有空闲的。有的话直接使用，没有的话去活动线程池中查看是否已经达到线程数上上限。  
    如果上限了，从活动线程池中取出最先使用的那个，进行清理操作，然后返回该连接。如果没上限，则创建一个新的连接。  
  + 事务原理  
    - 通过SqlSession的commit()和rollback()方法进行事务提交和回滚  
    - 自动提交事务，设置参数为true，sqlSession.openSession(true);  
    - 设置自动提交事务，适用于一次操作，不适合类似转账这种操作  
    
### 5、mybatis的动态Sql语句  
  + 单表多条件查询  
    - if标签  
    ```
        <!-- 根据条件查询用户 条件很多的时候很繁琐 1=1表示后面所有条件都要为真 -->
        <select id="findUserByCondition" parameterType="com.mybatis.domain.User" resultType="com.mybatis.domain.User">
            select * from user where 1=1
            <if test="username != null">
                and username = #{username}
            </if>
            <if test="sex != null">
                and sex = #{sex}
            </if>
        </select>    
    ```  
    - where标签嵌套if标签  
    ```
        <!-- 嵌套使用，if标签中的条件允许为假 -->
        <select id="findUserByCondition" parameterType="com.mybatis.domain.User" resultType="com.mybatis.domain.User">
            select * from user
            <where>
                <if test="username != null">
                    and username = #{username}
                </if>
                <if test="sex != null">
                    and sex = #{sex}
                </if>
            </where>
        </select>
    ```  
    - foreach标签  
    ```
        // 对应sql语句select * from user where id in {41, 42, 46};
        <!-- 根据queryVo中的id集合实现查询用户列表 -->
        <select id="findUserInIds" parameterType="com.mybatis.domain.QueryVo" resultType="com.mybatis.domain.User">
            select * from user
            <where>
                <if test="ids != null and ids.size()>0">
                    <!-- 从collection里遍历，每个对象为item,并用separator分隔，最后放在open和close之间 -->
                    <foreach collection="ids" open="and id in (" close=")" item="id" separator=",">
                        #{id} <!-- #{}和item属性对应-->
                    </foreach>
                </if>
            </where>
        </select>
    ```  
    - sql标签
    ```
        <!-- 了解的内容，用于抽取重复的Sql语句 -->
        <sql id="defaultUser">
            select * from user
        </sql>
    
        <!-- 使用include去实现代替 -->
        <select id="findAll" resultType="com.mybatis.domain.User">
            <include refid="defaultUser"></include>
        </select>
    ```
    
### 6、mybatis的多表查询  
```
    // User类，即主表
    public class User implements Serializable {
    
        private Integer id;
        private String username;
        private Date birthday;
        private String sex;
        private String address;
    
        // 一对多关系映射,主表实体应该包含从表实体的集合引用
        private List<Account> accounts;
    }
```
```
    // Account类，即从表
    public class Account implements Serializable {
    
        private Integer id;
        private Integer uid;
        private Double money;
    
        // 从表实体应该包含一个主表实体的对象引用
        // 以此体现一对多和多对一关系
        private User user;
    }
```
  + 一对一,多对一  
  ```
      <!-- 定义封装account和user的resultMap, type表示要映射的实体 -->
      <!-- column表示数据表主键字段或者查询语句的别名,property表示pojo对象的主键属性 -->
      <resultMap id="accountUserMap" type="account">
          <!-- 只能保证Account实体封装完成 -->
          <id property="id" column="aid"></id>
          <result property="uid" column="uid"></result>
          <result property="money" column="money"></result>
          <!-- 一对一关系映射，配置封装user的内容,javaTupe表示关联的实体 -->
          <!-- property表示pojo的一个对象属性" javaType="上面pojo关联的pojo对象 -->
          <association property="user" javaType="com.mybatis.domain.User">
              <id property="id" column="id"></id>
              <result property="username" column="username"></result>
              <result property="address" column="address"></result>
              <result property="sex" column="sex"></result>
              <result property="birthday" column="birthday"></result>
          </association>
      </resultMap>
      
      <select id="findAllAccounts" resultMap="accountUserMap">
          select u.*, a.id as aid, a.uid, a.money from account a, user u where u.id = a.uid
      </select>   
  ```
  + 一对多  
  ```
      <!-- 定义User的resultMap-->
      <resultMap id="userAccountMap" type="user">
          <id property="id" column="id"></id>
          <result property="username" column="username"></result>
          <result property="address" column="address"></result>
          <result property="sex" column="sex"></result>
          <result property="birthday" column="birthday"></result>
          <!-- 配置user对象中accounts集合的映射 -->
          <!-- collection中的property表示实体类的集合属性,ofType表示集合中的对象 -->
          <collection property="accounts" ofType="account">
              <id property="id" column="aid"></id>
              <result property="uid" column="uid"></result>
              <result property="money" column="money"></result>
          </collection>
      </resultMap>
      
      <select id="findAllUsers" resultMap="userAccountMap">
          select u.*, a.id as aid, a.uid, a.money from user u left outer join account a on u.id = a.uid
      </select>    
  ```
  + 多对多  
    两张表需要有个中间表,包含各自主键  
    两个实体类需要各自包含一个对方的集合引用  
    - 角色->用户的多对多,反之同理  
  ```
      // User类
      public class User implements Serializable {
      
          private Integer id;
          private String username;
          private Date birthday;
          private String sex;
          private String address;
      
          private List<Account> accounts;
      }  
  ```
  ```
      // Role类
      public class Role implements Serializable {
        
        private Integer roleId;
        private String roleName;
        private String roleDesc;
        
        private List<User> users;
      }
  ```
  ```
      <!-- 定义role表的resultMap -->
      <resultMap id="roleMap" type="role">
          <id property="roleId" column="rid"></id>
          <result property="roleName" column="role_name"></result>
          <result property="roleDesc" column="role_desc"></result>
          <collection property="users" ofType="user">
              <id property="id" column=""></id>
              <result property="username" column="username"></result>
              <result property="sex" column="sex"></result>
              <result property="address" column="address"></result>
              <result property="birthday" column="birthday"></result>
          </collection>
      </resultMap>
  
      <!-- 查询所有角色对应的每个用户 -->
      <select id="findAllRole" resultMap="roleMap">
          <!-- mysql不支持全外连接 -->
          select u.*, r.id as rid, r.role_name, r.role_desc from role r 
           left outer join user_role ur on r.id = ur.rid 
            left outer join user u on u.id = ur.uid
      </select>
  ```
  
### 7、mybatis的加载  
  + 立即加载  
    - 多表查询的时候，如果在SQL语句中直接使用多表连接就是立即加载  
    - 适用于一对一和多对一(一对一是通常情况下立即加载)  
  + 延迟加载  
    - 适用于一对多和多对多  
    - 先从单表查询、需要时再从关联表去关联查询，大大提高 数据库性能，因为查询单表要比关联查询多张表速度要快
    - 使用时，需在SqlMapConfig.xml中配置来开启延迟加载  
    ```
    <!-- 配置参数 -->
    <settings>
        <!-- 开启mybatis支持延迟加载 -->
        <!-- mybatis version > 3.4.1 时,aggressiveLazyLoading可以不设置 -->
        <setting name="lazyLoadingEnabled" value="true"/>
        <setting name="aggressiveLazyLoading" value="false"></setting>
    </settings>  
    ```
    - 一对一实现延迟加载  
    AccountDao.xml  
    ```
    <mapper namespace="com.mybatis.dao.AccountDao">
    
        <!-- 定义封装account和user的resultMap, type表示要映射的实体 -->
        <resultMap id="accountUserMap" type="account">
            <id property="id" column="id"></id>
            <result property="uid" column="uid"></result>
            <result property="money" column="money"></result>
            <!-- select属性属性指定的内容，就是查询用户的唯一标识
                 column在此处必须写，是用户根据id查询时，所需要参数的值，来自account-->
            <association property="user" column="uid" javaType="user" select="com.mybatis.dao.UserDao.findUserById"></association>
        </resultMap>
    
        <!-- 查询所有 -->
        <select id="findAllAccounts" resultMap="accountUserMap">
            select * from account
        </select>
    
        <!-- 根据用户ID查询账户信息 -->
        <select id="findAccountByUid" resultType="account">
            select * from account where uid = #{uid}
        </select>
    </mapper>
    ```
    - 一对多实现延迟加载  
    UserDao.xml  
    ```
    <mapper namespace="com.mybatis.dao.UserDao">
    
        <!-- 定义User的resultMap-->
        <resultMap id="userAccountMap" type="user">
            <id property="id" column="id"></id>
            <result property="username" column="username"></result>
            <result property="address" column="address"></result>
            <result property="sex" column="sex"></result>
            <result property="birthday" column="birthday"></result>
            <!-- 配置user对象中accounts集合的映射 -->
            <collection property="accounts" ofType="account" select="com.mybatis.dao.AccountDao.findAccountByUid" column="id">
            </collection>
        </resultMap>
    
        <!-- 查询所有用户以及对应的账户 -->
        <select id="findAllUsers" resultMap="userAccountMap">
            select * from user
        </select>
    
        <!-- 根据id查询用户 -->
        <select id="findUserById" parameterType="java.lang.Integer" resultType="com.mybatis.domain.User">
            select * from user where id=#{userid};
        </select>
    
    </mapper>
    ```  
   
### 8、mybatis的缓存  
  + 使用缓存的好处  
    - MyBatis拥有自己的缓存结构，减少与数据库的交互次数，提高执行效率  
  + 什么时候使用缓存  
    - 经常查询并且不经常改变的 
    - 数据的正确与否对最终结果影响不大(即同步与否影响不大)  
  + 一级缓存  
    - 是SqlSession对象的缓存，默认支持，无需配置  
    - 缓存数据存放在HashMap区域，不同SqlSession互不影响  
    - 第一次执行查询写入内存，后面查询直接从内存拿数据  
    - 可以使用SqlSession的close或者clearCache方法清空缓存  
    - 更新，删除等操作也会触发清空缓存  
    
  
    
        
        
        
      
    