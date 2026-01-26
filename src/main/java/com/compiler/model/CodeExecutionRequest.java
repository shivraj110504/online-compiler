package com.compiler.model;

import lombok.Data;

@Data
public class CodeExecutionRequest {
    private String language;
    private  String code;
    private String input;
}
