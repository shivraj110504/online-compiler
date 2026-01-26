package com.compiler.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LanguageConfig {

    JAVA("java-runner", "Main.java", "javac Main.java", "java Main"),
    CPP("cpp-runner", "main.cpp", "g++ main.cpp -o main", "./main"), // ./main works inside container
    PYTHON("python-runner", "main.py", null, "python3 main.py"),
    NODE("node-runner", "main.js", null, "node main.js"),
    JS("node-runner", "main.js", null, "node main.js");

    private final String image;            // Docker image name
    private final String fileName;         // Code file name
    private final String compileCommand;   // Compilation command (null for interpreted)
    private final String runCommand;       // Execution command
}
