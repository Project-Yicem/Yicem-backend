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
import com.yicem.backend.yicem.models.Review;
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
public class ReviewRepository {

    private static final TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.MAJORITY)
            .writeConcern(WriteConcern.MAJORITY)
            .build();

    private final MongoClient client;

    private MongoCollection<Review> reviewCollection;

    public ReviewRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        reviewCollection = client.getDatabase("yicem").getCollection("reviews", Review.class);
    }

    public Review save(Review review){
        review.setId(new ObjectId());
        reviewCollection.insertOne(review);
        return review;
    }

    public List<Review> saveAll(List<Review> review) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(() -> {
                review.forEach(p -> p.setId(new ObjectId()));
                reviewCollection.insertMany(clientSession, review);
                return review;
            }, txnOptions);
        }
    }

    public List<Review> findAll() {
        return reviewCollection.find().into(new ArrayList<>());
    }

    public List<Review> findAll(List<String> ids) {
        return reviewCollection.find(in("_id", mapToObjectIds(ids))).into(new ArrayList<>());
    }

    public Review findOne(String id) {
        return reviewCollection.find(eq("_id", new ObjectId(id))).first();
    }

    public long count() {
        return reviewCollection.countDocuments();
    }

    public long delete(String id) {
        return reviewCollection.deleteOne(eq("_id", new ObjectId(id))).getDeletedCount();
    }

    public long delete(List<String> ids) {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> reviewCollection.deleteMany(clientSession, in("_id", mapToObjectIds(ids))).getDeletedCount(),
                    txnOptions);
        }
    }

    public long deleteAll() {
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> reviewCollection.deleteMany(clientSession, new BsonDocument()).getDeletedCount(), txnOptions);
        }
    }

    public Review update(Review review) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER);
        return reviewCollection.findOneAndReplace(eq("_id", review.getId()), review, options);
    }

    public long update(List<Review> reviews) {
        List<ReplaceOneModel<Review>> writes = reviews.stream()
                .map(p -> new ReplaceOneModel<>(eq("_id", p.getId()),
                        p))
                .toList();
        try (ClientSession clientSession = client.startSession()) {
            return clientSession.withTransaction(
                    () -> reviewCollection.bulkWrite(clientSession, writes).getModifiedCount(), txnOptions);
        }
    }

    private List<ObjectId> mapToObjectIds(List<String> ids) {
        return ids.stream().map(ObjectId::new).toList();
    }

}
