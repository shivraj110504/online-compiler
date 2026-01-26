package com.compiler.service;

import com.compiler.dto.CreateQuestionRequest;
import com.compiler.model.Question;
import com.compiler.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repository;

    // READ ALL
    public List<Question> getAll() {
        return repository.findAll();
    }

    // READ BY SLUG
    public Question getBySlug(String slug) {
        return repository.findBySlug(slug).orElse(null);
    }

    // DELETE
    public void delete(String id) {
        repository.deleteById(id);
    }

    // CREATE
    public Question create(CreateQuestionRequest request) {
        Question question = mapToEntity(new Question(), request);
        return repository.save(question);
    }

    // UPDATE
    public Question updateQuestion(String id, CreateQuestionRequest request) {
        Question existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Question updated = mapToEntity(existing, request);
        return repository.save(updated);
    }

    // Common mapper
    private Question mapToEntity(Question question, CreateQuestionRequest request) {

        question.setTitle(request.getTitle());
        question.setSlug(request.getSlug());
        question.setDifficulty(request.getDifficulty());
        question.setTags(request.getTags());
        question.setCompanies(request.getCompanies());
        question.setDescription(request.getDescription());
        question.setInputFormat(request.getInputFormat());
        question.setOutputFormat(request.getOutputFormat());
        question.setConstraints(request.getConstraints());
        question.setStarterCode(request.getStarterCode());
        question.setHiddenCode(request.getHiddenCode());

        if (request.getExamples() != null) {
            question.setExamples(request.getExamples().stream()
                    .map(this::mapTestCase)
                    .collect(Collectors.toList()));
        }

        if (request.getTestCases() != null) {
            question.setTestCases(request.getTestCases().stream()
                    .map(this::mapTestCase)
                    .collect(Collectors.toList()));
        }

        return question;
    }

    private Question.TestCase mapTestCase(CreateQuestionRequest.TestCaseDto dto) {
        Question.TestCase testCase = new Question.TestCase();
        testCase.setInput(dto.getInput());
        testCase.setOutput(dto.getOutput());
        testCase.setVisible(dto.isVisible());
        testCase.setExplanation(dto.getExplanation());
        return testCase;
    }
}
