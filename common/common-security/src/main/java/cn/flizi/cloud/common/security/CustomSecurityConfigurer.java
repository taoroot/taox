package cn.flizi.cloud.common.security;

import cn.flizi.cloud.common.security.oauth.CustomHttpSessionOAuth2AuthorizationRequestRepository;
import cn.flizi.cloud.common.security.oauth.CustomOAuth2AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Log4j2
@AllArgsConstructor
@Order(99)
public class CustomSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .oauth2Login(config -> config.successHandler(oAuth2AuthenticationSuccessHandler)
                        .authorizationEndpoint(authorization -> {
                            // 处理 /{registrationId}/oauth2/authorization/{registrationId}
                            // 由于网关服务, 跳转需加前缀 spring.application.name, 将 name 和 registrationId 取名相同解决
                            DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                                    clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

                            // 保存 redirect_url 参数
                            resolver.setAuthorizationRequestCustomizer(build -> build.attributes(attributesConsumer -> {
                                ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                                HttpServletRequest request = Objects.requireNonNull(attrs).getRequest();
                                String redirectUri = request.getParameter(OAuth2ParameterNames.REDIRECT_URI);
                                assert StringUtils.isEmpty(redirectUri);
                                // 可以再这里做判断redirectUri,走白名单, 或者写死redirectUri
                                attributesConsumer.put(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
                            }));
                            authorization.authorizationRequestResolver(resolver);

                            // 恢复 redirect_url 参数
                            authorization.authorizationRequestRepository(new CustomHttpSessionOAuth2AuthorizationRequestRepository());
                        })

                );

    }

}
