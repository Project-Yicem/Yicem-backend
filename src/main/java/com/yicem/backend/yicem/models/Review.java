package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@Setter
@AllArgsConstructor
public class Review {
    @Id
    private String id;

    private String transactionId;

    private String comment;

    private float rating;

    public Review(String transactionId, String comment, float rating) {
        this.transactionId = transactionId;
        this.comment = comment;
        this.rating = rating;
    }
}
