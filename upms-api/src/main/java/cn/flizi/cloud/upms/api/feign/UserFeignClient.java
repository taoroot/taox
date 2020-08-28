package cn.flizi.cloud.upms.api.feign;

import cn.flizi.cloud.upms.api.vo.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("upms")
public interface UserFeignClient {

    @GetMapping("/user/info/{username}")
    UserInfo info(@PathVariable("username") String username);
}
