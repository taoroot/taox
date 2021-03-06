package cn.flizi.cloud.upms.biz.web;

import cn.flizi.cloud.common.core.utils.R;
import cn.flizi.cloud.upms.biz.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authservice;

    /**
     * 特供 Resource-Server#oauth2Login 使用, 根据 token 放回用户信息
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
     * 前端获取登录者信息
     */
    @SneakyThrows
    @GetMapping(value = "/user_info")
    public R userInfo() {
        return authservice.userInfo();
    }
}
