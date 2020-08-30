package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

    private final String raw;

    public static Email from(final String emailAsText) {
        if (StringUtils.isBlank(emailAsText)) {
            throw new IllegalArgumentException("email cannot be empty");
        }
        return new Email(emailAsText);
    }
}
