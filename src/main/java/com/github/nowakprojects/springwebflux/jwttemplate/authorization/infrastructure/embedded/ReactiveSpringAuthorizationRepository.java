package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ReactiveSpringAuthorizationRepository extends ReactiveCrudRepository<User, UserId> {

    Mono<User> findByUsername(Username username);

    Mono<User> findByUserId(UserId userId);

    Mono<User> findByUsernameAndActivity(Username username, Activity activity);
}
