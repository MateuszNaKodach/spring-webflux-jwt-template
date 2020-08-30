package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.PlainTextPassword;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("EOF process error while starting embedded mongo db")
@SpringBootTest
@Tag("integration")
class UserInMongoDbIntegrationTest {

    @Autowired
    private AuthorizationRepository repository;

    @DisplayName("given new user to create")
    @Nested
    class GivenNewUserToCreate {
        final Username username = Username.from("username");
        final User newUser = user(username);

        @DisplayName("when creating new user")
        @Nested
        class WhenCreatingNewUser {
            {
                repository.save(newUser).block();
            }

            @Test
            @DisplayName("then user is created and can be retrieved form database")
            void thenUserIsCreatedAndCanBeRetrievedFormDatabase() {
                final User createdUser = repository.findBy(username).block();

                assertThat(createdUser).isNotNull();
            }
        }
    }

    private User user(Username username) {
        return User.from(UserId.from("userId"), username, PlainTextPassword.from("123"), Activity.ACTIVE);
    }
}
