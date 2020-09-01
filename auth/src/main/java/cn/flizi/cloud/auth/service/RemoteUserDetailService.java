package cn.flizi.cloud.auth.service;

import cn.flizi.cloud.upms.api.feign.UserFeignClient;
import cn.flizi.cloud.upms.api.vo.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RemoteUserDetailService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo info = userFeignClient.getInfoByUsername(username);
        if (info != null) {
            return new User(info.getUsername(),
                    info.getPassword(),
                    AuthorityUtils.createAuthorityList("USER"));
        }
        return null;
    }
}
