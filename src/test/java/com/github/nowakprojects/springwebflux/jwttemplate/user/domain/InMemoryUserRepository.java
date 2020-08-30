package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private Map<UserId, User> userRepo = new HashMap<>();

    @Override
    public Mono<User> create(User toCreate) {
        userRepo.put(toCreate.getUserId(), toCreate);
        return Mono.just(toCreate);
    }

    @Override
    public Mono<User> update(User toUpdate) {
        userRepo.put(toUpdate.getUserId(), toUpdate);
        return Mono.just(toUpdate);
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(userRepo.values());
    }

    @Override
    public Mono<User> findBy(Username username) {
        final Optional<User> userWithProvidedUsername = userRepo.values()
                .stream().filter(user -> user.getUsername().equals(username))
                .findFirst();
        return Mono.justOrEmpty(userWithProvidedUsername);
    }

    @Override
    public Mono<User> findBy(UserId userId) {
        final Optional<User> userWithProvidedUserId = userRepo.values()
                .stream().filter(user -> user.getUserId().equals(userId))
                .findFirst();
        return Mono.justOrEmpty(userWithProvidedUserId);
    }

    @Override
    public Mono<Boolean> existsBy(Username username) {
        boolean existsUser = userRepo.values().stream().anyMatch(user -> user.getUsername().equals(username));
        return Mono.just(existsUser);
    }

    Mono<Integer> count() {
        return Mono.just(userRepo.size());
    }

}
