package com.yicem.backend.yicem.dtos;

import com.yicem.backend.yicem.models.Review;
import org.bson.types.ObjectId;

public record ReviewDTO(
        String id,
        String buyerId,
        String sellerId,
        String comment,
        float rating) {

    public ReviewDTO(Review r) {
        this(r.getId() == null ? ObjectId.get().toHexString() : r.getId().toHexString(),
                r.getBuyerId().toHexString(), r.getSellerId().toHexString(),
                r.getComment(), r.getRating());
    }

    public Review toReview() {
        ObjectId _id = id == null ? new ObjectId() : new ObjectId(id);
        return Review.builder()
                .id(_id)
                .buyerId(new ObjectId(buyerId))
                .sellerId(new ObjectId(sellerId))
                .comment(comment)
                .rating(rating)
                .build();
    }

}
