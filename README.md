# Foo-Generator代码生成器

​		本项目是基于Mybatis和MybatisPlus的代码生成器，可以自动快速生成单表，多表的controller，service，mapper，po，dto，vo等文件。


## 第一章 使用方法

### 1.1 通过DDL生成单表

- 请求示例

  ![](https://img1.imgtp.com/2023/07/26/YMkAcmq3.jpg)

  需要说明：

  1、file传你从navicat导出的ddl建表语句即可，请确保ddl格式的规范性，建议直接使用导出的即可，不要修改。

  2、pachageName传你的项目包名即可

### 1.2 通过连接数据库生成多表(推荐)

- 请求示例

  ![](https://img1.imgtp.com/2023/07/26/T6YheZRy.jpg)

- json格式

  ```json
  {
      "packageName":"com.foogui.gen.code",
      "prefix":"t_",
      "projectName":"gen-code-vq",
      "author":"Foogui",
      "host": "localhost",
      "port": "3308",
      "dbname": "otd",
      "username": "root",
      "password": "root",
      "tableNames":["t_purchase_relation","t_risk_info"],
      "frameType": "Mybatis"
  }
  ```

  需要说明：

  1、以上参数除了author和projectName，其他**必须传递**
  2、frameType只可以传递Mybatis或者MybatisPlus
