package cn.flizi.cloud.auth.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Log4j2
@Component
@Order(101)
public class CustomOAuth2AuthorizationServerConfiguration extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(withDefaults())
                .oauth2Login(withDefaults())
                .authorizeRequests().anyRequest().authenticated();
    }
    // @formatter:on

}
