package cn.flizi.cloud.auth.service;

import cn.flizi.cloud.auth.social.SocialDetailsService;
import cn.flizi.cloud.upms.api.feign.UpmsUserFeignClient;
import cn.flizi.cloud.upms.api.vo.AuthUserInfoVo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@AllArgsConstructor
public class RemoteUserDetailService implements UserDetailsService, SocialDetailsService {

    private final UpmsUserFeignClient upmsUserFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUserInfoVo info = upmsUserFeignClient.getInfoByUsername(username);
        if (info == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return getUserDetails(info);
    }

    @Override
    public UserDetails loadUserBySocial(String registrationId, String name) throws OAuth2AuthenticationException {
        AuthUserInfoVo info = upmsUserFeignClient.getInfoBySocial(registrationId, name);
        if (info == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED));
        }
        return getUserDetails(info);
    }

    private UserDetails getUserDetails(AuthUserInfoVo info) {
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        grantedAuthorities.addAll(AuthorityUtils.createAuthorityList(info.getRoles()));
        grantedAuthorities.addAll(AuthorityUtils.createAuthorityList(info.getAuthorities()));

        // 我们的全局永久唯一凭证是 userId, security 想要的也是这个, 只是他定义的名字叫 username
        // 这里回去security 不会再对这个 username 进行判断,
        return new User(String.format("%d", info.getId()), info.getPassword(), grantedAuthorities);
    }
}
