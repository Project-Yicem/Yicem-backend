package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.User;
import org.bson.types.ObjectId;

public record UserDTO(
        String id,
        String username,
        String email,
        String password) {

    public UserDTO(User u) {
        this(u.getId() == null ? new ObjectId().toHexString() : u.getId().toHexString(), u.getUsername(),
                u.getEmail(), u.getPassword());
    }

    public User toUser() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return User.builder()
                .id(_id)
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

}
