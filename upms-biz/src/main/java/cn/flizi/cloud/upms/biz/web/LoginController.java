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
     * 对外使用,需要token
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
     * 对外使用,需要token
     */
    @SneakyThrows
    @GetMapping(value = "/user_info")
    public String userInfo() {
        String result =
        "{\"code\":0,\"datascope\":{\"functions\":[],\"roles\":[{\"id\":1,\"name\":\"普通角色\",\"role\":\"ROLE_USER\",\"description\":\"\",\"scope\":null,\"scopeType\":\"ALL\"," +
                "\"createTime\":\"2020-08-18T11:36:19\",\"updateTime\":\"2020-08-26T08:46:15\",\"authorities\":null},{\"id\":2,\"name\":\"个人角色\",\"role\":\"ROLE_OWN\",\"" +
                "description\":\"\",\"scope\":null,\"scopeType\":\"OWN\",\"createTime\":\"2020-08-27T08:40:44\",\"updateTime\":null,\"authorities\":null}],\"dept\":\"软件一部\"," +
                "\"menus\":[{\"id\":1000,\"parentId\":-1,\"weight\":1,\"name\":null,\"path\":\"/system\",\"hidden\":false,\"alwaysShow\":false,\"redirect\":\"/authority/index\"" +
                ",\"type\":0,\"component\":\"Layout\",\"meta\":{\"breadcrumb\":null,\"icon\":\"example\",\"title\":\"系统设置\"},\"children\":[{\"id\":1004,\"parentId\":1000,\"weight\":1" +
                ",\"name\":\"User\",\"path\":\"user\",\"hidden\":false,\"alwaysShow\":false,\"redirect\":null,\"type\":0,\"component\":\"user/index\",\"meta\":{\"breadcrumb\":false,\"icon\":" +
                "\"user\",\"title\":\"用户管理\"}},{\"id\":1003,\"parentId\":1000,\"weight\":2,\"name\":\"Role\",\"path\":\"role\",\"hidden\":false,\"alwaysShow\":false,\"redirect\":null,\"type\":" +
                "0,\"component\":\"role/index\",\"meta\":{\"breadcrumb\":false,\"icon\":\"peoples\",\"title\":\"角色管理\"}},{\"id\":1002,\"parentId\":1000,\"weight\":3,\"name\":\"Dept\",\"path\":\"" +
                "dept\",\"hidden\":false,\"alwaysShow\":false,\"redirect\":null,\"type\":0,\"component\":\"dept/index\",\"meta\":{\"breadcrumb\":false,\"icon\":\"tree\",\"title\":\"部门管理\"}},{\"" +
                "id\":1001,\"parentId\":1000,\"weight\":4,\"name\":\"Authority\",\"path\":\"authority\",\"hidden\":false,\"alwaysShow\":false,\"redirect\":null,\"type\":0,\"component\":\"a" +
                "uthority/index\",\"meta\":{\"breadcrumb\":null,\"icon\":\"tree-table\",\"title\":\"权限管理\"}}]},{\"id\":10,\"parentId\":-1,\"weight\":2,\"name\":null,\"path\":\"external-link\",\"hidden\":fal" +
                "se,\"alwaysShow\":null,\"redirect\":\"/github\",\"type\":0,\"component\":\"Layout\",\"meta\":{\"breadcrumb\":null,\"icon\":\"link\",\"title\":\"动态路由\"},\"children\":[{\"id\":11,\"parent" +
                "Id\":10,\"weight\":1,\"name\":\"github\",\"path\":\"https://github.com/taoroot/tao\",\"hidden\":false,\"alwaysShow\":null,\"redirect\":null,\"type\":0,\"component\":null,\"meta\":{\"brea" +
                "dcrumb\":null,\"icon\":\"github\",\"title\":\"github\"}},{\"id\":12,\"parentId\":10,\"weight\":1,\"name\":\"vuepress\",\"path\":\"https://doc-tao.flizi.cn\",\"hidden\":false,\"alwaysS" +
                "how\":null,\"redirect\":null,\"type\":0,\"component\":null,\"meta\":{\"breadcrumb\":null,\"icon\":\"education\",\"title\":\"vuepress\"}}]}],\"info\":{\"id\":1,\"username\":\"admin\",\"p" +
                "assword\":\"****\",\"phone\":\"13131333333\",\"avatar\":\"https://avatars0.githubusercontent.com/u/14340565?v=4\",\"nickname\":\"" +
                "超级管理员\",\"email\":\"1@qq.com\",\"deptId\":1000,\"enabled\":true,\"roles\":null}}}";

        // 查询用户个人信息
        return result;
    }

    /**
     * 对内使用,不需要 token
     * @param username
     */
    @NotAuth
    @SneakyThrows
    @GetMapping(value = "/user/info/{username}")
    public UserInfo getMessages(@PathVariable String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername(username);
        userInfo.setPassword("$2a$10$BK2HGpZVjchJe1HJQUAzVudj8DUhWwNjdS7zEBKFM8RmDjYmbWgLi"); // 123456
        return userInfo;
    }
}
