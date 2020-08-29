package cn.flizi.cloud.common.security.annotation;


import cn.flizi.cloud.common.security.oauth.login.CustomLogoutSuccessHandler;
import cn.flizi.cloud.common.security.oauth.login.CustomOAuth2AuthenticationSuccessHandler;
import cn.flizi.cloud.common.security.oauth.login.Oauth2LoginSecurityConfigurer;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@Import({Oauth2LoginSecurityConfigurer.class,
        CustomOAuth2AuthenticationSuccessHandler.class,
        CustomLogoutSuccessHandler.class
})
@EnableWebSecurity
public @interface EnableOAuth2LoginSecurity {

    String value() default "";
}
