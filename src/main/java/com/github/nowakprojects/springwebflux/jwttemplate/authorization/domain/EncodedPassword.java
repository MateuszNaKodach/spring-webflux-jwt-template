package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode(callSuper = false)
@Value
public class EncodedPassword extends Password {

    private EncodedPassword(String raw) {
        super(raw);
    }

    public static EncodedPassword from(final String raw) {
        if (StringUtils.isBlank(raw)) {
            throw new IllegalArgumentException("password cannot be empty");
        }
        return new EncodedPassword(raw);
    }
}
