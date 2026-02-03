package com.compiler.service;

import com.compiler.model.Topic;
import com.compiler.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository repository;

    public List<Topic> getAllTopics() {
        return repository.findAll();
    }

    public Topic getTopicById(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    public Topic createTopic(Topic topic) {
        return repository.save(topic);
    }

    public Topic updateTopic(String id, Topic topicDetails) {
        Topic topic = getTopicById(id);
        topic.setName(topicDetails.getName());
        topic.setQuestionIds(topicDetails.getQuestionIds());
        return repository.save(topic);
    }

    public void deleteTopic(String id) {
        repository.deleteById(id);
    }
}
