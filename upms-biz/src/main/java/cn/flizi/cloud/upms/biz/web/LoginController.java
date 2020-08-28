package cn.flizi.cloud.upms.biz.web;

import cn.flizi.cloud.upms.api.vo.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @SneakyThrows
    @GetMapping("/user_info")
    public String getMessages() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);
        return new ObjectMapper().writeValueAsString(hashMap);
    }

    @SneakyThrows
    @GetMapping("/user/info/{username}")
    public UserInfo getMessages(@PathVariable String username) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setUsername(username);
        userInfo.setPassword("$2a$10$BK2HGpZVjchJe1HJQUAzVudj8DUhWwNjdS7zEBKFM8RmDjYmbWgLi"); // 123456
        return userInfo;
    }
}
