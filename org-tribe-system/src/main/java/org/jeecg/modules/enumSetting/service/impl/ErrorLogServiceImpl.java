package org.jeecg.modules.enumSetting.service.impl;

import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.jeecg.modules.enumSetting.service.ErrorLogService;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

    private final ErrorLogMapper errorLogMapper;
    private final TaskExecutor taskExecutor;
    private final DynamicRefreshTaskRunner taskRunner;

    @Autowired
    public ErrorLogServiceImpl(ErrorLogMapper errorLogMapper,
                               @Qualifier("dynamicRefreshExecutor") TaskExecutor taskExecutor,
                               DynamicRefreshTaskRunner taskRunner) {
        this.errorLogMapper = errorLogMapper;
        this.taskExecutor = taskExecutor;
        this.taskRunner = taskRunner;
    }

    @Override
    public List<Map<String, Object>> getData(PageData pd) {
        return errorLogMapper.getData(pd);
    }

    @Override
    public void callProc(PageData pd) {
        Object rawId = pd.get("id");
        String taskId = rawId == null ? "" : rawId.toString().trim();
        if (taskId.isEmpty()) {
            throw new IllegalArgumentException("任务编号不能为空");
        }
        if (errorLogMapper.markTaskRunning(taskId) != 1) {
            throw new IllegalStateException("任务正在执行或已被删除");
        }
        try {
            taskExecutor.execute(() -> taskRunner.run(taskId));
        } catch (RuntimeException exception) {
            errorLogMapper.updateTaskStatus(taskId, "500");
            throw exception;
        }
    }

    @Override
    public Integer getCount(PageData pd) {
        return errorLogMapper.getCount(pd);
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
}
