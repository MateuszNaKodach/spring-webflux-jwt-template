package com.github.nowakprojects.springwebflux.jwttemplate.user.infrastructure;

import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

interface SpringReactiveRepository extends ReactiveCrudRepository<User, UserId> {

    Mono<User> findByUsername(Username username);

    Mono<Boolean> existsByUsername(Username username);
}
