package org.jeecg.modules.enumSetting.mapper;

import org.jeecg.modules.util.PageData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ErrorLogMapperVastbaseIT {

    @Autowired
    private ErrorLogMapper errorLogMapper;

    @Autowired
    private DynamicRefreshRunLogMapper runLogMapper;

    @Test
    public void queriesExecShellTaskThroughRealMyBatisMapper() {
        PageData parameters = new PageData();
        parameters.put("page", 0);
        parameters.put("rows", 10);

        Integer count = errorLogMapper.getCount(parameters);
        List<Map<String, Object>> rows = errorLogMapper.getData(parameters);

        assertNotNull(count);
        assertNotNull(rows);
        assertTrue(rows.size() <= 10);
        assertEquals(Math.min(count, 10), rows.size());
    }

    @Test
    @Transactional
    public void executesCrudAndStatusUpdatesThroughRealMyBatisMapper() {
        String id = UUID.randomUUID().toString().replace("-", "");
        String taskName = "mapper-it-" + id;
        PageData task = task(id, taskName);

        assertEquals(1, errorLogMapper.add(task));
        assertEquals(taskName, errorLogMapper.getTaskById(id).get("task_name"));

        PageData query = new PageData();
        query.put("task_name", taskName);
        query.put("page", 0);
        query.put("rows", 10);
        assertEquals(Integer.valueOf(1), errorLogMapper.getCount(query));
        assertEquals(id, errorLogMapper.getData(query).get(0).get("id"));

        task.put("task_name", taskName + "-updated");
        task.put("update_time", now());
        assertEquals(1, errorLogMapper.edit(task));
        assertEquals(taskName + "-updated", errorLogMapper.getTaskById(id).get("task_name"));

        assertEquals(1, errorLogMapper.markTaskRunning(id));
        assertEquals("1", errorLogMapper.getTaskById(id).get("status"));
        assertEquals(1, errorLogMapper.updateTaskStatus(id, "200"));
        assertEquals("200", errorLogMapper.getTaskById(id).get("status"));

        assertEquals(1, errorLogMapper.del(id));
        assertEquals(null, errorLogMapper.getTaskById(id));
    }

    @Test
    @Transactional
    public void persistsAndQueriesRunHistoryThroughRealVastbase() {
        String taskId = uuid();
        String oldRunId = uuid();
        String newRunId = uuid();
        assertEquals(1, errorLogMapper.add(task(taskId, "mapper-history-" + taskId)));
        assertEquals(1, runLogMapper.add(runLog(oldRunId, taskId, "2026-07-17 10:00:00")));
        assertEquals(1, runLogMapper.add(runLog(newRunId, taskId, "2026-07-17 11:00:00")));

        PageData query = new PageData();
        query.put("task_id", taskId);
        query.put("page", 0);
        query.put("rows", 10);
        assertEquals(Integer.valueOf(2), runLogMapper.getCount(query));
        List<Map<String, Object>> rows = runLogMapper.getData(query);
        assertEquals(newRunId, rows.get(0).get("id"));
        assertTrue(rows.get(0).containsKey("result_message"));

        assertEquals(1, runLogMapper.complete(newRunId, "500", "mapper integration failure"));
        assertEquals("500", runLogMapper.getById(newRunId).get("status"));
    }

    private PageData task(String id, String taskName) {
        PageData task = new PageData();
        task.put("id", id);
        task.put("task_name", taskName);
        task.put("shell_path", "/home/app/dwbi/");
        task.put("shell_name", "adm.p_all_control");
        task.put("shell_param", "202510");
        task.put("status", "0");
        task.put("task_type", "1");
        task.put("create_time", now());
        task.put("update_time", now());
        return task;
    }

    private PageData runLog(String id, String taskId, String startTime) {
        PageData runLog = new PageData();
        runLog.put("id", id);
        runLog.put("task_id", taskId);
        runLog.put("task_name", "mapper-history-" + taskId);
        runLog.put("task_type", "1");
        runLog.put("shell_path", "/home/app/dwbi/");
        runLog.put("shell_name", "adm.p_all_control");
        runLog.put("shell_param", "202510");
        runLog.put("status", "1");
        runLog.put("start_time", startTime);
        runLog.put("result_message", "任务执行中");
        return runLog;
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
