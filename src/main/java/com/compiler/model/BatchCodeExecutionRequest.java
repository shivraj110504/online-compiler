package com.compiler.model;

import lombok.Data;
import java.util.List;

@Data
public class BatchCodeExecutionRequest {
    private String language;
    private String code;
    private List<String> inputs;
    private String questionId;
}
