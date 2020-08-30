package com.github.nowakprojects.springwebflux.jwttemplate.authorization.infrastructure.embedded;


public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
