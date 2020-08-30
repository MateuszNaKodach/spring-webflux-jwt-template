package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.ConfigToggles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Profile(ConfigToggles.Database.MONGO_DB)
@Repository
public class MongoAuthorizationRepository implements AuthorizationRepository {

    private final ReactiveSpringAuthorizationRepository reactiveSpringAuthorizationRepository;

    MongoAuthorizationRepository(ReactiveSpringAuthorizationRepository reactiveSpringAuthorizationRepository) {
        this.reactiveSpringAuthorizationRepository = reactiveSpringAuthorizationRepository;
    }

    @Override
    public Mono<User> save(User user) {
        return reactiveSpringAuthorizationRepository.save(user);
    }

    @Override
    public Mono<User> findBy(Username username) {
        return reactiveSpringAuthorizationRepository.findByUsername(username);
    }

    @Override
    public Mono<User> findActiveUserBy(Username username) {
        return reactiveSpringAuthorizationRepository.findByUsernameAndActivity(username, Activity.ACTIVE);
    }

    @Override
    public Mono<User> findBy(UserId userId) {
        return reactiveSpringAuthorizationRepository.findByUserId(userId);
    }
}
