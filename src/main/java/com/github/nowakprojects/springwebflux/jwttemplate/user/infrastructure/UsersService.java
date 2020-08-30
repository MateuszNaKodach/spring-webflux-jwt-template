package com.github.nowakprojects.springwebflux.jwttemplate.user.infrastructure;

import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserRepository;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UserRepository userRepository;

    public Mono<Boolean> existsByUsername(String rawUsername) {
        final Username username = Username.from(rawUsername);
        return userRepository.existsBy(username);
    }

    public Mono<User> findByUsername(String rawUsername) {
        Username username = Username.from(rawUsername);
        return userRepository.findBy(username);
    }
}
