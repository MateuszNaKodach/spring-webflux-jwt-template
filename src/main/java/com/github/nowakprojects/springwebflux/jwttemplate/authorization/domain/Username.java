package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Username {
    private final String raw;

    public static Username from(String raw) {
        if (StringUtils.isBlank(raw)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        return new Username(raw);
    }
}
