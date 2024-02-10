package com.yicem.backend.yicem.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ToString
public class Buyer extends User {
    //private List<Review> reviews;
    //private List<Transaction> pastTransactions;
    //private List<Report> supportReports;
    //private List<Seller> favoriteSellers;
    //TODO: Achievement attribute eklenecek.
    @Builder
    public Buyer (ObjectId id, String username, String email, String password) {
        super(id, username, email, password);
    }
}
