package com.william.exception.handler;

import com.william.exception.InvalidAccountException;
import com.william.exception.InvalidAmountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity handleInvalidAccountException(InvalidAccountException exception) {
        log.error("Invalid Account", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Account");
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity handleInvalidAmountException(InvalidAmountException exception) {
        log.error("Invalid Amount", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Amount");
    }
}
