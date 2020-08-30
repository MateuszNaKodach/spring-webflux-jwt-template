package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.AuthenticationService;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.AuthenticationUser;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Component
@RequiredArgsConstructor
class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private static final List<SimpleGrantedAuthority> AUTHORITIES = List.of(new SimpleGrantedAuthority(Role.USER.roleName()));
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        return zipUserIdAndUsername(authToken)
                .filter(token -> jwtTokenProvider.validateToken(authToken))
                .map(it -> new AuthenticationUser(it.getT1(), it.getT2()))
                .filterWhen(it -> authenticationService.isUserActive(it.getUsername()))
                .map(user -> new UsernamePasswordAuthenticationToken(user, null, AUTHORITIES));
    }

    private Mono<Tuple2<UserId, Username>> zipUserIdAndUsername(String authToken) {
        try {
            return Mono.zip(Mono.justOrEmpty(UserId.from(jwtTokenProvider.getUserIdFromToken(authToken))),
                    Mono.justOrEmpty(Username.from(jwtTokenProvider.getUsernameFromToken(authToken))));
        } catch (ExpiredJwtException e) {
            return Mono.error(e);
        }
    }

}

