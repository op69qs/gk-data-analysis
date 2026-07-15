package org.jeecg.modules.enumSetting.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DynamicRefreshTaskRunner {

    private final ErrorLogMapper errorLogMapper;
    private final DynamicRefreshProcessRunner processRunner;
    private final DynamicRefreshStoredProcedureRunner storedProcedureRunner;

    public DynamicRefreshTaskRunner(ErrorLogMapper errorLogMapper,
                                    DynamicRefreshProcessRunner processRunner,
                                    DynamicRefreshStoredProcedureRunner storedProcedureRunner) {
        this.errorLogMapper = errorLogMapper;
        this.processRunner = processRunner;
        this.storedProcedureRunner = storedProcedureRunner;
    }

    public void run(String taskId) {
        String finalStatus = "500";
        try {
            Map<String, Object> task = errorLogMapper.getTaskById(taskId);
            if (task == null) {
                throw new IllegalArgumentException("动态刷数任务不存在");
            }
            String taskType = String.valueOf(task.get("task_type"));
            if ("1".equals(taskType)) {
                storedProcedureRunner.run(task);
                finalStatus = "200";
            } else if ("2".equals(taskType)) {
                finalStatus = processRunner.run(task) == 0 ? "200" : "500";
            } else {
                throw new IllegalArgumentException("不支持的任务类型: " + taskType);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            log.error("动态刷数任务被中断, id={}", taskId, exception);
        } catch (Exception exception) {
            log.error("动态刷数任务执行失败, id={}", taskId, exception);
        } finally {
            errorLogMapper.updateTaskStatus(taskId, finalStatus);
        }
    }
}
