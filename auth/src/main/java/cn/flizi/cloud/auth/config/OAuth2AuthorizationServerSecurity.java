package cn.flizi.cloud.auth.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
public class OAuth2AuthorizationServerSecurity extends WebSecurityConfigurerAdapter {

    // @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer<>();

		http
			.requestMatcher(new OrRequestMatcher(authorizationServerConfigurer.getEndpointMatchers()))
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
						.anyRequest().authenticated()
			)
			.formLogin(withDefaults())
			.csrf().disable()
			.apply(authorizationServerConfigurer);
	}
	// @formatter:on
}
