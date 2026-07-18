package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.util.PageData;
import org.junit.Test;
import org.springframework.core.task.SyncTaskExecutor;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ErrorLogServiceImplTest {

    @Test
    public void marksRunningBeforeSubmittingTask() {
        ServiceTestMapperStub mapper = new ServiceTestMapperStub(1);
        RecordingTaskRunner runner = new RecordingTaskRunner();
        ErrorLogServiceImpl service = new ErrorLogServiceImpl(mapper, new SyncTaskExecutor(), runner);
        PageData parameters = new PageData();
        parameters.put("id", "task-1");

        service.callProc(parameters);

        assertEquals("task-1", mapper.markedId);
        assertEquals("task-1", runner.taskId);
    }

    @Test(expected = IllegalStateException.class)
    public void rejectsTaskThatIsAlreadyRunning() {
        ServiceTestMapperStub mapper = new ServiceTestMapperStub(0);
        ErrorLogServiceImpl service = new ErrorLogServiceImpl(
                mapper, new SyncTaskExecutor(), new RecordingTaskRunner()
        );
        PageData parameters = new PageData();
        parameters.put("id", "task-1");

        service.callProc(parameters);
    }

    @Test
    public void normalizesRequiredTaskFieldsBeforeInsert() {
        ServiceTestMapperStub mapper = new ServiceTestMapperStub(1);
        ErrorLogServiceImpl service = new ErrorLogServiceImpl(
                mapper, new SyncTaskExecutor(), new RecordingTaskRunner()
        );
        PageData parameters = new PageData();
        parameters.put("task_name", " task ");
        parameters.put("task_type", " 1 ");
        parameters.put("shell_path", " /home/app/dwbi/ ");
        parameters.put("shell_name", " adm.P_ALL_CONTROL ");

        service.add(parameters);

        assertEquals("task", mapper.added.get("task_name"));
        assertEquals("1", mapper.added.get("task_type"));
        assertEquals("/home/app/dwbi/", mapper.added.get("shell_path"));
        assertEquals("adm.P_ALL_CONTROL", mapper.added.get("shell_name"));
    }

    private static class RecordingTaskRunner extends DynamicRefreshTaskRunner {
        private String taskId;

        RecordingTaskRunner() {
            super(null, null, null);
        }

        @Override
        public void run(String taskId) {
            this.taskId = taskId;
        }
    }

    private static class ServiceTestMapperStub implements ErrorLogMapper {
        private final int markResult;
        private String markedId;
        private PageData added;

        ServiceTestMapperStub(int markResult) {
            this.markResult = markResult;
        }

        @Override
        public int markTaskRunning(String id) {
            markedId = id;
            return markResult;
        }

        @Override
        public List<Map<String, Object>> getData(PageData pd) {
            throw new UnsupportedOperationException("getData is covered by ErrorLogMapperVastbaseIT");
        }

        @Override
        public Integer getCount(PageData pd) {
            throw new UnsupportedOperationException("getCount is covered by ErrorLogMapperVastbaseIT");
        }

        @Override
        public int add(PageData pd) {
            added = pd;
            return 1;
        }

        @Override
        public int edit(PageData pd) {
            throw new UnsupportedOperationException("not used by this service unit test");
        }

        @Override
        public int del(String id) {
            throw new UnsupportedOperationException("not used by this service unit test");
        }

        @Override
        public Map<String, Object> getTaskById(String id) {
            throw new UnsupportedOperationException("not used by this service unit test");
        }

        @Override
        public int updateTaskStatus(String id, String status) {
            throw new UnsupportedOperationException("not used by this service unit test");
        }
    }
}
