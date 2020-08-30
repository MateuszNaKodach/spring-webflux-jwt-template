package com.github.nowakprojects.springwebflux.jwttemplate.user.infrastructure;

import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.ConfigToggles;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserRepository;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@Profile(ConfigToggles.Database.IN_MEMORY)
@Repository
class InMemoryUserRepository implements UserRepository {

    private final ConcurrentHashMap<UserId, User> repository = new ConcurrentHashMap<>();

    @Override
    public Mono<User> create(User toCreate) {
        repository.put(toCreate.getUserId(), toCreate);
        return Mono.just(toCreate);
    }

    @Override
    public Mono<User> update(User toUpdate) {
        repository.put(toUpdate.getUserId(), toUpdate);
        return Mono.just(toUpdate);
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(repository.values());
    }

    @Override
    public Mono<User> findBy(Username username) {
        return Mono.justOrEmpty(
                repository.values().stream()
                        .filter(it -> it.getUsername().equals(username))
                        .findFirst()
        );
    }

    @Override
    public Mono<User> findBy(UserId userId) {
        return repository.containsKey(userId)
                ? Mono.just(repository.get(userId))
                : Mono.empty();
    }


    @Override
    public Mono<Boolean> existsBy(Username username) {
        return findBy(username)
                .map(it -> Boolean.TRUE)
                .switchIfEmpty(Mono.just(Boolean.FALSE));
    }
}

