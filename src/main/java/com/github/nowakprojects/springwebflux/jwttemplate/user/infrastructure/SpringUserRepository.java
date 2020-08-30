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

@Profile(ConfigToggles.Database.MONGO_DB)
@Repository
class SpringUserRepository implements UserRepository {

    private final SpringReactiveRepository springReactiveRepository;

    SpringUserRepository(SpringReactiveRepository springReactiveRepository) {
        this.springReactiveRepository = springReactiveRepository;
    }

    @Override
    public Mono<User> create(User toCreate) {
        return springReactiveRepository.save(toCreate);
    }

    @Override
    public Mono<User> update(User toUpdate) {
        return springReactiveRepository.save(toUpdate);
    }

    @Override
    public Flux<User> findAll() {
        return springReactiveRepository.findAll();
    }

    @Override
    public Mono<User> findBy(Username username) {
        return springReactiveRepository.findByUsername(username);
    }

    @Override
    public Mono<User> findBy(UserId userId) {
        return springReactiveRepository.findById(userId);
    }

    @Override
    public Mono<Boolean> existsBy(Username username) {
        return springReactiveRepository.existsByUsername(username);
    }
}

