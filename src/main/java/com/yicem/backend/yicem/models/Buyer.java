package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "buyers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Buyer {
    @Id
    private String id;

    private String username;

    private List<String> reviews;

    private List<Transaction> pastTransactions;

    private List<Report> supportReport;

    private List<Seller> favoriteSellers;

    public Buyer(String id, String username) {
        this.id = id;
        this.username = username;
        this.reviews = new ArrayList<>();
        this.pastTransactions = new ArrayList<>();
        this.supportReport = new ArrayList<>();
        this.favoriteSellers = new ArrayList<>();
    }

    public void addReview(String review) {
        reviews.add(review);
    }

}
