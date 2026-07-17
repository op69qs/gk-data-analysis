package org.jeecg.modules.enumSetting.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicRefreshProcessRunner {

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

    public int run(Map<String, Object> task) throws IOException, InterruptedException {
        List<String> command = buildCommand(task);
        Path realRoot = allowedRoot.toRealPath();
        Path executable = Paths.get(command.get(0)).toRealPath();
        if (!executable.startsWith(realRoot) || !Files.isRegularFile(executable) || !Files.isExecutable(executable)) {
            throw new IllegalArgumentException("脚本不存在、不可执行或超出允许目录");
        }
        command.set(0, executable.toString());
        Process process = new ProcessBuilder(command)
                .redirectErrorStream(true)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .start();
        if (!process.waitFor(timeoutSeconds, TimeUnit.SECONDS)) {
            process.destroy();
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
            }
            throw new IOException("动态刷数脚本执行超时");
        }
        return process.exitValue();
    }

    private String required(Map<String, Object> task, String key) {
        Object value = task.get(key);
        if (value == null || value.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(key + "不能为空");
        }
        return value.toString().trim();
    }
}
