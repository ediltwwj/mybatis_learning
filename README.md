# Mybatis学习笔记   
## 目录   

[TOC] 

### 1、mybatis入门  
  + mybatis的环境搭建   
    - 第一步: 创建maven工程并导入依赖（主要是mybatis和mysql） 
    - 第二步: 创建实体类和dao的接口  
    - 第三步: 创建mybatis的主配置文件通常命名为SqlMapConfig.xml）
    a.mysql环境配置  
    b.指定映射文件配置  
    - 第四步: 创建映射配置文件（取名UserDao.xml为了和接口类名保持一致，取UserMapper.xml也是可以的  
    a. mybatis的映射配置文件位置必须和dao接口的包结构相同  
    b. 映射配置文件的mapper标签中namespace属性的取值必须是dao接口的全限定类名  
    c. 映射配置文件的操作配置（select），id属性的取值必须是dao接口的方法名  
    d. 遵循以上三点，后续开发就无须再写dao的实现类  
  