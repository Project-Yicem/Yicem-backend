package com.yicem.backend.yicem.models;

import lombok.*;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    private String id;

    private String buyerId;

    private String sellerId;

    private String itemName;

    private float price;

    private Review review;

    private Date transactionDate;
}
