package com.compiler.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "topics")
public class Topic {
    @Id
    private String id;
    private String name;
    private List<String> questionIds;
}
