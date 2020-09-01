package cn.flizi.cloud.auth.service;

import cn.flizi.cloud.upms.api.feign.UpmsUserFeignClient;
import cn.flizi.cloud.upms.api.vo.AuthUserInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class RemoteUserDetailService implements UserDetailsService {

    private final UpmsUserFeignClient upmsUserFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserInfoVo info = upmsUserFeignClient.getInfoByUsername(username);

        if (info == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        grantedAuthorities.addAll(AuthorityUtils.createAuthorityList(info.getRoles()));
        grantedAuthorities.addAll(AuthorityUtils.createAuthorityList(info.getAuthorities()));

        return new User(String.format("%d", info.getId()), info.getPassword(), grantedAuthorities);
    }
}
