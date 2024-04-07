package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

}
