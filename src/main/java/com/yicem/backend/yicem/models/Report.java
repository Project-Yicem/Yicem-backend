package com.yicem.backend.yicem.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reports")
@Getter
@Setter
public class Report {
    @Id
    private String id;

    private String reportedBusinessId;
    private String reportDescription;

    public Report(String reportedBusinessId, String reportDescription) {
        this.reportedBusinessId = reportedBusinessId;
        this.reportDescription = reportDescription;
    }

}
