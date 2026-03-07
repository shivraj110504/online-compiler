package com.compiler.controller;

import com.compiler.model.UserTopicProgress;
import com.compiler.repository.UserTopicProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics/progress")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TopicProgressController {

    private final UserTopicProgressRepository repository;

    @GetMapping("/{userId}")
    public Map<String, List<String>> getTopicProgress(@PathVariable String userId) {
        List<UserTopicProgress> allProgress = repository.findByUserId(userId);
        return allProgress.stream()
                .collect(Collectors.groupingBy(
                        UserTopicProgress::getTopicId,
                        Collectors.mapping(UserTopicProgress::getQuestionId, Collectors.toList())));
    }

    @GetMapping("/{userId}/{topicId}")
    public List<String> getSolvedQuestionsInTopic(@PathVariable String userId, @PathVariable String topicId) {
        return repository.findByUserIdAndTopicId(userId, topicId)
                .stream()
                .map(UserTopicProgress::getQuestionId)
                .collect(Collectors.toList());
    }
}
