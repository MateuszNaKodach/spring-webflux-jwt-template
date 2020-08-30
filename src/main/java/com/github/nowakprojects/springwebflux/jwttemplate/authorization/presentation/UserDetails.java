package com.github.nowakprojects.springwebflux.jwttemplate.authorization.presentation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "from")
class UserDetails {
    @JsonProperty("name")
    private final String firstName;
    private final String username;
}
