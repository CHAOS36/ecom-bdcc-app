package org.sid.orderservice.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1) Vérifier qu’on est authentifié ET que l’Authentication est de type JWT
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            // On récupère le token JWT
            String tokenValue = jwtAuth.getToken().getTokenValue();

            // 2) On ajoute l’header Authorization dans la requête Feign
            requestTemplate.header("Authorization", "Bearer " + tokenValue);
        }
        // else => l’utilisateur est anonyme, on ne met pas de header
        // (ou on lève une exception si on veut forcer l'authentification)
    }
}

