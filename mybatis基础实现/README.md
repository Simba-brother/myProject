# Mybatis基础的自我实现
看MybatisTest.java中

##配置文件开发
首先需要一个总的mybatis的xml配置。该配置里面有数据库连接信息（驱动，url, username, password）以及 映射配置（resource 包中）----1
在Resources 中提供一个静态的getResourceAsStream 度取这个主配置文件 返回一个InputStream。

写一个SqlSessionFactoryBuilder类 里面有一个build(InputStream config)方法， 方法中通过工具类XMLConfigBuilder的静态方法loadConfiguration去解析这个流，封装成Configuration 对象（driver, url, usename, password, Map<String, Mapper>）其中Mapper 有属性(sqlString, resultType) 这两个属性是从----1中解析出来的。

现在有了Configuration  cfg 对象我们就要在这个build方法中  new DefaultSqlSessionFactory(cfg)  -------到此位置就是qlSessionFactoryBuilder 构建出DefaultSqlSessionFactory这个对象，该工厂对象接收cfg 对象 在openSession()方法中生产DefaultSqlSession（一个cfg属性，一个connection属性） 其中connection属性 数工具类DataSourceUtil，根据cfg,封装的url, driver, username, password信息 用DriverManager.getConnection(....) 得到的。有了session 对象 通过示例方法getMapper(Class<T> daoInterfaceClass),生成一个代理对象。执行new Executor().selectList(mapper,conn);——>这里面就根据mapper.getResultType()获得的全类名进行反射得到实例，根据mapper.getQueryString().得到sql,然后查询数据库封装即可
##注解开发
需要自己定义一个Select 注解，应用到IUserDAO 的 findAll方法上， XMLConfigBuilder也是去封装mapper。只要Configuration 封装好了，其他和上面一样了



