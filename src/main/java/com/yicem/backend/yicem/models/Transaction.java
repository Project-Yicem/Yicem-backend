package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String buyerId;

    @NonNull
    private String sellerId;

    //TODO: Implement other attributes
}
