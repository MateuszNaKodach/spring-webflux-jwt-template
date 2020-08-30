package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import lombok.Value;

@Value
class UserRequestBody {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final boolean isActive;
}
