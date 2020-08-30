package com.github.nowakprojects.springwebflux.jwttemplate.authorization.presentation;

import com.github.nowakprojects.springwebflux.jwttemplate.TestTags;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security.EncodedPasswordFactory;
import com.github.nowakprojects.springwebflux.jwttemplate.rest.UserService;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Email;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


@Tag(TestTags.EMBEDDED_MONGO_DB)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EncodedPasswordFactory encodedPasswordFactory;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("when user admin-admin is authenticated, then token and user name is returned")
    void messageWhenAuthenticated() {

        // given
        final String userNameRaw = "test-user";
        final String userIdRaw = "123";

        final User userForAuthorisation = User.from(UserId.from(userIdRaw), Username.from(userNameRaw), encodedPasswordFactory.encodedPasswordFrom("password"), Activity.ACTIVE);

        final com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User userForManagement = com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User.from(
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId.from(userIdRaw),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username.from(userNameRaw),
                Name.from("Firstname", "Lastname"),
                Email.from("github@github.com"),
                com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity.ACTIVE);

        userService.createUser(userForAuthorisation, userForManagement).block();

        // when
        this.webTestClient
                .post()
                .uri("/token")
                .body(Mono.just(new AuthenticationRequest("test-user", "password")), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.token").isNotEmpty()
                .jsonPath("$.user").isNotEmpty()
                .jsonPath("$.user.username").isEqualTo("test-user")
                .jsonPath("$.user.name").isEqualTo("Firstname");
    }

    @Test
    @DisplayName("when user nonexisting-user is not authenticated, then 401 - unauthorized - is returned")
    void messageWhenNotAuthenticated() {
        this.webTestClient
                .post()
                .uri("/token")
                .body(Mono.just(new AuthenticationRequest("nonexisting", "user")), AuthenticationRequest.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

}
