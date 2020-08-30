package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring-boot-webflux-jwttemplate.jjwt")
@Setter
class JwtTokenConfig {

    private String secret;
    private String expirationTime; //in seconds

    String secret() {
        return secret;
    }

    String expirationTime() {
        return expirationTime;
    }
}
