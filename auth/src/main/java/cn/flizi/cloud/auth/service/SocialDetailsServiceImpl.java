package cn.flizi.cloud.auth.service;

import cn.flizi.cloud.auth.AuthUtils;
import cn.flizi.cloud.auth.api.entity.AuthUser;
import cn.flizi.cloud.auth.api.entity.AuthUserOauth2;
import cn.flizi.cloud.auth.service.mapper.UserMapper;
import cn.flizi.cloud.auth.service.mapper.UserOauth2Mapper;
import cn.flizi.cloud.auth.social.SocialDetailsService;
import cn.flizi.cloud.auth.social.user.CustomOAuth2User;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class SocialDetailsServiceImpl implements SocialDetailsService {
    final UserOauth2Mapper userOauth2Mapper;
    final UserMapper userMapper;
    final OAuth2ClientProperties properties;


    @Override
    public UserDetails loadUserBySocial(String registrationId, String name) {
        AuthUserOauth2 userOauth2 = userOauth2Mapper.selectOne(
                Wrappers.<AuthUserOauth2>lambdaQuery()
                        .eq(AuthUserOauth2::getClientRegistrationId, registrationId)
                        .eq(AuthUserOauth2::getPrincipalName, name)
        );

        if (userOauth2 == null) {
            return null;
        }
        AuthUser user = userMapper.selectById(userOauth2.getUserId());
        return AuthUtils.translate(user);
    }

    @Override
    public boolean bindSocial(CustomOAuth2User oAuth2User, String registrationId, String username) {
        AuthUserOauth2 authUserOauth2 = new AuthUserOauth2();
        authUserOauth2.setAvatar(oAuth2User.getAvatar());
        authUserOauth2.setClientRegistrationId(registrationId);
        authUserOauth2.setNickname(oAuth2User.getNickname());
        authUserOauth2.setPrincipalName(oAuth2User.getName());
        authUserOauth2.setUserId(username);
        int insert = userOauth2Mapper.insert(authUserOauth2);
        return insert > 0;
    }

    @Override
    public boolean unbindSocial(String registrationId, String principalName) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        int delete = userOauth2Mapper.delete(
                Wrappers.<AuthUserOauth2>lambdaQuery()
                        .eq(AuthUserOauth2::getClientRegistrationId, registrationId)
                        .eq(AuthUserOauth2::getPrincipalName, principalName)
                        .eq(AuthUserOauth2::getUserId, name)
        );
        return delete > 0;
    }

    @Override
    public Map<String, AuthUserOauth2> getSocials(boolean all) {
        HashMap<String, AuthUserOauth2> result = new HashMap<>();

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        // 全部
        if (all) {
            Collection<ClientRegistration> clientRegistrations = OAuth2ClientPropertiesRegistrationAdapter
                    .getClientRegistrations(properties).values();
            for (ClientRegistration clientRegistration : clientRegistrations) {
                result.put(clientRegistration.getRegistrationId(), null);
            }
        }

        // 已绑
        List<AuthUserOauth2> list = userOauth2Mapper.selectList(
                Wrappers.<AuthUserOauth2>lambdaQuery().eq(AuthUserOauth2::getUserId, name));
        for (AuthUserOauth2 userOauth2 : list) {
            result.put(userOauth2.getClientRegistrationId(), userOauth2);
        }

        return result;
    }
}
