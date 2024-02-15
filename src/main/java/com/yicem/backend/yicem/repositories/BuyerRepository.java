package com.yicem.backend.yicem.repositories;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.User;
import jakarta.annotation.PostConstruct;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class BuyerRepository {
    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private final MongoClient client;

    private MongoCollection <Buyer> buyerCollection;

    private MongoCollection <User> userCollection;

    public BuyerRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        userCollection = client.getDatabase("yicem").getCollection("users", User.class);
        buyerCollection = client.getDatabase("yicem").getCollection("buyers", Buyer.class);
    }

    public Buyer save(Buyer buyer){
        buyer.setId(new ObjectId());
        buyerCollection.insertOne(buyer);
        userCollection.insertOne(buyer);
        return buyer;
    }
    
    public List<Buyer> saveAll(List<Buyer> buyer) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                buyer.forEach(p -> p.setId(new ObjectId()));
                buyerCollection.insertMany(clientSession, buyer);
                return buyer;
            }, txnOptions);
        }
    }

    public List<Buyer> findAll() {
        return buyerCollection.find().into(new ArrayList<>());
    }

    public List<Buyer> findAll(List<String> ids) {
        return buyerCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    public Buyer findOne(String id) {
        return buyerCollection.find(eq("_id", new ObjectId(id))).first();
    }

    public long count() {
        return buyerCollection.countDocuments();
    }

    public long delete(String id) {
        return buyerCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> buyerCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> buyerCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    public Buyer update(Buyer buyer) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return buyerCollection.findOneAndReplace(eq("_id", buyer.getId()), buyer, options);
    }

    public long update(List<Buyer> buyers) {
        List<ReplaceOneModel<Buyer>> writes = buyers.stream()
                .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                        p))
                .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> buyerCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }
    
}
