package cn.flizi.cloud.upms.api.feign;

import cn.flizi.cloud.upms.api.vo.AuthUserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("upms")
public interface UpmsUserFeignClient {

    /**
     * 通过用户名查找用户信息
     */
    @GetMapping(value = "/auth/pass/{username}")
    AuthUserInfoVo getInfoByUsername(@PathVariable("username") String username);

    /**
     * 通过社交账号查找用户信息
     *
     * @param registrationId 社交类型
     * @param name 当前用户在当前社交平台上的唯一标识
     * @see cn.flizi.cloud.auth.social.user
     */
    @GetMapping(value = "/auth/social/{registrationId}/{name}")
    AuthUserInfoVo getInfoBySocial(@PathVariable("registrationId") String registrationId, @PathVariable("name") String name);

}