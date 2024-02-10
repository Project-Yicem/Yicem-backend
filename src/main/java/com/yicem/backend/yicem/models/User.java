package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {

    private ObjectId id;

    private String username;

    private String email;
    
    private String password;

}
