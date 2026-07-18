package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.DynamicRefreshRunLogMapper;
import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.enumSetting.service.ErrorLogService;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogMapper errorLogMapper;
    private final DynamicRefreshRunLogMapper runLogMapper;
    private final TaskExecutor taskExecutor;
    private final DynamicRefreshTaskRunner taskRunner;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public ErrorLogServiceImpl(ErrorLogMapper errorLogMapper,
                               DynamicRefreshRunLogMapper runLogMapper,
                               @Qualifier("dynamicRefreshExecutor") TaskExecutor taskExecutor,
                               DynamicRefreshTaskRunner taskRunner,
                               PlatformTransactionManager transactionManager) {
        this.errorLogMapper = errorLogMapper;
        this.runLogMapper = runLogMapper;
        this.taskExecutor = taskExecutor;
        this.taskRunner = taskRunner;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    public List<Map<String, Object>> getData(PageData pd) {
        return errorLogMapper.getData(pd);
    }

    @Override
    public String callProc(PageData pd) {
        Object rawId = pd.get("id");
        String taskId = rawId == null ? "" : rawId.toString().trim();
        if (taskId.isEmpty()) {
            throw new IllegalArgumentException("任务编号不能为空");
        }
        String runId = UuidUtil.get32UUID();
        transactionTemplate.execute(status -> {
            if (errorLogMapper.markTaskRunning(taskId) != 1) {
                throw new IllegalStateException("任务正在执行或已被删除");
            }
            Map<String, Object> task = errorLogMapper.getTaskById(taskId);
            if (task == null) {
                throw new IllegalArgumentException("动态刷数任务不存在");
            }
            if (runLogMapper.add(runLog(runId, task, pd)) != 1) {
                throw new IllegalStateException("创建运行记录失败");
            }
            return null;
        });
        try {
            taskExecutor.execute(() -> taskRunner.run(taskId, runId));
        } catch (RuntimeException exception) {
            String message = exceptionMessage(exception);
            errorLogMapper.updateTaskStatus(taskId, "500");
            runLogMapper.complete(runId, "500", message);
            throw exception;
        }
        return runId;
    }

    @Override
    public Integer getCount(PageData pd) {
        return errorLogMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getRunRecords(PageData pd) {
        required(pd, "task_id", "任务编号不能为空");
        return runLogMapper.getData(pd);
    }

    @Override
    public Integer getRunRecordCount(PageData pd) {
        required(pd, "task_id", "任务编号不能为空");
        return runLogMapper.getCount(pd);
    }

    @Override
    public void add(PageData pd) {
        validateTask(pd, false);
        pd.put("id", UuidUtil.get32UUID());
        pd.put("status", "0");
        pd.put("create_time", now());
        pd.put("update_time", now());
        if (errorLogMapper.add(pd) != 1) {
            throw new IllegalStateException("新增动态刷数任务失败");
        }
    }

    @Override
    public void edit(PageData pd) {
        validateTask(pd, true);
        pd.put("update_time", now());
        if (errorLogMapper.edit(pd) != 1) {
            throw new IllegalArgumentException("动态刷数任务不存在或正在执行");
        }
    }

    @Override
    public void del(PageData pd) {
        String id = required(pd, "id", "任务编号不能为空");
        if (errorLogMapper.del(id) != 1) {
            throw new IllegalArgumentException("动态刷数任务不存在或正在执行");
        }
    }

    private void validateTask(PageData pd, boolean requireId) {
        if (requireId) {
            required(pd, "id", "任务编号不能为空");
        }
        required(pd, "task_name", "任务名称不能为空");
        String type = required(pd, "task_type", "任务类型不能为空");
        if (!"1".equals(type) && !"2".equals(type)) {
            throw new IllegalArgumentException("任务类型不合法");
        }
        required(pd, "shell_path", "脚本路径不能为空");
        required(pd, "shell_name", type.equals("1") ? "存储过程名称不能为空" : "脚本名称不能为空");
    }

    private String required(PageData pd, String key, String message) {
        Object rawValue = pd.get(key);
        String value = rawValue == null ? "" : rawValue.toString().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        pd.put(key, value);
        return value;
    }

    private String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private PageData runLog(String runId, Map<String, Object> task, PageData request) {
        PageData runLog = new PageData();
        runLog.put("id", runId);
        runLog.put("task_id", task.get("id"));
        runLog.put("task_name", task.get("task_name"));
        runLog.put("task_type", task.get("task_type"));
        runLog.put("shell_path", task.get("shell_path"));
        runLog.put("shell_name", task.get("shell_name"));
        runLog.put("shell_param", task.get("shell_param"));
        runLog.put("status", "1");
        runLog.put("start_time", now());
        runLog.put("result_message", "任务执行中");
        runLog.put("create_user", request.get("create_user"));
        return runLog;
    }

    private String exceptionMessage(Throwable exception) {
        Throwable root = exception;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        String message = exception.getClass().getName() + ": " + safeMessage(exception);
        if (root != exception) {
            message += "; root cause: " + root.getClass().getName() + ": " + safeMessage(root);
        }
        return truncate(message);
    }

    private String safeMessage(Throwable exception) {
        return exception.getMessage() == null ? "无错误信息" : exception.getMessage();
    }

    private String truncate(String value) {
        int maxLength = 65536;
        return value.length() <= maxLength
                ? value
                : value.substring(0, maxLength) + "\n[错误信息已截断]";
    }
}
