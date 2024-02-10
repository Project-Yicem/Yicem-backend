package com.yicem.backend.yicem.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.yicem.backend.yicem.models.Buyer;
import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public class BuyerRepository {
    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private MongoClient client;

    private MongoCollection <Buyer> buyerCollection;

    public BuyerRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        buyerCollection = client.getDatabase("yicem").getCollection("buyers", Buyer.class);
    }

    public Buyer save(Buyer buyer){
        buyer.setId(new ObjectId());
        buyerCollection.insertOne(buyer);
        return buyer;
    }



}
