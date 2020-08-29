package cn.flizi.cloud.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SecurityUtils {

    public static Integer userId() {
        return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static List<String> authorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
            Jwt principal = (Jwt) jwtAuthenticationToken.getPrincipal();
            return Arrays.asList(((String) principal.getClaims().get("scp")).split(" "));
        }
        return null;
    }

    public static List<String> roles() {
        return Objects.requireNonNull(SecurityUtils.authorities())
                .stream().filter(authority -> authority.startsWith("ROLE_"))
                .collect(Collectors.toList());
    }
}
