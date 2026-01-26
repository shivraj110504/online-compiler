package com.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CodeExecutionResponse {
    private String stdout;
    private String stderr;
    private String status;
    private long executionTimeMs;
}
