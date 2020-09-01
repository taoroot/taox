package cn.flizi.cloud.auth.social;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public interface SocialDetailsService {

    UserDetails loadUserBySocial(String registrationId, String name) throws OAuth2AuthenticationException;
}
