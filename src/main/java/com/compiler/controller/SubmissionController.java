package com.compiler.controller;

import com.compiler.model.Submission;
import com.compiler.repository.SubmissionRepository;
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

    @PostMapping
    public Submission recordSubmission(@RequestBody Submission submission) {
        submission.setTimestamp(LocalDateTime.now());
        return submissionRepository.save(submission);
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
