package com.github.nowakprojects.springwebflux.jwttemplate.authorization.presentation;

import lombok.Value;

@Value
class AuthenticationRequest {
    private final String username;
    private final String password;
}
