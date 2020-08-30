package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@EqualsAndHashCode(callSuper = false)
@Value
public class PlainTextPassword extends Password {

    private PlainTextPassword(String raw) {
        super(raw);
    }

    public static PlainTextPassword from(final String raw) {
        if (StringUtils.isBlank(raw)) {
            throw new IllegalArgumentException("password cannot be empty");
        }
        return new PlainTextPassword(raw);
    }
    public EncodedPassword encodeWith(PasswordEncoder encoder) {
        return EncodedPassword.from(encoder.encode(raw));
    }
}
