package org.jeecg.modules.enumSetting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicRefreshProcessRunner {

    private static final int MAX_OUTPUT_BYTES = 65536;

    private final Path allowedRoot;
    private final long timeoutSeconds;

    public DynamicRefreshProcessRunner() {
        this("/home/app/dwbi", 3600L);
    }

    @Autowired
    public DynamicRefreshProcessRunner(
            @Value("${dynamic-refresh.allowed-shell-root:/home/app/dwbi}") String allowedRoot,
            @Value("${dynamic-refresh.shell-timeout-seconds:3600}") long timeoutSeconds) {
        this.allowedRoot = Paths.get(allowedRoot).toAbsolutePath().normalize();
        if (timeoutSeconds <= 0) {
            throw new IllegalArgumentException("脚本超时时间必须大于0秒");
        }
        this.timeoutSeconds = timeoutSeconds;
    }

    List<String> buildCommand(Map<String, Object> task) {
        String shellPath = required(task, "shell_path");
        String shellName = required(task, "shell_name");
        Path configuredDirectory = Paths.get(shellPath);
        if (!configuredDirectory.isAbsolute()) {
            throw new IllegalArgumentException("脚本路径必须是绝对路径");
        }
        Path baseDirectory = configuredDirectory.normalize();
        if (!baseDirectory.startsWith(allowedRoot)) {
            throw new IllegalArgumentException("脚本路径不在允许目录内");
        }
        Path name = Paths.get(shellName);
        if (name.isAbsolute() || name.getNameCount() != 1 || ".".equals(shellName) || "..".equals(shellName)) {
            throw new IllegalArgumentException("脚本名称只能是当前目录下的文件名");
        }

        Path executable = baseDirectory.resolve(name).normalize();
        if (!baseDirectory.equals(executable.getParent())) {
            throw new IllegalArgumentException("脚本路径超出配置目录");
        }

        List<String> command = new ArrayList<>();
        command.add(executable.toString());
        Object rawParameters = task.get("shell_param");
        if (rawParameters != null && !rawParameters.toString().trim().isEmpty()) {
            for (String parameter : rawParameters.toString().split("@", -1)) {
                command.add(parameter);
            }
        }
        return command;
    }

    public ProcessResult run(Map<String, Object> task) throws IOException, InterruptedException {
        List<String> command = buildCommand(task);
        Path realRoot = allowedRoot.toRealPath();
        Path executable = Paths.get(command.get(0)).toRealPath();
        if (!executable.startsWith(realRoot) || !Files.isRegularFile(executable) || !Files.isExecutable(executable)) {
            throw new IllegalArgumentException("脚本不存在、不可执行或超出允许目录");
        }
        command.set(0, executable.toString());
        Path outputFile = Files.createTempFile("dynamic-refresh-", ".log");
        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
                process.destroy();
                if (!process.waitFor(5, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
                throw new IOException("动态刷数脚本执行超时");
            }
            return new ProcessResult(process.exitValue(), readBounded(outputFile));
        } finally {
            Files.deleteIfExists(outputFile);
        }
    }

    private String readBounded(Path outputFile) throws IOException {
        try (InputStream input = Files.newInputStream(outputFile);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int remaining = MAX_OUTPUT_BYTES;
            int read;
            while (remaining > 0
                    && (read = input.read(buffer, 0, Math.min(buffer.length, remaining))) != -1) {
                output.write(buffer, 0, read);
                remaining -= read;
            }
            String value = new String(output.toByteArray(), StandardCharsets.UTF_8);
            if (input.read() != -1) {
                return value + "\n[输出已截断]";
            }
            return value;
        }
    }

    private String required(Map<String, Object> task, String key) {
        Object value = task.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(key + "不能为空");
        }
        return value.toString().trim();
    }

    static final class ProcessResult {
        private final int exitCode;
        private final String output;

        ProcessResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }

        int getExitCode() {
            return exitCode;
        }

        String getOutput() {
            return output;
        }
    }
}
