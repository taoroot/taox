package cn.flizi.cloud.auth.config;

import cn.flizi.cloud.auth.social.SocialDetailsService;
import cn.flizi.cloud.auth.social.UserDetailTypeOAuthUserService;
import cn.flizi.cloud.auth.social.converter.*;
import cn.flizi.cloud.auth.social.user.GitHubOAuth2User;
import cn.flizi.cloud.auth.social.user.GiteeOAuth2User;
import cn.flizi.cloud.auth.social.user.QQOAuth2User;
import cn.flizi.cloud.auth.social.user.WechatOAuth2User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.CustomUserTypesOAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@Order(101)
@AllArgsConstructor
public class OAuth2AuthorizationServerConfiguration extends WebSecurityConfigurerAdapter {

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final SocialDetailsService socialDetailsService;

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(Customizer.withDefaults())
                .logout().invalidateHttpSession(true).and()
                .oauth2Login(config -> config
                        .tokenEndpoint(this::tokenEndpoint)
                        .userInfoEndpoint(this::userInfoEndpoint)
                        .authorizationEndpoint(this::authorizationEndpoint))
                .authorizeRequests().antMatchers("/resource/ids", "/v2/api-docs", "/actuator/**").permitAll()
                    .anyRequest().authenticated();
    }

    // @formatter:on

    // 解决第三方授权登录兼容性问题
    private void authorizationEndpoint(OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {
        authorization.authorizationRequestResolver(new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI));
    }

    // 解决第三方授权登录兼容性问题
    private void tokenEndpoint(OAuth2LoginConfigurer<HttpSecurity>.TokenEndpointConfig tokenEndpoint) {
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new CustomOAuth2AuthorizationCodeGrantRequestEntityConverter());
        OAuth2AccessTokenResponseHttpMessageConverter oAuth2AccessTokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        oAuth2AccessTokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomMapOAuth2AccessTokenResponseConverter());
        ArrayList<MediaType> mediaTypes = new ArrayList<>(oAuth2AccessTokenResponseHttpMessageConverter.getSupportedMediaTypes());
        mediaTypes.add(MediaType.TEXT_PLAIN); // 解决微信问题:  放回是text/plain 的问题
        mediaTypes.add(MediaType.TEXT_HTML); // 解决QQ问题:  放回是text/html 的问题
        oAuth2AccessTokenResponseHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), oAuth2AccessTokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        client.setRestOperations(restTemplate);
        tokenEndpoint.accessTokenResponseClient(client);
    }

    /**
     * 1. 解决第三方授权登录兼容性问题
     * 2. 加载平台 userId
     */
    private void userInfoEndpoint(OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfo) {
        // 目前支持这四个, 如果是标准 OAuth2.0 协议,只需加入自定义的OAuth2User就好
        Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
        customUserTypes.put(GiteeOAuth2User.TYPE, GiteeOAuth2User.class);
        customUserTypes.put(WechatOAuth2User.TYPE, WechatOAuth2User.class);
        customUserTypes.put(QQOAuth2User.TYPE, QQOAuth2User.class);
        customUserTypes.put(GitHubOAuth2User.TYPE, GitHubOAuth2User.class);

        CustomUserTypesOAuth2UserService customOAuth2UserService = new UserDetailTypeOAuthUserService(socialDetailsService, customUserTypes);
        customOAuth2UserService.setRequestEntityConverter(new CustomOAuth2UserRequestEntityConverter());

        RestTemplate restTemplate = new RestTemplate(); // 解决微信问题: 放回是text/plain 的问题
        restTemplate.getMessageConverters().add(new CustomMappingJackson2HttpMessageConverter());
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        customOAuth2UserService.setRestOperations(restTemplate);

        userInfo.userService(customOAuth2UserService);
    }
}
