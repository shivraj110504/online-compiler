package com.compiler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Difficulty {
    @JsonProperty("Easy")
    EASY,
    @JsonProperty("Medium")
    MEDIUM,
    @JsonProperty("Hard")
    HARD
}
