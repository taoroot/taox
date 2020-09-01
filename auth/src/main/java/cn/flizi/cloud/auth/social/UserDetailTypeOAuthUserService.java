package cn.flizi.cloud.auth.social;

import cn.flizi.cloud.auth.social.user.CustomOAuth2User;
import cn.flizi.cloud.auth.social.user.UserDetailOAuthUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class UserDetailTypeOAuthUserService extends CustomUserTypesOAuth2UserService {

    private final SocialDetailsService socialDetailsService;

    public UserDetailTypeOAuthUserService(SocialDetailsService socialDetailsService,
                                          Map<String, Class<? extends OAuth2User>> customUserTypes) {
        super(customUserTypes);
        this.socialDetailsService = socialDetailsService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        UserDetails userDetails = socialDetailsService.loadUserBySocial(registrationId, oAuth2User.getName());
        return new UserDetailOAuthUser(userDetails, oAuth2User);
    }
}
