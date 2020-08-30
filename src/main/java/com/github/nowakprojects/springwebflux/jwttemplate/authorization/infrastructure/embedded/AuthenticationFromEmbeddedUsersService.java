package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.AuthenticationService;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.PlainTextPassword;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(
        value = "spring-boot-webflux-jwttemplate.authentication",
        havingValue = "embedded",
        matchIfMissing = true)
@AllArgsConstructor
public class AuthenticationFromEmbeddedUsersService implements AuthenticationService {

    private final AuthorizationRepository authorizationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> authenticate(Username username, PlainTextPassword plaitTextPassword) {
        return authorizationRepository.findActiveUserBy(username)
                .filter(user -> passwordEncoder.matches(plaitTextPassword.getRaw(), user.getPassword().getRaw()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new RuntimeException("user cannot be authenticated"))));
    }

    @Override
    public Mono<Boolean> isUserActive(Username username) {
        return authorizationRepository.findActiveUserBy(username)
                .map(user -> true)
                .switchIfEmpty(Mono.just(false));
    }

}
