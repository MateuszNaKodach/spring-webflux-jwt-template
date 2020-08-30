package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Value
@Document("userCredentials")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    private final UserId userId;
    private final Username username;
    private final Password password;
    private final Activity activity;

    public static User from(final UserId userId, final Username username, final Password password, final Activity activity) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(password, "password cannot be null");
        Objects.requireNonNull(activity, "activity cannot be null");
        return new User(userId, username, password, activity);
    }

    public User activate() {
        if (activity != Activity.INACTIVE) {
            throw new IllegalArgumentException("user " + username.getRaw() + " is already active");
        }
        return User.from(userId, username, password, Activity.ACTIVE);
    }

    public User deactivate() {
        if (activity != Activity.ACTIVE) {
            throw new IllegalArgumentException("user " + username.getRaw() + " is already inactive");
        }
        return User.from(userId, username, password, Activity.INACTIVE);
    }
}
