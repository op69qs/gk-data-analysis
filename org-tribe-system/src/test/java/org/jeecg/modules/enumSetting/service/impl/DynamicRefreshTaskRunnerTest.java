package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.util.PageData;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DynamicRefreshTaskRunnerTest {

    @Test
    public void marksStoredProcedureTaskSuccessful() {
        FakeMapper mapper = new FakeMapper(task("1"));
        RecordingProcedureRunner procedureRunner = new RecordingProcedureRunner();
        DynamicRefreshTaskRunner runner = new DynamicRefreshTaskRunner(
                mapper, new FixedProcessRunner(0), procedureRunner
        );

        runner.run("task-1");

        assertEquals(1, procedureRunner.calls);
        assertEquals("200", mapper.statuses.get(0));
    }

    @Test
    public void marksShellTaskFailedWhenProcessReturnsNonZero() {
        FakeMapper mapper = new FakeMapper(task("2"));
        DynamicRefreshTaskRunner runner = new DynamicRefreshTaskRunner(
                mapper, new FixedProcessRunner(9), new RecordingProcedureRunner()
        );

        runner.run("task-1");

        assertEquals("500", mapper.statuses.get(0));
    }

    private static Map<String, Object> task(String type) {
        Map<String, Object> task = new HashMap<>();
        task.put("id", "task-1");
        task.put("task_type", type);
        return task;
    }

    private static class FixedProcessRunner extends DynamicRefreshProcessRunner {
        private final int exitCode;

        FixedProcessRunner(int exitCode) {
            this.exitCode = exitCode;
        }

        @Override
        public int run(Map<String, Object> task) throws IOException, InterruptedException {
            return exitCode;
        }
    }

    private static class RecordingProcedureRunner extends DynamicRefreshStoredProcedureRunner {
        int calls;

        RecordingProcedureRunner() {
            super(null);
        }

        @Override
        public void run(Map<String, Object> task) throws SQLException {
            calls++;
        }
    }

    private static class FakeMapper implements ErrorLogMapper {
        private final Map<String, Object> task;
        private final List<String> statuses = new ArrayList<>();

        FakeMapper(Map<String, Object> task) {
            this.task = task;
        }

        @Override
        public Map<String, Object> getTaskById(String id) {
            return task;
        }

        @Override
        public int updateTaskStatus(String id, String status) {
            statuses.add(status);
            return 1;
        }

        @Override
        public int markTaskRunning(String id) {
            return 1;
        }

        @Override
        public List<Map<String, Object>> getData(PageData pd) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer getCount(PageData pd) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int add(PageData pd) { return 0; }

        @Override
        public int edit(PageData pd) { return 0; }

        @Override
        public int del(String id) { return 0; }
    }
}
