# taox

# 特点:

- 基于新的 `OAuth2 Authorization Server` 

- 基于 OAuth2 实现单点登录


[线上预览地址](http://mall.flizi.cn) 

```
username: user
password: 123456
```

# 依赖

| Dependency | Version |
| ----------- | ----------- |
| Spring Boot   | 2.3.2.RELEASE       |
| Spring Cloud   | Hoxton.SR7        |
| Spring Security | 5.3.3.RELEASE        |
| Spring Security OAuth2 Authorization Server   | 0.0.1 |


# 使用

host 配置:

```java
127.0.0.1 auth-server register-server
```

# 启动顺序

| Microservice  | Port | Desc |
| ----------- | ----------- | ----------- | 
| EurekaApplication   | 8848       | 注册中心
| GatewayApplication  | 8008      | 网关
| UpmsApplication | 6513        | 通用用户权限管理系统(开发中)
| AuthApplication   | 6628| 统一认证中心 |
| MallApplication   | 8800| 测试应用 |

# 访问地址 

```
127.0.0.1:8008/mall/oauth2/authorization/mall?redirect_uri=http://localhost:8081/
```

