package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.security;

enum Role {
    USER("USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    String roleName() {
        return roleName;
    }
}
