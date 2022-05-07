# paimon-sdk


欢迎使用 Paimon SDK for Java 。

Paimon SDK for Java 提供的功能操作接口进行了一层封装，让您不用复杂编程即可访问 Paimon 区块链各项能力，SDK可以自动帮您满足能力调用过程中所需的证书校验、加签、验签、发送HTTP请求等非功能性要求及提供了包括对资产类型（schema，也称文档类型，相当于表结构）、资产（document，也称文档，相当于表中的一条数据）、智能合约（contract）等功能性方法调用。

这里向您介绍如何获取 Paimon SDK for Java 并开始调用。


## 环境要求

1. Paimon SDK for Java 需要配合`JKD 1.8`或其以上版本。
2. 使用 Paimon SDK for Java 之前 ，您需要先获取 Paimon 的“链应用”接入的权限及其访问令牌。 
3. 准备工作完成后，注意保存如下信息，后续将作为使用 SDK 的输入。
 - `Paimon 网关地址`、`令牌 ID`、`令牌私钥`

## 安装依赖
推荐通过 Maven 来管理项目依赖，您只需在项目的`pom.xml`文件中声明如下依赖
```xml
<dependency>
    <groupId>com.dreamkey.paimon</groupId>
    <artifactId>paimon-sdk-java</artifactId>
    <version>1.0</version>
</dependency>
```


## 目录说明
paimon-sdk-java

- api：在 application 基础上的一层处理，提供给开发调用的便捷方法
- application：分不同类对接 Paimon 各模块操作功能，原封返回 Paimon 的统一返回结构体数据
- common：提供 SDK 所需基础类或工具
- exception：自定义异常用于抛出请求 Paimon 失败或 Paimon 处理请求失败的异常
- model：用于封装查询条件或返回数据的实体类
- util：提供加密工具、HTTP请求工具等

## 使用说明

以下这段代码示例向您展示了使用 Alipay SDK for Java调用一个API的3个主要步骤：
1. 调用 Paimon 需要一些基本配置信息，我们需要使用到这个配置信息来创建和初始化每个调用 Paimon 功能的功能操作对象实例。
2. 创建或更新 schema 需要使用 @Property 标注需要上链的字段，包括该字段的类型（Paimon 现只提供 5 种数据类型，分别是 int、string、bool、float、currency，其中 currency 为货币类型）。
3. 创建或更新 document 需要使用 @DocId 标注哪个属性字段对应链上某条数据的 ID，如果创建 document 时没有 @DocId 指定的属性字段，将由链生成并返回。创建和更新 document 都要求其对应的 schema 所拥有的字段值不能为 null，可自行定义非空无效默认值。

### 步骤说明

1. 调用 Paimon 需要一些基本配置信息，我们需要使用到这个配置信息来创建和初始化每个调用 Paimon 功能的功能操作对象实例，这些配置信息需要链管理员提供，配置信息包含的内容如下：

   ```java
   public class PaimonConfig {
       // 域
       private String domain;
       // 接口地址（ip+port，格式：127.0.0.1:9100）
       private String address;
       // 通行id
       private String accessId;
       // 私钥，二进制
       private String secretKey;
   }
   ```

2. 创建或更新 schema 需要使用 @Property 标注需要上链的字段，包括该字段的类型（Paimon 现只提供 5 种数据类型，分别是 int、string、bool、float、currency，其中 currency 为货币类型）。

   @Property：标注 bean 中的属性字段，参数 type 指定该字段在链中对应的类型，参数 indexed 指定该字段是否创建索引，默认为 false 不创建。

   ```java
   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface Property {
       PropertyType type();
       boolean indexed() default false;
   }
   ```

   如果是更新 schema 并且有字段索引的添加或删除，需要调用 rebuildIndex() 方法生效重建的索引。

3. 创建或更新 document 需要使用 @DocId 标注哪个属性字段对应链上某条数据的 ID，如果创建 document 时没有 @DocId 指定的属性字段，将由链生成并返回。创建和更新 document 都要求其对应的 schema 所拥有的字段值不能为 null，可自行定义非空无效默认值。

   Paimon 使用文档存储（包含两个字段 id 和 content），api 包中提供的方法获取的是 content 中的内容转换为指定类型（通过 .class），所以如果习惯使用 api 包中的方法，建议@DocId 和 @Property 两个注解一起作用于主键字段（主键值也包含到 content  中），否则也可调用 application 包中的方法得到 Document 类型后再自行转换。

### DEMO

将打包好的 SDK Jar 包添加到路径

实体类封装需要上链的数据：

```java
@Data
public class User {
    @DocId
    @Property(type = PropertyType.STRING, indexed = true)
    private String userId;

    @Property(type = PropertyType.STRING)
    private String name;

    @Property(type = PropertyType.STRING)
    private String sex;

    @Property(type = PropertyType.INT)
    private Integer age;

    @Property(type = PropertyType.BOOL)
    private Boolean marry;
}
```

添加资产类型（schema）：

```java
@Test
public void addUserSchemaTest() throws IOException {
	SchemaApi<User> schemaApi = new SchemaApi<>(config);
    String schemaName = schemaApi.addSchema(User.class);
    System.out.println(schemaName);
}
```

添加资产（document）：

```java
@Test
public void addUserTest() throws IOException, IllegalAccessException {
	user.setUserId(IdWorker.getIdStr());
	DocumentApi<User> documentApi = new DocumentApi<>(config);
	String userId = documentApi.addDocument(user);
	System.out.println(userId);
}
```

获取资产详情：

```java
@Test
public void getUserTest() throws IOException {
    DocumentApi<User> documentApi = new DocumentApi<>(config);
    User user = documentApi.getDocument("1506898363680686081", User.class);
    System.out.println(user);
}
```

备注说明：创建 api 对应实例的时候传入的参数 config，是上一部分使用说明第一点中的 PaimonConfig。


## 变更日志
每个版本的详细更改记录在[变更日志](./CHANGELOG)中。

注：版本号最末一位修订号的增加（比如从`1.0.0`升级为`1.0.1`），意味着SDK的功能没有发生任何变化，此类变更默认不记录在变更日志中。
