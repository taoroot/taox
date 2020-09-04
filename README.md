# taox

![auth](https://cdn.flizi.cn/taox/auth.gif)
![auth](https://cdn.flizi.cn/taox/swagger.gif)

# 特点:

- 基于新的 `Spring Security OAuth2 Authorization Server` **实现单点登录**
  - 支持密码登录
  - 支持第三方OAuth2授权登录
    - github
    - 码云
    - 微信开放平台
    - QQ
  - 支持短信登录(计划中)

- 全局统一 USER_ID
- 个性化

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
| Spring Security | [5.3.3.RELEASE](https://github.com/spring-projects/spring-security)        |
| Spring Security OAuth2 Authorization Server   | [0.0.1](https://github.com/spring-projects-experimental/spring-authorization-server) |


# 开发

host 配置:

```java
127.0.0.1 auth-server register-server
```

## 启动顺序

| Microservice  | Port | Desc |
| ----------- | ----------- | ----------- | 
| EurekaApplication   | 8848       | 注册中心
| GatewayApplication  | 8008      | 网关
| UpmsApplication | 6513        | 通用用户权限管理系统(开发中)
| AuthApplication   | 6628| 统一认证中心|
| MallApplication   | 8800| 测试应用 |


## 开发测试 

```js
cd mall-ui
npm run dev
```


# 关于登录流程

Spring Security 5 的 Oauth2Login, 所有配置都在后端, 前端只需要配置访问请求地址


```
http://gateway/${router}/oauth2/authorization/${registerId}?redirect_uri=${redirect_uri}
```

- registerId 代表一个client配置对象唯一标识, 我这里将配置成clientId一样,方便记忆
- router 代表一个微服务的路由转发前缀, 默认使用 spring.application.name
- redirect_uri 最终登录成功后,跳转回前端的地址,会带回access_token, (并非默认实现,通过自定义 successHandler 实现)

我这里取巧,将clientId, registerId, router 三者定义值一样

以mall为例, 前端跳转地址

```
http://gw.flizi.cn/mall/oauth2/authorization/mall/?redirect_uri=http%3A%2F%2Fmall.flizi.cn%2F%23%2Fauth-redirect
```

## EnableOAuth2LoginSecurity

将 Oauth2Login 通过注解导入

```java
@EnableDiscoveryClient
@EnableOAuth2LoginSecurity
@EnableOauth2ResourceSecurity
@SpringBootApplication
public class MallApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class, args);
    }
}
```

## EnableOauth2ResourceSecurity

将 Oauth2Resource 通过注解导入

```java
@EnableDiscoveryClient
@EnableOauth2ResourceSecurity
@SpringBootApplication
public class UpmsApplication {
	public static void main(String[] args) {
		SpringApplication.run(UpmsApplication.class, args);
	}
}
```

## TIPS

upms 只是资源服务器,只对外提供资源

mall 不只是资源服务器,还对外提供 Oauth2Login 服务, 所以都得配置, 后期可以将mall拆分, mall-auth, mall-xxx ..., 专门负责 Oauth2Login

## 个人想法?

1. 是否也可以利用客户端授权模式,做一个 EnableOauth2PasswordSecurity, 完成传统的密码登录 ?

2. 为什么不直接改造 auth-server 实现密码登录? 

因为 Oauth2 的 password 模式, 由前端直接发送client&secret到auth-server, secrect 是对外暴露的, 通过客户端授权模式,讲配置隐藏

