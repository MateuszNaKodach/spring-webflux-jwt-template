package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UserFacadeTest {

    @Nested
    @DisplayName("given no saved users")
    class GivenNoSavedUsers {

        final UserRepository repo = new InMemoryUserRepository();
        final UserFacade userFacade = new UserFacade(repo);
        final UserId userId = UserId.from("12345");
        final User newUser = User.from(userId, Username.from("SampleUsername"), Name.from("Janina", "Kowalska"), Email.from("JaninaKowalska@GitHub.com"), Activity.ACTIVE);

        @Nested
        @DisplayName("when user is created")
        class WhenUserIsSaved {
            {
                userFacade.create(newUser).block();
            }

            @Test
            @DisplayName("then user is present in repo with provided Id")
            void thenUserIsPresentInRepoWithProvidedId() {
                final User createdUser = repo.findBy(userId).block();
                assertThat(createdUser.getUserId()).isEqualTo(userId);
            }
        }

        @Nested
        @DisplayName("when user with unknown id is updated")
        class WhenUserWithUnknownIdIsUpdated {
            final Throwable thrown = catchThrowable(() -> userFacade.update(newUser).block());

            @Test
            @DisplayName("then exception is thrown")
            void thenExceptionIsThrown() {

                assertThat(thrown).isInstanceOf(UserDoesNotExistException.class)
                        .hasMessage("user with provided userId 12345 doesn't exist");
            }
        }

        @Nested
        @DisplayName("when find all")
        class WhenFindAll {
            final List<User> allUsers = userFacade.findAll().collectList().block();

            @Test
            @DisplayName("then no users found")
            void thenNoUsersFound() {
                assertThat(allUsers).isEmpty();
            }
        }
    }

    @Nested
    @DisplayName("given user already saved")
    class GivenUserAlreadySaved {

        final InMemoryUserRepository repo = new InMemoryUserRepository();
        final UserFacade userFacade = new UserFacade(repo);
        final UserId userId = UserId.from("12345");
        final User newUser = User.from(userId, Username.from("SampleUsername"), Name.from("Janina", "Kowalska"), Email.from("JaninaKowalska@GitHub.com"), Activity.ACTIVE);

        {
            userFacade.create(newUser).block();
        }

        @Nested
        @DisplayName("when user with same username is created")
        class WhenUserWithSameIdIsCreated {
            final Throwable thrown = catchThrowable(() -> userFacade.create(newUser).block());

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
            final Name changedName = Name.from("Janina", "GitHub");
            final User userToUpdate = User.from(userId, Username.from("SampleUsername"), changedName, Email.from("JaninaKowalska@GitHub.com"), Activity.ACTIVE);

            {
                userFacade.update(userToUpdate).block();
            }

            @Test
            @DisplayName("then user is present in repo with changed name")
            void thenUserIsPresentInRepoWithChangedName() {
                final User createdUser = repo.findBy(userId).block();

                assertThat(createdUser.getName()).isEqualTo(changedName);
                assertThat(repo.count().block()).isEqualTo(1);
            }
        }

        @Nested
        @DisplayName("when find all")
        class WhenFindAll {
            final List<User> allUsers = userFacade.findAll().collectList().block();

            @Test
            @DisplayName("then 1 user found")
            void then1UserFound() {
                assertThat(allUsers).hasSize(1);
            }
        }
    }
}
