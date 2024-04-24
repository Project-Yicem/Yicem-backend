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
    private String offerId;
    private float price;
    private Date transactionDate;

    // It will be initialized as NULL and change if buyer makes a review.
    private String review;

    public Transaction(String buyerId, String sellerId, String offerId, float price) {
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.offerId = offerId;
        this.price = price;
        this.transactionDate = new Date();
        this.review = "";
    }
}
