package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.DynamicRefreshRunLogMapper;
import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.enumSetting.service.ErrorLogService;
import org.jeecg.modules.util.PageData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicRefreshExecutionVastbaseIT {

    @Autowired
    private ErrorLogService errorLogService;

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Autowired
    private DynamicRefreshRunLogMapper runLogMapper;

    @Test
    public void recordsInvalidProcedureFailureThroughRealMapperAndVastbase() throws Exception {
        PageData task = invalidProcedureTask();
        String taskId = null;
        try {
            errorLogService.add(task);
            taskId = String.valueOf(task.get("id"));

            PageData start = new PageData();
            start.put("id", taskId);
            String runId = errorLogService.callProc(start);
            Map<String, Object> run = awaitCompletedRun(runId, 10, TimeUnit.SECONDS);

            assertEquals("500", errorLogMapper.getTaskById(taskId).get("status"));
            assertEquals("500", run.get("status"));
            assertNotNull(run.get("end_time"));
            assertTrue(String.valueOf(run.get("result_message"))
                    .contains("存储过程名称不合法"));
        } finally {
            if (taskId != null) {
                errorLogMapper.updateTaskStatus(taskId, "500");
                runLogMapper.deleteByTaskId(taskId);
                errorLogMapper.del(taskId);
            }
        }
    }

    private Map<String, Object> awaitCompletedRun(
            String runId, long timeout, TimeUnit unit) throws Exception {
        long deadline = System.nanoTime() + unit.toNanos(timeout);
        Map<String, Object> run;
        do {
            run = runLogMapper.getById(runId);
            if (run != null && !"1".equals(String.valueOf(run.get("status")))) {
                return run;
            }
            Thread.sleep(100L);
        } while (System.nanoTime() < deadline);
        throw new AssertionError("运行记录未在超时时间内完成: " + runId);
    }

    private PageData invalidProcedureTask() {
        PageData task = new PageData();
        task.put("task_name", "async-it-" + UUID.randomUUID().toString().replace("-", ""));
        task.put("task_type", "1");
        task.put("shell_path", "/home/app/dwbi/");
        task.put("shell_name", "invalid");
        task.put("shell_param", "202510");
        return task;
    }
}
