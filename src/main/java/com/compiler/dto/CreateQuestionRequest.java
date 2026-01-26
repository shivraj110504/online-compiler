package com.compiler.dto;

import com.compiler.model.Difficulty;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CreateQuestionRequest {

    private String title;
    private String slug;
    private Difficulty difficulty;
    private List<String> tags;
    private List<String> companies;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private List<String> constraints;
    private List<TestCaseDto> examples;
    private List<TestCaseDto> testCases;
    private Map<String, String> starterCode;

    @Data
    public static class TestCaseDto {
        private String input;
        private String output;
        private boolean visible;
        private String explanation;
    }
}
