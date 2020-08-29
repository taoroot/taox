package cn.flizi.cloud.common.security.annotation;


import cn.flizi.cloud.common.security.oauth.resource.Oauth2ResourceSecurityConfigurer;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
@EnableWebSecurity
@Import({Oauth2ResourceSecurityConfigurer.class})
public @interface EnableOauth2ResourceSecurity {


}
