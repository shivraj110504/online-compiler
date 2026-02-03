package com.compiler.controller;

import com.compiler.model.Topic;
import com.compiler.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
@CrossOrigin
public class TopicController {

    private final TopicService service;

    @GetMapping
    public List<Topic> getAllTopics() {
        return service.getAllTopics();
    }

    @GetMapping("/{id}")
    public Topic getTopicById(@PathVariable String id) {
        return service.getTopicById(id);
    }
}
