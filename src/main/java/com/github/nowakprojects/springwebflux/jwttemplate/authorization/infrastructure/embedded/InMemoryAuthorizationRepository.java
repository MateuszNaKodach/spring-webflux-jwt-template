package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.ConfigToggles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

@Profile(ConfigToggles.Database.IN_MEMORY)
@Repository
public class InMemoryAuthorizationRepository implements AuthorizationRepository {

    private final ConcurrentHashMap<UserId, User> repository = new ConcurrentHashMap<>();

    @Override
    public Mono<User> save(User user) {
        repository.put(user.getUserId(), user);
        return Mono.just(user);
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
    public Mono<User> findActiveUserBy(Username username) {
        return Mono.justOrEmpty(
                repository.values().stream()
                        .filter(it -> it.getUsername().equals(username))
                        .filter(it -> Activity.ACTIVE.equals(it.getActivity()))
                        .findFirst()
        );
    }

    @Override
    public Mono<User> findBy(UserId userId) {
        return Mono.justOrEmpty(
                repository.values().stream()
                        .filter(it -> it.getUserId().equals(userId))
                        .findFirst()
        );
    }
}
