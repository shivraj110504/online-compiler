package com.compiler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchCodeExecutionResponse {
    private List<CodeExecutionResponse> results;
    private long totalTimeMs;
}
