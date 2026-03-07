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
@Document(collection = "user_topic_progress")
public class UserTopicProgress {
    @Id
    private String id;
    private String userId;
    private String topicId;
    private String questionId;
    private LocalDateTime completedAt;
}
