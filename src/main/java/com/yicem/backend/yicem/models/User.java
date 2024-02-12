package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private ObjectId id;

    private String username;

    private String email;
    
    private String password;

}
