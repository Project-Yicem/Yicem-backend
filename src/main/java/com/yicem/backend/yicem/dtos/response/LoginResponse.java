package com.yicem.backend.yicem.dtos.response;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String token;
}
