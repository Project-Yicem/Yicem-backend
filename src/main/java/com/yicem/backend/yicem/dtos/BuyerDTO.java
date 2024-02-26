package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Buyer;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public record BuyerDTO(
        String id,
        String username,
        String email,
        String password,
        List<String> reviews,
        List<String> pastTransactions,
        List<String> supportReports,
        List<String> favoriteSellers) {

    public BuyerDTO(Buyer b) {
        this(b.getId() == null ? ObjectId.get().toHexString() : b.getId().toHexString(), b.getUsername(),
                b.getEmail(), b.getPassword(),
                b.getReviews().stream().map(ObjectId::toHexString).collect(Collectors.toList()),
                b.getPastTransactions().stream().map(ObjectId::toHexString).collect(Collectors.toList()),
                b.getSupportReports().stream().map(ObjectId::toHexString).collect(Collectors.toList()),
                b.getFavoriteSellers().stream().map(ObjectId::toHexString).collect(Collectors.toList()));
    }

    public Buyer toBuyer(){
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Buyer.builder()
                .id(_id)
                .username(username)
                .email(email)
                .password(password)
                .reviews(reviews.stream().map(ObjectId::new).collect(Collectors.toList()))
                .pastTransactions(pastTransactions.stream().map(ObjectId::new).collect(Collectors.toList()))
                .supportReports(supportReports.stream().map(ObjectId::new).collect(Collectors.toList()))
                .favoriteSellers(favoriteSellers.stream().map(ObjectId::new).collect(Collectors.toList()))
                .build();
    }
}

