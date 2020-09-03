package cn.flizi.cloud.auth.web;

import cn.flizi.cloud.auth.api.entity.AuthUserOauth2;
import cn.flizi.cloud.auth.social.SocialDetailsService;
import cn.flizi.cloud.auth.social.user.GiteeOAuth2User;
import cn.flizi.cloud.auth.social.user.UserDetailOAuthUser;
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

    @GetMapping(value = "/oauth2/bind/" + GiteeOAuth2User.TYPE)
    public String bindGitee(Model model, @RegisteredOAuth2AuthorizedClient(GiteeOAuth2User.TYPE)
            OAuth2AuthorizedClient authorizedClient) {
        authorizedClientService.removeAuthorizedClient(GiteeOAuth2User.TYPE, authorizedClient.getPrincipalName());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailOAuthUser userDetailOAuthUser = (UserDetailOAuthUser) authentication.getPrincipal();

        boolean b = socialDetailsService.bindSocial(userDetailOAuthUser.getCustomOAuth2User(),
                GiteeOAuth2User.TYPE,
                authentication.getName()
        );

        if (!b) {
            // 当前账号以有绑定,请先解绑
        }

        return "redirect:/";
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
}
