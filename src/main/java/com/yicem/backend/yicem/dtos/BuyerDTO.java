package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.Seller;
import org.bson.types.ObjectId;

public record BuyerDTO(
        String id,
        String username,
        String email,
        String password){

    public BuyerDTO(Buyer b) {
        this(b.getId() == null ? ObjectId.get().toHexString() : b.getId().toHexString(), b.getUsername(),
                b.getEmail(), b.getPassword());
    }

    public Buyer toBuyer(){
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Buyer.builder()
                .id(_id)
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}

