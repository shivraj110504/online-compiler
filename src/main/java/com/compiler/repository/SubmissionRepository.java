package com.compiler.repository;

import com.compiler.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findByUserId(String userId);

    List<Submission> findByUserIdAndStatus(String userId, String status);
}
