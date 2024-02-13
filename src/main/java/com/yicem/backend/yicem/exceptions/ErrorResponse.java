package com.yicem.backend.yicem.exceptions;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

    /*ERROR_NOT_FOUND(HttpStatus.NOT_FOUND, "Not found error");

    private final HttpStatus statusCode;
    private final String message;*/
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}




