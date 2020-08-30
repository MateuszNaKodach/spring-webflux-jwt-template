package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@AllArgsConstructor
class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        ServerHttpRequest request = swe.getRequest();
        Optional<String> authHeader = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        return authHeader
                .filter(authHeaderValue -> authHeaderValue.startsWith(TOKEN_PREFIX))
                .map(authHeaderValue -> authHeaderValue.replace(TOKEN_PREFIX, ""))
                .map(authHeaderValue -> new UsernamePasswordAuthenticationToken(authHeaderValue, authHeaderValue))
                .map(authentication -> jwtAuthenticationManager.authenticate(authentication).map(SecurityContextImpl::new).cast(SecurityContext.class))
                .orElse(Mono.empty());
    }
}
