package cn.flizi.cloud.auth.config;

import cn.flizi.cloud.auth.social.converter.*;
import cn.flizi.cloud.auth.social.user.GitHubOAuth2User;
import cn.flizi.cloud.auth.social.user.GiteeOAuth2User;
import cn.flizi.cloud.auth.social.user.QQOAuth2User;
import cn.flizi.cloud.auth.social.user.WechatOAuth2User;
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
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Log4j2
@Component
@Order(101)
public class CustomOAuth2AuthorizationServerConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private ClientRegistrationRepository clientRegistrationRepository;

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(Customizer.withDefaults())
                .oauth2Login(config -> config
                        .tokenEndpoint(this::tokenEndpoint)
                        .userInfoEndpoint(this::userInfoEndpoint)
                        .authorizationEndpoint(this::authorizationEndpoint))
                .authorizeRequests()
                    .anyRequest().authenticated();
    }
    // @formatter:on


    // 解决第三方授权登录兼容性问题
    private void authorizationEndpoint(OAuth2LoginConfigurer<HttpSecurity>.AuthorizationEndpointConfig authorization) {
        authorization.authorizationRequestResolver(new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository,
                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI));
    }
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
    private void userInfoEndpoint(OAuth2LoginConfigurer<HttpSecurity>.UserInfoEndpointConfig userInfo) {
        List<OAuth2UserService<OAuth2UserRequest, OAuth2User>> userServices = new ArrayList<>();

        Map<String, Class<? extends OAuth2User>> customUserTypes = new HashMap<>();
        customUserTypes.put(GiteeOAuth2User.TYPE, GiteeOAuth2User.class);
        customUserTypes.put(GitHubOAuth2User.TYPE, GitHubOAuth2User.class);
        customUserTypes.put(WechatOAuth2User.TYPE, WechatOAuth2User.class);
        customUserTypes.put(QQOAuth2User.TYPE, QQOAuth2User.class);

        // 解决微信问题: 放回是text/plain 的问题
        CustomOAuth2UserRequestEntityConverter customOAuth2UserRequestEntityConverter = new CustomOAuth2UserRequestEntityConverter();
        CustomUserTypesOAuth2UserService customOAuth2UserService = new CustomUserTypesOAuth2UserService(customUserTypes);
        customOAuth2UserService.setRequestEntityConverter(customOAuth2UserRequestEntityConverter);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new CustomMappingJackson2HttpMessageConverter());

        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        customOAuth2UserService.setRestOperations(restTemplate);
        userServices.add(customOAuth2UserService);
        DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
        defaultOAuth2UserService.setRequestEntityConverter(customOAuth2UserRequestEntityConverter);
        userServices.add(defaultOAuth2UserService);
        userInfo.userService(new DelegatingOAuth2UserService<>(userServices));
    }
}
