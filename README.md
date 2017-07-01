## Mybatis Generator 的分页查询插件
使用 [MyBatis Generator](https://github.com/mybatis/generator) 可以为项目生成代码.但是默认生成的代码中,没有分页查询的方法和SQL.

本插件可以为生成的 Mapper.java 添加一个分页查询的方法
```
List<实体类> selectByPage(@Param("offset") Long offset, @Param("limit") Long limit);
```

为 Mapper.xml 添加一个分页查询的SQL语句
```
<select id="selectByPage" resultMap="BaseResultMap">
    SELECT <include refid="Base_Column_List" /> FROM 实体类 LIMIT #{offset},#{limit}
</select>
```

### 安装
下载此项目,在此项目根目录下执行以下 Maven 命令安装此项目到本地仓库
```
mvn install
 ```
 
### 使用
在 mybatis-generator-maven-plugin 插件中添加依赖
```
<!-- mybatis generator plugin -->
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.5</version>
    <!-- 解决生成时无法找到JDBC驱动的错误 -->
    <dependencies>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.35</version>
        </dependency>
        
        <!-- 添加自定义的分页代码生成插件 -->
        <dependency>
            <groupId>org.linweiyu</groupId>
            <artifactId>PaginationPlugin</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <!-- 添加自定义的分页代码生成插件 -->
        
    </dependencies>
    <configuration>
        <!-- 是否覆盖 -->
        <overwrite>true</overwrite>
        <!-- 生成时是否输出信息 -->
        <verbose>true</verbose>
    </configuration>
</plugin>
```

在 Mybatis 的代码生成配置文件(例如:generatorConfig.xml)中的 context 元素下配置此插件
```
<plugin type="org.linweiyu.PaginationPlugin"/>
```

在 Mybatis 的代码生成配置文件(例如:generatorConfig.xml)中的 table 元素下通过generate.pagination属性设置当前表是否需要生成分页代码
```
<table schema="seckill" tableName="user" domainObjectName="User"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
               selectByExampleQueryId="false">
<property name="useActualColumnNames" value="false"/>

<!-- 是否生成分页查询代码 -->
<property name="generate.pagination" value="true"/>
<!-- 是否生成分页查询代码 -->

</table>
```

### 结束语
本插件为本人学习 Mybatis 插件开发的成果,个人时间水平有限,所以只是简单地生成了适用于 MySQL 数据库的简单分页查询代码.如果不足或错误之处,请多多包涵.参考此插件,可以生成功能更加强大复杂的代码.Mybatis Generator 插件开发可以参考本人写的[文章](http://www.jianshu.com/p/b96043291b0d).谢谢支持.
