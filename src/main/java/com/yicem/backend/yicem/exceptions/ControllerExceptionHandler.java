package com.yicem.backend.yicem.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.yicem.backend.yicem.exceptions.ErrorResponse;
import com.yicem.backend.yicem.exceptions.ResourceNotFoundException;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {
    /*@ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handlePostException(final ResourceNotFoundException exp){
        log.warn("Exception occured " + exp);

        return this.makeErrorResponseEntity(exp.getErrorResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final ErrorMessage msg){
        return ResponseEntity.status(msg.getStatusCode())
                .body(new ErrorResponse(msg.name(),msg.getMessage()));
    }

    @Getter
    @RequiredArgsConstructor
    static class ErrorResponse{
        private final String code;
        private final String message;
    }*/
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookNotFoundException(ResourceNotFoundException ex) {
        return new ErrorResponse("Buyer not found: " + ex.getMessage());
    }

}
