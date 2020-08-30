package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security.EncodedPasswordFactory;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Email;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Name;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserAlreadyExistsException;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserDoesNotExistException;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId;
import io.vavr.API;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Slf4j
class UserController {

    private final UserService userService;
    private final EncodedPasswordFactory encodedPasswordFactory;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Flux<UserResponseBody> allUsers() {
        return userService.findAll().map(this::toUserResponseBody);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> create(@RequestBody UserRequestBody user) {
        final UserIdGenerator userIdGenerator = new UserIdGenerator();

        final User userForAuthorization = userForAuthorization(user, userIdGenerator.userIdForAuthorization());
        final com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement = userForManagement(user, userIdGenerator.userIdForManagement());

        return userService.createUser(userForAuthorization, userForManagement);
    }

    @PostMapping("/activation")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> activate(@RequestBody UsernameRequestBody username) {

        final Username usernameForAuthorization = Username.from(username.getUsername());
        final com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username usernameForManagement = com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(username.getUsername());

        return userService.activateUser(usernameForAuthorization, usernameForManagement);
    }

    @PostMapping("/deactivation")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> deactivate(@RequestBody UsernameRequestBody username) {

        final Username usernameForAuthorization = Username.from(username.getUsername());
        final com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username usernameForManagement = com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(username.getUsername());

        return userService.deactivateUser(usernameForAuthorization, usernameForManagement);
    }


    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> update(@PathVariable String userId, @RequestBody UserRequestBody user) {
        final User userForAuthorization = userForAuthorization(user, com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId.from(userId));
        final com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement = userForManagement(user, UserId.from(userId));

        return userService.updateUser(userForAuthorization, userForManagement);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UserAlreadyExistsException.class,
            com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded.UserAlreadyExistsException.class})
    void handleUserAlreadyExistsException(RuntimeException e) {
        log.error("UserAlreadyExistsException ", e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserDoesNotExistException.class,
            com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded.UserDoesNotExistException.class})
    void handleUserDoesNotExistException(RuntimeException e) {
        log.error("UserDoesNotExistException ", e);
    }

    private UserResponseBody toUserResponseBody(com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User user) {
        return UserResponseBody.builder()
                .userId(user.getUserId().getRaw())
                .username(user.getUsername().getRaw())
                .firstName(user.getName().getFirstName())
                .lastName(user.getName().getLastName())
                .email(user.getEmail().getRaw())
                .isActive(com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE.equals(user.getActivity()))
                .build();
    }

    private com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement(final UserRequestBody user, UserId userId) {
        return com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User.builder()
                .userId(userId)
                .username(com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(user.getUsername()))
                .email(Email.from(user.getEmail()))
                .name(Name.from(user.getFirstName(), user.getLastName()))
                .activity(Match(user.isActive()).of(
                                Case($(true), com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE),
                                Case($(false), com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.INACTIVE)
                        ))
                .build();
    }

    private User userForAuthorization(final UserRequestBody user,
                                      com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId userId) {
        return User.from(
                userId,
                Username.from(user.getUsername()),
                encodedPasswordFactory.encodedPasswordFrom(user.getPassword()),
                Match(user.isActive()).of(
                        API.Case($(true), Activity.ACTIVE),
                        API.Case($(false), Activity.INACTIVE)
                ));
    }

}
