package com.github.nowakprojects.springwebflux.jwttemplate.authorization.domain;

import lombok.Getter;

public abstract class Password {

    @Getter
    protected final String raw;

    protected Password(String raw) {
        this.raw = raw;
    }
}
