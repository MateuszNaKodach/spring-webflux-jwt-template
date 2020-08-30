package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import com.github.nowakprojects.springwebflux.jwttemplate.TestTags;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security.EncodedPasswordFactory;
import com.github.nowakprojects.springwebflux.jwttemplate.rest.backoffice.UsernameRequestBody;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Email;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@Tag(TestTags.EMBEDDED_MONGO_DB)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EncodedPasswordFactory encodedPasswordFactory;

    @Autowired
    private UserService userService;

    @DisplayName("given saved active user")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GivenSavedActiveUser {

        final String userNameRaw = "test-user-activation";
        final String userIdRaw = "123";

        {
            userService.createUser(userForAuthorisation(userIdRaw, userNameRaw), userForManagement(userIdRaw, userNameRaw)).block();
        }

        @DisplayName("when activate active user then expected error")
        @Test
        void whenActivateActiveUserThenExpectError() {
            webTestClient
                    .post()
                    .uri("/user/activation")
                    .body(Mono.just(new UsernameRequestBody(userNameRaw)), UsernameRequestBody.class)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("message").isEqualTo("user test-user-activation is already active");
        }

        @DisplayName("when deactivate active user then expected success")
        @Test
        void whenDeactivateActiveUserThenExpectError() {
            webTestClient
                    .post()
                    .uri("/user/deactivation")
                    .body(Mono.just(new UsernameRequestBody(userNameRaw)), UsernameRequestBody.class)
                    .exchange()
                    .expectStatus().isCreated();
        }
    }

    @DisplayName("given saved active user")
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class GivenSavedInactiveUser {

        final String userNameRaw = "test-user-deactivation";
        final String userIdRaw = "1234";

        {
            userService.createUser(userForAuthorisation(userIdRaw, userNameRaw), userForManagement(userIdRaw, userNameRaw)).block();
            userService.deactivateUser(Username.from(userNameRaw), com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(userNameRaw)).block();
        }

        @DisplayName("when deactivate inactive user")
        @Test
        void whenDeactivateInactiveUserThenExpectError() {
            webTestClient
                    .post()
                    .uri("/user/deactivation")
                    .body(Mono.just(new UsernameRequestBody(userNameRaw)), UsernameRequestBody.class)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("message").isEqualTo("user test-user-deactivation is already inactive");
        }

        @DisplayName("when activate inactive user")
        @Test
        void whenActivateInactiveUserThenExpectSuccess() {
            webTestClient
                    .post()
                    .uri("/user/activation")
                    .body(Mono.just(new UsernameRequestBody(userNameRaw)), UsernameRequestBody.class)
                    .exchange()
                    .expectStatus().isCreated();
        }
    }

    private com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement(String userIdRaw, String userNameRaw) {
        return com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User.from(
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId.from(userIdRaw),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(userNameRaw),
                Name.from("Firstname", "Lastname"),
                Email.from("github@github.com"),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE);
    }

    private com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User userForAuthorisation(String userIdRaw, String userNameRaw) {
        return com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User.from(UserId.from(userIdRaw), Username.from(userNameRaw), encodedPasswordFactory.encodedPasswordFrom("password"), Activity.ACTIVE);
    }
}
