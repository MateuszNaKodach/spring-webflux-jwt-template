package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Name {

    private final String firstName;
    private final String lastName;

    public static Name from(final String firstName, final String lastName) {
        if (StringUtils.isBlank(firstName)) {
            throw new IllegalArgumentException("firstName cannot be empty");
        }
        if (StringUtils.isBlank(lastName)) {
            throw new IllegalArgumentException("lastName cannot be empty");
        }
        return new Name(firstName, lastName);
    }
}
