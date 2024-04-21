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

    // These will hold IDs instead of objects. They will initialized as Empty Lists.
    private List<String> reviews;
    private List<String> pastTransactions;
    private List<String> supportReports;
    private List<String> favoriteSellers;

    public Buyer(String id, String username) {
        this.id = id;
        this.username = username;
        this.reviews = new ArrayList<>();
        this.pastTransactions = new ArrayList<>();
        this.supportReports = new ArrayList<>();
        this.favoriteSellers = new ArrayList<>();
    }

    public boolean addReview(String review) {
        if(!reviews.contains(review)) {
            reviews.add(review);
            return true;
        }
        return false;
    }

    public boolean addTransaction(String transaction) {
        if(!pastTransactions.contains(transaction)) {
            pastTransactions.add(transaction);
            return true;
        }
        return false;
    }

    public boolean addSupportReport(String report) {
        if(!supportReports.contains(report)) {
            supportReports.add(report);
            return true;
        }
        return false;
    }

    public boolean addFavoriteSeller(String seller) {
        if(!favoriteSellers.contains(seller)) {
            favoriteSellers.add(seller);
            return true;
        }
        return false;
    }

}
