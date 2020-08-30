package com.github.nowakprojects.springwebflux.jwttemplate.rest;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class UserResponseBody {

    private final String userId;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean isActive;
}
