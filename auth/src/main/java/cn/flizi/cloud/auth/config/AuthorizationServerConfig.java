package cn.flizi.cloud.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keys.KeyManager;
import org.springframework.security.crypto.keys.StaticKeyGeneratingKeyManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@EnableWebSecurity
@EnableConfigurationProperties(AuthorizationProperties.class)
public class AuthorizationServerConfig {

	@Resource
	private AuthorizationProperties authorizationProperties;

    // @formatter:off
	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		List<RegisteredClient> list = new LinkedList<>();

		for (AuthorizationProperties.Client clientRegistration : authorizationProperties.getClient()) {

			RegisteredClient.Builder builder = RegisteredClient
					.withId(UUID.randomUUID().toString())
					.clientId(clientRegistration.getClientId())
					.clientSecret(clientRegistration.getClientSecret())
					.clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
					.redirectUri(clientRegistration.getRedirectUri());

			clientRegistration.getScope().forEach(builder::scope);

			list.add(builder.build());
		}

		return new InMemoryRegisteredClientRepository(list.toArray(new RegisteredClient[0]));
	}
	// @formatter:on

    @Bean
    public KeyManager keyManager() {
        return new StaticKeyGeneratingKeyManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
