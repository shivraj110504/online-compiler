package com.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "submissions")
public class Submission {
    @Id
    private String id;
    private String userId;
    private String userEmail;
    private String questionId;
    private String questionSlug;
    private String language;
    private String status; // e.g., "ACCEPTED", "WA", "TLE", "ERROR"
    private LocalDateTime timestamp;
}
