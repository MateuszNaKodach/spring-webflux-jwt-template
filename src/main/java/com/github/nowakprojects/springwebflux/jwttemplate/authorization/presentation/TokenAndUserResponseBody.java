package com.github.nowakprojects.springwebflux.jwttemplate.authorization.presentation;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "from")
class TokenAndUserResponseBody {
    private final String token;
    private final UserDetails user;
}
