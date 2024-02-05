package com.yicem.backend.yicem.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder
public class User {
    @Id
    private String userID;

    private String username;

    private String email;
    
    private String password;

}
