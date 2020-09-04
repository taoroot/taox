package cn.flizi.cloud.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorization")
public class AuthorizationProperties {

    private List<Client> client = new ArrayList<>();

    @Data
    static class Client {

        private String clientId;

        private String clientSecret;

        private String redirectUri;

        private Set<String> scope;
    }
}
