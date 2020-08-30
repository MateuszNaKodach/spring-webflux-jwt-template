package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.UserId;

import java.util.UUID;

class UserIdGenerator {

    private final String raw = generate();

    UserId userIdForAuthorization() {
        return UserId.from(raw);
    }

    com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId userIdForManagement() {
        return com.github.nowakprojects.springwebflux.jwttemplate.user.domain.UserId.from(raw);
    }

    private String generate() {
        return UUID.randomUUID().toString();
    }
}
