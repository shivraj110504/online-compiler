package com.compiler.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static Path createTempDir() throws IOException {
        return Files.createTempDirectory("exec-");
    }


    public static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content == null ? "" : content);
    }
}
