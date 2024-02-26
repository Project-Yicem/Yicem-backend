package com.yicem.backend.yicem.models;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Buyer extends User {
    private List<ObjectId> reviews;
    private List<ObjectId> pastTransactions;
    private List<ObjectId> supportReports;
    private List<ObjectId> favoriteSellers;
    //TODO: Achievement attribute eklenecek.
    @Builder
    public Buyer (ObjectId id, String username, String email, String password, List<ObjectId> reviews,
                  List<ObjectId> pastTransactions, List<ObjectId> supportReports, List<ObjectId> favoriteSellers) {
        super(id, username, email, password);
        this.reviews = reviews;
        this.pastTransactions = pastTransactions;
        this.supportReports = supportReports;
        this.favoriteSellers = favoriteSellers;
    }
}
