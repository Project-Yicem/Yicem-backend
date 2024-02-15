package com.yicem.backend.yicem.dtos.request;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRequest {
    private String email;

    private String password;
}
