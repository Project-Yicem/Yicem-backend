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
import com.yicem.backend.yicem.models.Offer;
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
public class OfferRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private final MongoClient client;

    private MongoCollection<Offer> offerCollection;

    public OfferRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        offerCollection = client.getDatabase("yicem").getCollection("offers", Offer.class);
    }

    public Offer save(Offer offer){
        offer.setId(new ObjectId());
        offerCollection.insertOne(offer);
        return offer;
    }

    public List<Offer> saveAll(List<Offer> offer) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                offer.forEach(p -> p.setId(new ObjectId()));
                offerCollection.insertMany(clientSession, offer);
                return offer;
            }, txnOptions);
        }
    }

    public List<Offer> findAll() {
        return offerCollection.find().into(new ArrayList<>());
    }

    public List<Offer> findAll(List<String> ids) {
        return offerCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    public Offer findOne(String id) {
        return offerCollection.find(eq("_id", new ObjectId(id))).first();
    }

    public long count() {
        return offerCollection.countDocuments();
    }

    public long delete(String id) {
        return offerCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> offerCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> offerCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    public Offer update(Offer offer) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return offerCollection.findOneAndReplace(eq("_id", offer.getId()), offer, options);
    }

    public long update(List<Offer> offers) {
        List<ReplaceOneModel<Offer>> writes = offers.stream()
                .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                        p))
                .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> offerCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }

}

