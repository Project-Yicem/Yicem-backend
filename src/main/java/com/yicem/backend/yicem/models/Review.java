package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;

    private String transactionId;

    private String comment;

    private int rating;

    public Review(String transactionId, String comment, int rating) {
        this.transactionId = transactionId;
        this.comment = comment;
        this.rating = rating;
    }
}
