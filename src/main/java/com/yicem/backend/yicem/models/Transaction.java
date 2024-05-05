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
    private String buyerName;
    private String sellerId;
    private String sellerName;
    private String offerId;
    private String offerName;
    private float price;
    private Date transactionDate;

    // It will be initialized as NULL and change if buyer makes a review.
    private String review;

    public Transaction(String buyerId, String buyerName, String sellerId, String sellerName, String offerId,
                       String offerName, float price) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.offerId = offerId;
        this.offerName = offerName;
        this.price = price;
        this.transactionDate = new Date();
        this.review = "";
    }
}
