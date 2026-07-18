package org.jeecg.modules.enumSetting.service.impl;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DynamicRefreshProcessRunnerTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private final DynamicRefreshProcessRunner runner = new DynamicRefreshProcessRunner();

    @Test
    public void buildsCommandFromPathNameAndAtSeparatedArguments() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_path", "/home/app/dwbi/");
        task.put("shell_name", "run-task.sh");
        task.put("shell_param", "202510@CQ@full load");

        List<String> command = runner.buildCommand(task);

        assertEquals(
                Arrays.asList("/home/app/dwbi/run-task.sh", "202510", "CQ", "full load"),
                command
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsShellNameThatEscapesConfiguredDirectory() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_path", "/home/app/dwbi/");
        task.put("shell_name", "../other/run.sh");

        runner.buildCommand(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsRelativeShellDirectory() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_path", "scripts/");
        task.put("shell_name", "run.sh");

        runner.buildCommand(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsShellDirectoryOutsideAllowedRoot() {
        Map<String, Object> task = new HashMap<>();
        task.put("shell_path", "/bin/");
        task.put("shell_name", "sh");

        runner.buildCommand(task);
    }

    @Test
    public void capturesOutputAndNonZeroExitCode() throws Exception {
        Path root = temporaryFolder.newFolder("dynamic-refresh").toPath();
        Path script = root.resolve("fail.sh");
        Files.write(
                script,
                Arrays.asList("#!/bin/sh", "echo expected failure >&2", "exit 9"),
                StandardCharsets.UTF_8
        );
        Files.setPosixFilePermissions(script, PosixFilePermissions.fromString("rwx------"));

        Map<String, Object> task = new HashMap<>();
        task.put("shell_path", root.toString());
        task.put("shell_name", script.getFileName().toString());
        DynamicRefreshProcessRunner processRunner =
                new DynamicRefreshProcessRunner(root.toString(), 10L);

        DynamicRefreshProcessRunner.ProcessResult result = processRunner.run(task);

        assertEquals(9, result.getExitCode());
        assertTrue(result.getOutput().contains("expected failure"));
    }
}
