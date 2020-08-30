package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.Value;

@Value
public class AuthenticationUser {
    private final UserId userId;
    private final Username username;
}
