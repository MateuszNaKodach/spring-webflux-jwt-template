package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class CurrentUserProvider {

    public Mono<AuthenticationUser> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .cast(AuthenticationUser.class);
    }

    public Mono<Void> removeCurrentUserFromContext() {
        return ReactiveSecurityContextHolder.getContext()
                .filter(Objects::nonNull)
                .map(securityContext -> {
                            securityContext.setAuthentication(null);
                            return securityContext;
                        }
                ).then();
    }
}
