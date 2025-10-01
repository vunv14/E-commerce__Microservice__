package com.example.commonservice.exception.advice;


import com.example.commonservice.exception.model.ErrorMessage;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(Exception e) {
        return new ResponseEntity<>(new ErrorMessage("404", "Resource  not found", HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage("520", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
