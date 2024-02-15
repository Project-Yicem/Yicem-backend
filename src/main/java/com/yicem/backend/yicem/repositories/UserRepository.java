package com.yicem.backend.yicem.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.yicem.backend.yicem.models.Buyer;
import com.yicem.backend.yicem.models.User;
import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class UserRepository {

    private final MongoClient client;
    private MongoCollection<User> userCollection;

    public UserRepository(MongoClient client) {
        this.client = client;
    }

    @PostConstruct
    void init() {
        userCollection = client.getDatabase("yicem").getCollection("users", User.class);
    }

    public User findUserByEmail(String email){
        return userCollection.find(eq("email", email)).first();
    }
}
