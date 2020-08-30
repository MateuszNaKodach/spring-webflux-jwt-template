package com.github.nowakprojects.springwebflux.jwttemplate.config;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
class GlobalHttpExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessage.from(e.getLocalizedMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalStateException e) {
        log.error("IllegalStateException ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.from(e.getLocalizedMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorMessage> handleIllegalArgumentException(RuntimeException e) {
        log.error("RuntimeException ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.from(e.getLocalizedMessage()));
    }

    @Value
    @RequiredArgsConstructor(staticName = "from")
    private static class ErrorMessage {
        private final String message;
    }
}
