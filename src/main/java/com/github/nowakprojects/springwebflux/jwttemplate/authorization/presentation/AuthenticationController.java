package com.github.nowakprojects.springwebflux.jwttemplate.authorization.presentation;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.AuthenticationService;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.PlainTextPassword;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security.JWTTokenProvider;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
class AuthenticationController {

    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationService authenticationService;
    private final UserFacade userFacade;

    @PostMapping("/token")
    Mono<ResponseEntity<TokenAndUserResponseBody>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(Username.from(authenticationRequest.getUsername()), PlainTextPassword.from(authenticationRequest.getPassword()))
                .flatMap(user -> Mono.zip(Mono.just(jwtTokenProvider.generateToken(user)), userFacade.findBy(com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(user.getUsername().getRaw()))))
                .map(it -> ResponseEntity.status(HttpStatus.CREATED).body(TokenAndUserResponseBody.from(it.getT1(), userDetails(it.getT2()))))
                .onErrorReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build())
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private UserDetails userDetails(User user) {
        return UserDetails.from(user.getName().getFirstName(), user.getUsername().getRaw());
    }

}
