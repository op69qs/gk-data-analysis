package org.jeecg.modules.enumSetting.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.enumSetting.mapper.DynamicRefreshRunLogMapper;
import org.jeecg.modules.enumSetting.mapper.ErrorLogMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DynamicRefreshTaskRunner {

    private final ErrorLogMapper errorLogMapper;
    private final DynamicRefreshRunLogMapper runLogMapper;
    private final DynamicRefreshProcessRunner processRunner;
    private final DynamicRefreshStoredProcedureRunner storedProcedureRunner;

    public DynamicRefreshTaskRunner(ErrorLogMapper errorLogMapper,
                                    DynamicRefreshRunLogMapper runLogMapper,
                                    DynamicRefreshProcessRunner processRunner,
                                    DynamicRefreshStoredProcedureRunner storedProcedureRunner) {
        this.errorLogMapper = errorLogMapper;
        this.runLogMapper = runLogMapper;
        this.processRunner = processRunner;
        this.storedProcedureRunner = storedProcedureRunner;
    }

    public void run(String taskId, String runId) {
        String finalStatus = "500";
        String resultMessage = "任务执行失败";
        try {
            Map<String, Object> task = errorLogMapper.getTaskById(taskId);
            if (task == null) {
                throw new IllegalArgumentException("动态刷数任务不存在");
            }
            String taskType = String.valueOf(task.get("task_type"));
            if ("1".equals(taskType)) {
                storedProcedureRunner.run(task);
                Map<String, Object> currentTask = errorLogMapper.getTaskById(taskId);
                String procedureStatus = currentTask == null
                        ? "500"
                        : String.valueOf(currentTask.get("status"));
                if ("500".equals(procedureStatus)) {
                    resultMessage = "存储过程返回失败状态（500）";
                } else {
                    finalStatus = "200";
                    resultMessage = "存储过程执行成功";
                }
            } else if ("2".equals(taskType)) {
                DynamicRefreshProcessRunner.ProcessResult result = processRunner.run(task);
                if (result.getExitCode() == 0) {
                    finalStatus = "200";
                    resultMessage = "脚本执行成功，退出码：0";
                } else {
                    resultMessage = "脚本执行失败，退出码：" + result.getExitCode();
                    if (!result.getOutput().trim().isEmpty()) {
                        resultMessage += "\n" + result.getOutput().trim();
                    }
                }
            } else {
                throw new IllegalArgumentException("不支持的任务类型: " + taskType);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            resultMessage = exceptionMessage(exception);
            log.error("动态刷数任务被中断, id={}", taskId, exception);
        } catch (Exception exception) {
            resultMessage = exceptionMessage(exception);
            log.error("动态刷数任务执行失败, id={}", taskId, exception);
        } finally {
            try {
                errorLogMapper.updateTaskStatus(taskId, finalStatus);
            } catch (Exception exception) {
                log.error("更新动态刷数任务状态失败, id={}", taskId, exception);
            }
            try {
                runLogMapper.complete(runId, finalStatus, truncate(resultMessage));
            } catch (Exception exception) {
                log.error("更新动态刷数运行记录失败, taskId={}, runId={}",
                        taskId, runId, exception);
            }
        }
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
