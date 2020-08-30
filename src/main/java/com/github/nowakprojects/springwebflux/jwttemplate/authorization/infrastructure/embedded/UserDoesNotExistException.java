package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;

public class UserDoesNotExistException extends RuntimeException {

    public UserDoesNotExistException(String message) {
        super(message);
    }

}
