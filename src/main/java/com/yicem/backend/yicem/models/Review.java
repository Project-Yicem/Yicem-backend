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
    @NonNull
    private String id;

    @NonNull
    private String transactionId;

    @NonNull
    private String comment;

    @NonNull
    private float rating;

}
