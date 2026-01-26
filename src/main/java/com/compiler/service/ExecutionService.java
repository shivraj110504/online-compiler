package com.compiler.service;

import com.compiler.config.LanguageConfig;
import com.compiler.model.CodeExecutionRequest;
import com.compiler.model.CodeExecutionResponse;
import com.compiler.util.FileUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutionService {

    public CodeExecutionResponse execute(CodeExecutionRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            LanguageConfig lang = LanguageConfig.valueOf(request.getLanguage().toUpperCase());

            Path workDir = FileUtil.createTempDir();
            Path codeFile = workDir.resolve(lang.getFileName());
            Path inputFile = workDir.resolve("input.txt");

            FileUtil.writeFile(codeFile, request.getCode());
            FileUtil.writeFile(inputFile, request.getInput() != null ? request.getInput() : "");

            // Compile + run command inside container
            String containerCmd;
            if (lang.getCompileCommand() != null) {
                containerCmd = String.format("%s && %s < input.txt", lang.getCompileCommand(), lang.getRunCommand());
            } else {
                containerCmd = String.format("%s < input.txt", lang.getRunCommand());
            }

            // Escape quotes for bash
            containerCmd = containerCmd.replace("\"", "\\\"");

            // Determine if we should use Docker or run directly (for Render)
            String runtimeEnv = System.getenv("RUNTIME_ENVIRONMENT");
            boolean useDocker = (runtimeEnv == null || !runtimeEnv.equalsIgnoreCase("CONTAINER"));

            String[] finalCmd;
            if (useDocker) {
                // Full docker command
                finalCmd = new String[] {
                        "docker", "run", "--rm",
                        "--network=none",
                        "--memory=256m", "--cpus=0.5",
                        "--pids-limit", "64",
                        "--cap-drop", "ALL",
                        "--read-only",
                        "-v", workDir.toAbsolutePath().toString().replace("\\", "/") + ":/app",
                        "-w", "/app",
                        lang.getImage(),
                        "/bin/bash", "-c", containerCmd
                };
            } else {
                // Direct execution inside the same container (for Render/Deployment)
                finalCmd = new String[] { "/bin/bash", "-c", containerCmd };
            }

            ProcessBuilder pb = new ProcessBuilder(finalCmd);
            pb.directory(workDir.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            boolean finished = process.waitFor(10, TimeUnit.SECONDS); // Increased timeout for warm-up
            if (!finished) {
                process.destroyForcibly();
                return new CodeExecutionResponse("", "Time Limit Exceeded", "TLE", 10000);
            }

            String output = new String(process.getInputStream().readAllBytes());
            long time = System.currentTimeMillis() - startTime;

            return new CodeExecutionResponse(output, "", "SUCCESS", time);

        } catch (Exception e) {
            // Return the error for debugging
            long time = System.currentTimeMillis() - startTime;
            return new CodeExecutionResponse("", e.getMessage(), "ERROR", time);
        }
    }
}
