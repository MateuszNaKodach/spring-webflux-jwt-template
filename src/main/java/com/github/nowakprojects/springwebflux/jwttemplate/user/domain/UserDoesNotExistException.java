package com.github.nowakprojects.springwebflux.jwttemplate.user.domain;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(String message) {
        super(message);
    }

}
