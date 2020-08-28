package cn.flizi.cloud.upms.biz.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/user/info/**").permitAll()
                .anyRequest().authenticated()
                .and()
                    .oauth2ResourceServer().jwt();
    }
    // @formatter:on
}
