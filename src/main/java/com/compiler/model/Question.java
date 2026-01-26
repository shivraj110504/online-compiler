package com.compiler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "questions")
public class Question {

    @Id
    private String id;

    private String title;
    private String slug;
    private Difficulty difficulty;
    private List<String> tags;
    private List<String> companies;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private List<String> constraints;
    private List<TestCase> examples;
    private Map<String, String> starterCode;

    // Evaluation test cases (can include hidden ones)
    private List<TestCase> testCases;

    @Data
    public static class TestCase {
        private String input;
        private String output;
        private String explanation;
        private boolean visible;
    }
}
