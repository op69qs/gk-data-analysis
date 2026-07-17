package org.jeecg.modules.enumSetting.service.impl;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DynamicRefreshProcessRunnerTest {

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
}
