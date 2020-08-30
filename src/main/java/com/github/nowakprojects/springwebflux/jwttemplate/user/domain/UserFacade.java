package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserFacade {

    private final UserRepository userRepository;

    public UserFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> create(User user) {
        return userRepository.findBy(user.getUsername())
                .map(this::throwWhenProvidedUsernameExists)
                .switchIfEmpty(Mono.defer(() -> userRepository.create(user)));
    }

    private User throwWhenProvidedUsernameExists(User foundUser) {
        throw new UserAlreadyExistsException("user with provided username " + foundUser.getUsername().getRaw() + " already exists");
    }

    public Mono<User> update(User user) {
        return userRepository.findBy(user.getUserId())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserDoesNotExistException("user with provided userId " + user.getUserId().getRaw() + " doesn't exist"))))
                .then(userRepository.update(user));
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findBy(Username username) {
        return userRepository.findBy(username);
    }

    public Mono<User> activate(Username username) {
        return userRepository.findBy(username)
                .map(User::activate)
                .flatMap(userRepository::update);
    }

    public Mono<User> deactivate(Username username) {
        return userRepository.findBy(username)
                .map(User::deactivate)
                .flatMap(userRepository::update);
    }

}
