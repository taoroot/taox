package cn.flizi.cloud.auth.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;

@Log4j2
@AllArgsConstructor
@Order(99)
public class CustomSecurityConfigurer extends WebSecurityConfigurerAdapter {


    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .logout()
                    .logoutRequestMatcher(new AndRequestMatcher())
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                .and()
                    .httpBasic();

    }
    // @formatter:on

}
