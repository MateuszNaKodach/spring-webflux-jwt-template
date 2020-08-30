package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Value
@Builder
public class User {

    @Id
    private final UserId userId;
    private final Username username;
    private final Name name;
    private final Email email;
    private final Activity activity;

    public static User from(final UserId userId, final Username username, final Name name, final Email email, Activity activity) {
        return new User(userId, username, name, email, activity);
    }

    private User(final UserId userId, final Username username, final Name name, final Email email, Activity activity) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(username, "username cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(activity, "activity cannot be null");

        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.activity = activity;
    }

    User deactivate() {
        if (activity != Activity.ACTIVE) {
            throw new IllegalArgumentException("user " + username.getRaw() + " is already inactive");
        }
        return User.from(userId, username, name, email, Activity.INACTIVE);
    }

    User activate() {
        if (activity != Activity.INACTIVE) {
            throw new IllegalArgumentException("user " + username.getRaw() + " is already active");
        }
        return User.from(userId, username, name, email, Activity.ACTIVE);
    }
}
