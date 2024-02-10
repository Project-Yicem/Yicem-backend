package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder
public class User {

    private ObjectId id;

    private String username;

    private String email;
    
    private String password;

    public User( ) {}

}
