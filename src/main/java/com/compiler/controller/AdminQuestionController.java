package com.compiler.controller;

import com.compiler.dto.CreateQuestionRequest;
import com.compiler.model.Question;
import com.compiler.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@CrossOrigin
public class AdminQuestionController {

    private final QuestionService service;

    @GetMapping
    public List<Question> getAllQuestions() {
        return service.getAll();
    }

    @PostMapping
    public Question createQuestion(@RequestBody CreateQuestionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable String id,
            @RequestBody CreateQuestionRequest request) {
        return service.updateQuestion(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable String id) {
        service.delete(id);
    }
}
