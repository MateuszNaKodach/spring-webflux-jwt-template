package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
