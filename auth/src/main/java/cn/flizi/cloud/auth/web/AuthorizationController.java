package cn.flizi.cloud.auth.web;

import cn.flizi.cloud.auth.api.entity.AuthUserOauth2;
import cn.flizi.cloud.auth.social.SocialDetailsService;
import cn.flizi.cloud.auth.social.user.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@Controller
@AllArgsConstructor
public class AuthorizationController {

    private final SocialDetailsService socialDetailsService;

    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping(value = "/")
    public String authorizationCodeGrant(Model model) {
        Map<String, AuthUserOauth2> socials = socialDetailsService.getSocials(true);
        model.addAttribute("AuthUserOauth2", socials);
        return "index";
    }


    @PostMapping("/oauth2/unbind/{registrationId}/{principalName}")
    public String unbind(Model model, @PathVariable("registrationId") String registrationId,
                         @PathVariable("principalName") String principalName) {

        boolean b = socialDetailsService.unbindSocial(registrationId, principalName);
        if (b) {
            // 解绑失败
        }
        return "redirect:/";
    }

    // todo 这里是否有更加优雅的方式
    @GetMapping(value = "/oauth2/bind/" + GiteeOAuth2User.TYPE)
    public String bindGitee(@RegisteredOAuth2AuthorizedClient(GiteeOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(GiteeOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + GitHubOAuth2User.TYPE)
    public String bindGithub(@RegisteredOAuth2AuthorizedClient(GitHubOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(GitHubOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + WechatOAuth2User.TYPE)
    public String bindWechat(@RegisteredOAuth2AuthorizedClient(WechatOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(WechatOAuth2User.TYPE, authorizedClient);
    }

    @GetMapping(value = "/oauth2/bind/" + QQOAuth2User.TYPE)
    public String bindQQ(@RegisteredOAuth2AuthorizedClient(QQOAuth2User.TYPE) OAuth2AuthorizedClient authorizedClient) {
        return doBind(QQOAuth2User.TYPE, authorizedClient);
    }

    private String doBind(String registrationId, OAuth2AuthorizedClient authorizedClient) {
        authorizedClientService.removeAuthorizedClient(registrationId, authorizedClient.getPrincipalName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailOAuthUser userDetailOAuthUser = (UserDetailOAuthUser) authentication.getPrincipal();

        boolean b = socialDetailsService.bindSocial(userDetailOAuthUser.getCustomOAuth2User(),
                registrationId,
                authentication.getName()
        );

        if (!b) {
            // 当前账号以有绑定,请先解绑
        }
        return "redirect:/";
    }
}
