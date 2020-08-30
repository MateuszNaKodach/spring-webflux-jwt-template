package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthorizationSaver {

    private final AuthorizationRepository authorizationRepository;

    AuthorizationSaver(AuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public Mono<User> create(User userForAuthorization) {
        return authorizationRepository.findBy(userForAuthorization.getUsername())
                .map((this::throwWhenProvidedUsernameExists))
                .switchIfEmpty(Mono.defer(() -> authorizationRepository.save(userForAuthorization)));
    }

    private User throwWhenProvidedUsernameExists(User foundUser) {
        throw new UserAlreadyExistsException("user with provided username " + foundUser.getUsername().getRaw() + " already exists");
    }

    public Mono<User> update(User userForAuthorization) {
        return authorizationRepository.findBy(userForAuthorization.getUserId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserDoesNotExistException("user with provided userId " + userForAuthorization.getUserId().getRaw() + " doesn't exist"))))
                .then(authorizationRepository.save(userForAuthorization));
    }

    public Mono<User> activate(Username username) {
        return authorizationRepository.findBy(username)
                .switchIfEmpty(Mono.defer(() -> userDoesNotExists(username)))
                .map(User::activate)
                .flatMap(authorizationRepository::save);
    }

    public Mono<User> deactivate(Username username) {
        return authorizationRepository.findBy(username)
                .switchIfEmpty(Mono.defer(() -> userDoesNotExists(username)))
                .map(User::deactivate)
                .flatMap(authorizationRepository::save);
    }

    private Mono<User> userDoesNotExists(Username username) {
        return Mono.error(new UserDoesNotExistException("user with provided username " + username.getRaw() + " doesn't exists"));
    }
}

