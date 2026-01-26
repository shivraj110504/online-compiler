package com.compiler.controller;

import com.compiler.model.CodeExecutionRequest;
import com.compiler.model.CodeExecutionResponse;
import com.compiler.service.ExecutionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/execute")
public class CodeExecutionController {
    private final ExecutionService executionService;

    public CodeExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }


    @PostMapping
    public CodeExecutionResponse execute(@RequestBody CodeExecutionRequest request)
            throws Exception {
        return executionService.execute(request);
    }
}
