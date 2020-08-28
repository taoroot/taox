package cn.flizi.cloud.mall.web;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RestController
public class AuthorizationController {

    @GetMapping(value = "/")
    public OAuth2AuthorizedClient authorizationCodeGrant(
            @RegisteredOAuth2AuthorizedClient("mall")
                    OAuth2AuthorizedClient authorizedClient) {
        return authorizedClient;
    }
}
