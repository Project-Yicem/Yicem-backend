package com.yicem.backend.yicem.dtos.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ErrorResponse {
    HttpStatus httpStatus;
    String message;
}
