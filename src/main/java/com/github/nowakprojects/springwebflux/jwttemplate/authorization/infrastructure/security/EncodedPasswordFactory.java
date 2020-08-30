package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.EncodedPassword;
import com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain.PlainTextPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncodedPasswordFactory {

    private final PasswordEncoder passwordEncoder;

    public EncodedPassword encodedPasswordFrom(String plainTextPassword)  {
        return EncodedPassword.from(passwordEncoder.encode(plainTextPassword));
    }

    public EncodedPassword encodedPasswordFrom(PlainTextPassword plainTextPassword) {
        return plainTextPassword.encodeWith(passwordEncoder);
    }
}
