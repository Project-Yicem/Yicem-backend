package com.yicem.backend.yicem.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yicem.backend.yicem.models.Review;
import com.yicem.backend.yicem.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    private String id;

    private String buyerId;
    private String buyerName;
    private String sellerId;
    private String sellerName;
    private String offerId;
    private String offerName;
    private float price;
    private Date transactionDate;
    private Review review;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.buyerId = transaction.getBuyerId();
        this.sellerId = transaction.getSellerId();
        this.offerId = transaction.getOfferId();
        this.price = transaction.getPrice();
        this.transactionDate = transaction.getTransactionDate();
    }
}
