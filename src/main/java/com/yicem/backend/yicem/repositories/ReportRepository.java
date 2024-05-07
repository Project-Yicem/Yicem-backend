package com.yicem.backend.yicem.repositories;

import com.yicem.backend.yicem.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByReportedBusinessId(String reportedBusinessId);
}
