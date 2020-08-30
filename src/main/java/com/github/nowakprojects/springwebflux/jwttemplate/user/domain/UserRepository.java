package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> create(User toCreate);

    Mono<User> update(User toUpdate);

    Flux<User> findAll();

    Mono<User> findBy(Username username);

    Mono<User> findBy(UserId userId);

    Mono<Boolean> existsBy(Username username);
}
