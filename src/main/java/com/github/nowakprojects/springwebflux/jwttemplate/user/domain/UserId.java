package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserId implements Serializable {

    private String raw;

    public static UserId from(final String raw) {
        if (StringUtils.isBlank(raw)) {
            throw new IllegalArgumentException("UserId cannot be empty");
        }
        return new UserId(raw);
    }

}
