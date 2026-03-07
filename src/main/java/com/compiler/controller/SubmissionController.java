package com.compiler.controller;

import com.compiler.model.Submission;
import com.compiler.model.UserTopicProgress;
import com.compiler.repository.SubmissionRepository;
import com.compiler.repository.UserTopicProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubmissionController {

    private final SubmissionRepository submissionRepository;
    private final UserTopicProgressRepository topicProgressRepository;

    @PostMapping
    public Submission recordSubmission(@RequestBody Submission submission) {
        submission.setTimestamp(LocalDateTime.now());
        Submission saved = submissionRepository.save(submission);

        if ("ACCEPTED".equals(submission.getStatus()) && submission.getTopicId() != null) {
            UserTopicProgress progress = topicProgressRepository
                    .findByUserIdAndTopicIdAndQuestionId(submission.getUserId(), submission.getTopicId(),
                            submission.getQuestionId())
                    .orElse(UserTopicProgress.builder()
                            .userId(submission.getUserId())
                            .topicId(submission.getTopicId())
                            .questionId(submission.getQuestionId())
                            .build());
            progress.setCompletedAt(LocalDateTime.now());
            topicProgressRepository.save(progress);
        }

        return saved;
    }

    @GetMapping("/user/{userId}")
    public List<String> getSolvedQuestions(@PathVariable String userId) {
        return submissionRepository.findByUserIdAndStatus(userId, "ACCEPTED")
                .stream()
                .map(Submission::getQuestionId)
                .distinct()
                .collect(Collectors.toList());
    }

    @GetMapping("/stats/{userId}")
    public Map<String, Long> getSubmissionStats(@PathVariable String userId) {
        return submissionRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.groupingBy(
                        s -> s.getTimestamp().toLocalDate().toString(),
                        Collectors.counting()));
    }
}
