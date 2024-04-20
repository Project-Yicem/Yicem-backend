package com.yicem.backend.yicem.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PasswordChangeRequest {
    String oldPassword;
    String newPassword;
}
