package com.compiler.controller;

import com.compiler.model.Topic;
import com.compiler.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/topics")
@RequiredArgsConstructor
@CrossOrigin
public class AdminTopicController {

    private final TopicService service;

    @GetMapping
    public List<Topic> getAllTopics() {
        return service.getAllTopics();
    }

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return service.createTopic(topic);
    }

    @PutMapping("/{id}")
    public Topic updateTopic(@PathVariable String id, @RequestBody Topic topic) {
        return service.updateTopic(id, topic);
    }

    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable String id) {
        service.deleteTopic(id);
    }
}
