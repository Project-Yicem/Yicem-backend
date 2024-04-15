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
public class Buyer extends User{

    private List<Review> reviews;

    private List<Transaction> pastTransactions;

    private List<Report> supportReport;

    private List<Seller> favoriteSellers;

    public Buyer(String id, String username, String email, String password){
        super(username, email, password);
        this.setId(id);
    }

}
