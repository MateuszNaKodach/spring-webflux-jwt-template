package com.github.nowakprojects.springwebflux.jwttemplate.user.infrastructure;

import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Email;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Name;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserRepository;
import com.github.nowakprojects.springwebflux.jwttemplate.user.domain.Username;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("EOF process error while starting embedded mongo db")
@SpringBootTest
@Tag("integration")
class UserInMongoDbIntegrationTest {

    @Autowired
    private UserRepository repository;

    @DisplayName("given new user to create")
    @Nested
    class GivenNewUserToCreate {
        final UserId userId = UserId.from("userId");
        final User newUser = user(userId);

        @DisplayName("when creating new user")
        @Nested
        class WhenCreatingNewUser {
            {
                repository.create(newUser).block();
            }

            @Test
            @DisplayName("then user is created and can be retrieved form database")
            void thenUserIsCreatedAndCanBeRetrievedFormDatabase() {
                final User createdUser = repository.findBy(userId).block();

                assertThat(createdUser).isNotNull();
            }
        }
    }

    private User user(UserId userId) {
        return User.from(userId, Username.from("username"), Name.from("firstname", "lastname"), Email.from("email.123@github.com"), Activity.ACTIVE);
    }
}
