package cn.flizi.cloud.upms.biz.web;

import cn.flizi.cloud.common.security.annotation.NotAuth;
import cn.flizi.cloud.upms.api.vo.UserInfo;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RestController
public class LoginController {

    /**
     * 特供 oauth2Login 使用, 根据 token 放回用户信息
     * todo 兼容原来的 Security OAuth 2.0
     */
    @SneakyThrows
    @GetMapping(value = "/check_token")
    public HashMap<String, String> checkToken() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        return hashMap;
    }

    /**
     * 特供 Auth-Server 服务密码登录,不需要 token
     * @param username 用户登录账号
     */
    @NotAuth
    @SneakyThrows
    @GetMapping(value = "/user/user/{username}")
    public UserInfo getMessages(@PathVariable String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername(username);
        userInfo.setPassword("$2a$10$BK2HGpZVjchJe1HJQUAzVudj8DUhWwNjdS7zEBKFM8RmDjYmbWgLi"); // 123456
        return userInfo;
    }

    /**
     * 前端应该用这个
     */
    @SneakyThrows
    @GetMapping(value = "/user_info")
    public String userInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // todo
        String result =
        "{\"code\":0,\"data\":{\"functions\":[],\"roles\":[],\"dept\":\"软件一部\"," +
                "\"menus\":[],\"info\":{\"id\":1,\"username\":\"" + name +
                "\",\"password\":\"****\",\"phone\":\"13131333333\"" +
                ",\"avatar\":\"https://avatars0.githubusercontent.com/u/14340565?v=4\",\"nickname\":\"" +
                "超级管理员\",\"email\":\"1@qq.com\",\"deptId\":1000,\"enabled\":true,\"roles\":null}}}";

        // 查询用户个人信息
        return result;
    }
}
