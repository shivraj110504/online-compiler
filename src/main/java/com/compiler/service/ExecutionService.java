package com.compiler.service;

import com.compiler.config.LanguageConfig;
import com.compiler.model.CodeExecutionRequest;
import com.compiler.model.CodeExecutionResponse;
import com.compiler.model.BatchCodeExecutionRequest;
import com.compiler.model.BatchCodeExecutionResponse;
import com.compiler.model.Question;
import com.compiler.repository.QuestionRepository;
import com.compiler.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final QuestionRepository questionRepository;

    public BatchCodeExecutionResponse executeBatch(BatchCodeExecutionRequest request) {
        long startTime = System.currentTimeMillis();
        String finalCode = request.getCode();

        // Handle Code Wrapping (once per batch)
        if (request.getQuestionId() != null && !request.getQuestionId().isEmpty()) {
            Question question = questionRepository.findById(request.getQuestionId()).orElse(null);
            if (question != null && question.getHiddenCode() != null) {
                String language = request.getLanguage().toLowerCase();
                String wrapper = question.getHiddenCode().get(language);
                if (wrapper != null && wrapper.contains("{{USER_CODE}}")) {
                    finalCode = wrapper.replace("{{USER_CODE}}", request.getCode());
                }
            }
        }

        java.util.List<CodeExecutionResponse> results = new java.util.ArrayList<>();
        try {
            LanguageConfig lang = LanguageConfig.valueOf(request.getLanguage().toUpperCase());
            Path workDir = FileUtil.createTempDir();
            Path codeFile = workDir.resolve(lang.getFileName());
            FileUtil.writeFile(codeFile, finalCode);

            // 1. Single Compilation (if applicable)
            if (lang.getCompileCommand() != null) {
                String compileCmd = lang.getCompileCommand();
                String runtimeEnv = System.getenv("RUNTIME_ENVIRONMENT");
                boolean useDocker = (runtimeEnv == null || !runtimeEnv.equalsIgnoreCase("CONTAINER"));

                String[] finalCompileCmd;
                if (useDocker) {
                    finalCompileCmd = new String[] {
                            "docker", "run", "--rm", "-v",
                            workDir.toAbsolutePath().toString().replace("\\", "/") + ":/app",
                            "-w", "/app", lang.getImage(), "/bin/bash", "-c", compileCmd
                    };
                } else {
                    finalCompileCmd = new String[] { "/bin/bash", "-c", compileCmd };
                }

                Process compileProcess = new ProcessBuilder(finalCompileCmd).directory(workDir.toFile()).start();
                if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                    compileProcess.destroyForcibly();
                    throw new RuntimeException("Compilation Timeout");
                }
                if (compileProcess.exitValue() != 0) {
                    String compileError = new String(compileProcess.getErrorStream().readAllBytes());
                    results.add(new CodeExecutionResponse("", compileError, "ERROR", 0));
                    return new BatchCodeExecutionResponse(results, System.currentTimeMillis() - startTime);
                }
            }

            // 2. Run all inputs using the compiled binary/script
            for (String input : request.getInputs()) {
                long inputStartTime = System.currentTimeMillis();
                Path inputFile = workDir.resolve("input.txt");
                FileUtil.writeFile(inputFile, input != null ? input : "");

                String runCmd = String.format("%s < input.txt", lang.getRunCommand());
                runCmd = runCmd.replace("\"", "\\\"");

                String runtimeEnv = System.getenv("RUNTIME_ENVIRONMENT");
                boolean useDocker = (runtimeEnv == null || !runtimeEnv.equalsIgnoreCase("CONTAINER"));

                String[] finalRunCmd;
                if (useDocker) {
                    finalRunCmd = new String[] {
                            "docker", "run", "--rm", "--network=none", "--memory=256m", "--cpus=0.5",
                            "-v", workDir.toAbsolutePath().toString().replace("\\", "/") + ":/app",
                            "-w", "/app", lang.getImage(), "/bin/bash", "-c", runCmd
                    };
                } else {
                    finalRunCmd = new String[] { "/bin/bash", "-c", runCmd };
                }

                Process runProcess = new ProcessBuilder(finalRunCmd).directory(workDir.toFile())
                        .redirectErrorStream(true).start();
                if (!runProcess.waitFor(5, TimeUnit.SECONDS)) {
                    runProcess.destroyForcibly();
                    results.add(new CodeExecutionResponse("", "Time Limit Exceeded", "TLE", 5000));
                    continue;
                }

                String output = new String(runProcess.getInputStream().readAllBytes());
                results.add(
                        new CodeExecutionResponse(output, "", "SUCCESS", System.currentTimeMillis() - inputStartTime));
            }
        } catch (Exception e) {
            results.add(new CodeExecutionResponse("", e.getMessage(), "ERROR", 0));
        }

        return new BatchCodeExecutionResponse(results, System.currentTimeMillis() - startTime);
    }

    public CodeExecutionResponse execute(CodeExecutionRequest request) {
        // Fallback or wrapper for single execution using the batch logic
        BatchCodeExecutionRequest batchRequest = new BatchCodeExecutionRequest();
        batchRequest.setLanguage(request.getLanguage());
        batchRequest.setCode(request.getCode());
        batchRequest.setInputs(java.util.Collections.singletonList(request.getInput()));
        batchRequest.setQuestionId(request.getQuestionId());

        BatchCodeExecutionResponse batchResponse = executeBatch(batchRequest);
        if (batchResponse.getResults().isEmpty()) {
            return new CodeExecutionResponse("", "Internal Error", "ERROR", 0);
        }
        return batchResponse.getResults().get(0);
    }
}
