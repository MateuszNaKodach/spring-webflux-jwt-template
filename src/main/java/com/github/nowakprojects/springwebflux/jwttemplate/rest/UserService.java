package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.CurrentUserProvider;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded.AuthorizationSaver;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserFacade;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserFacade userFacade;
    private final AuthorizationSaver authorizationSaver;
    private final CurrentUserProvider currentUserProvider;

    Flux<User> findAll() {
        return userFacade.findAll();
    }

    @Transactional
    public Mono<Void> createUser(com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User userForAuthorization, User userForManagement) {
        return Mono.when(userFacade.create(userForManagement), authorizationSaver.create(userForAuthorization));
    }

    @Transactional
    Mono<Void> updateUser(com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User userForAuthorization, User userForManagement) {
        return Mono.when(userFacade.update(userForManagement), authorizationSaver.update(userForAuthorization));
    }

    Mono<Void> activateUser(Username usernameForAuthorization, com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username usernameForManagement) {
        return Mono.when(userFacade.activate(usernameForManagement), authorizationSaver.activate(usernameForAuthorization));
    }

    Mono<Void> deactivateUser(Username usernameForAuthorization, com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username usernameForManagement) {
        return Mono.when(userFacade.deactivate(usernameForManagement), authorizationSaver.deactivate(usernameForAuthorization), currentUserProvider.removeCurrentUserFromContext());
    }
}
