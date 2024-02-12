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
import com.yicem.backend.yicem.models.Seller;
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
public class SellerRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private final MongoClient client;

    private MongoCollection<Seller> sellerCollection;

    public SellerRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        sellerCollection = client.getDatabase("yicem").getCollection("sellers", Seller.class);
    }

    public Seller save(Seller seller){
        seller.setId(new ObjectId());
        sellerCollection.insertOne(seller);
        return seller;
    }

    public List<Seller> saveAll(List<Seller> seller) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                seller.forEach(p -> p.setId(new ObjectId()));
                sellerCollection.insertMany(clientSession, seller);
                return seller;
            }, txnOptions);
        }
    }

    public List<Seller> findAll() {
        return sellerCollection.find().into(new ArrayList<>());
    }

    public List<Seller> findAll(List<String> ids) {
        return sellerCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    public Seller findOne(String id) {
        return sellerCollection.find(eq("_id", new ObjectId(id))).first();
    }

    public long count() {
        return sellerCollection.countDocuments();
    }

    public long delete(String id) {
        return sellerCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> sellerCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> sellerCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    public Seller update(Seller seller) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return sellerCollection.findOneAndReplace(eq("_id", seller.getId()), seller, options);
    }

    public long update(List<Seller> sellers) {
        List<ReplaceOneModel<Seller>> writes = sellers.stream()
                .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                        p))
                .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> sellerCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }

}
