package com.compiler.repository;

import com.compiler.model.UserTopicProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTopicProgressRepository extends MongoRepository<UserTopicProgress, String> {
    List<UserTopicProgress> findByUserId(String userId);

    List<UserTopicProgress> findByUserIdAndTopicId(String userId, String topicId);

    Optional<UserTopicProgress> findByUserIdAndTopicIdAndQuestionId(String userId, String topicId, String questionId);
}
