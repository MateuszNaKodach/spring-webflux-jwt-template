package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;


import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<User> authenticate(Username username, PlainTextPassword password);

    Mono<Boolean> isUserActive(Username username);
}
