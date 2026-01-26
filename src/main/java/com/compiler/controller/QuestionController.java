package com.compiler.controller;

import com.compiler.model.Question;
import com.compiler.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@CrossOrigin
public class QuestionController {

    private final QuestionService service;

    @GetMapping("/{slug}")
    public Question getQuestionBySlug(@PathVariable String slug) {
        return service.getBySlug(slug);
    }
}
