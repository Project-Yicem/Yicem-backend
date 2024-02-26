package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Review {

    private ObjectId id;
    private ObjectId buyerId;
    private ObjectId sellerId;
    private String comment;
    private float rating;

    @Builder
    public Review(ObjectId id, ObjectId buyerId, ObjectId sellerId, String comment, float rating) {
        this.id = id;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.comment = comment;
        this.rating = rating;
    }
}
