package com.compiler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to Prep4Place Online Compiler API. Use /api/admin/questions to manage questions.";
    }
}
