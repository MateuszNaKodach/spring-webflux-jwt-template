package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Activity;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.PlainTextPassword;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.User;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class AuthorizationSaverTest {
    @Nested
    @DisplayName("given no saved users")
    class GivenNoSavedUsers {

        final AuthorizationRepository repo = new InMemoryAuthorizationRepository();
        final AuthorizationSaver authorizationSaver = new AuthorizationSaver(repo);
        private final UserId userId = UserId.from("12345");
        private final Username username = Username.from("SampleUsername");
        final User newUser = User.from(userId, username, PlainTextPassword.from("123"), Activity.ACTIVE);

        @Nested
        @DisplayName("when user is created")
        class WhenUserIsSaved {
            {
                authorizationSaver.create(newUser).block();
            }

            @Test
            @DisplayName("then user is present in repo with provided Id")
            void thenUserIsPresentInRepoWithProvidedId() {
                final User createdUser = repo.findBy(username).block();
                assertThat(createdUser.getUsername()).isEqualTo(username);
            }
        }

        @Nested
        @DisplayName("when user with unknown id is updated")
        class WhenUserWithUnknownIdIsUpdated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.update(newUser).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(UserDoesNotExistException.class)
                        .hasMessage("user with provided userId 12345 doesn't exist");
            }
        }

        @Nested
        @DisplayName("when user with unknown username is activated")
        class WhenUserWithUnknownUsernameIsActivated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.activate(username).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(UserDoesNotExistException.class)
                        .hasMessage("user with provided username SampleUsername doesn't exists");
            }
        }

        @Nested
        @DisplayName("when user with unknown username is deactivated")
        class WhenUserWithUnknownUsernameIsDeactivated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.deactivate(username).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(UserDoesNotExistException.class)
                        .hasMessage("user with provided username SampleUsername doesn't exists");
            }
        }

    }

    @Nested
    @DisplayName("given user already saved")
    class GivenUserAlreadySaved {

        final AuthorizationRepository repo = new InMemoryAuthorizationRepository();
        final AuthorizationSaver authorizationSaver = new AuthorizationSaver(repo);
        private final UserId userId = UserId.from("12345");
        private final Username username = Username.from("SampleUsername");
        final User newUser = User.from(userId, username, PlainTextPassword.from("123"), Activity.ACTIVE);

        {
            authorizationSaver.create(newUser).block();
        }

        @Nested
        @DisplayName("when user with same username is created")
        class WhenUserWithSameIdIsCreated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.create(newUser).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {
                assertThat(thrown)
                        .isInstanceOf(UserAlreadyExistsException.class)
                        .hasMessage("user with provided username SampleUsername already exists");
            }
        }

        @Nested
        @DisplayName("when user is updated using id")
        class WhenUserIsUpdatedUsingId {
            final Username updatedUsername = Username.from("SampleUsername2");
            final User userToUpdate = User.from(userId, updatedUsername, PlainTextPassword.from("1234"), Activity.ACTIVE);

            {
                authorizationSaver.update(userToUpdate).block();
            }

            @Test
            @DisplayName("then user is present in repo with changed name")
            void thenUserIsPresentInRepoWithChangedName() {
                final User createdUser = repo.findBy(updatedUsername).block();

                assertThat(createdUser.getUsername()).isEqualTo(updatedUsername);
            }
        }

        @Nested
        @DisplayName("when active user is deactivated")
        class WhenActiveUserIsDeactivated {
            {
                authorizationSaver.deactivate(username).block();
            }

            @Test
            @DisplayName("then user is inactive")
            void thenUserIsPresentInRepoWithChangedName() {
                final User createdUser = repo.findBy(username).block();

                assertThat(createdUser.getActivity()).isEqualTo(Activity.INACTIVE);
            }
        }

        @Nested
        @DisplayName("when active user is activated")
        class WhenActiveUserIsActivated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.activate(username).block());

            @Test
            @DisplayName("then user is inactive")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("user SampleUsername is already active");
            }
        }

    }

    @Nested
    @DisplayName("given saved user is inactive")
    class GivenSavedUserIsInactive {

        final AuthorizationRepository repo = new InMemoryAuthorizationRepository();
        final AuthorizationSaver authorizationSaver = new AuthorizationSaver(repo);
        private final UserId userId = UserId.from("12345");
        private final Username username = Username.from("SampleUsername");
        final User newUser = User.from(userId, username, PlainTextPassword.from("123"), Activity.ACTIVE);

        {
            authorizationSaver.create(newUser).block();
            authorizationSaver.deactivate(username).block();
        }

        @Nested
        @DisplayName("when inactive user is activated")
        class WhenInactiveUserIsActivated {
            {
                authorizationSaver.activate(username).block();
            }

            @Test
            @DisplayName("then user is inactive")
            void thenUserIsInactive() {
                final User createdUser = repo.findBy(username).block();

                assertThat(createdUser.getActivity()).isEqualTo(Activity.ACTIVE);
            }
        }

        @Nested
        @DisplayName("when inactive user is deactivated")
        class WhenInactiveUserIsDeactivated {
            final Throwable thrown = catchThrowable(() -> authorizationSaver.deactivate(username).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("user SampleUsername is already inactive");
            }
        }

    }
}
