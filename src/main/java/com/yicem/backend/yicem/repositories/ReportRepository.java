package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {

}
