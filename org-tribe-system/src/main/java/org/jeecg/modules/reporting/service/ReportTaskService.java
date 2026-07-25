package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ReportTaskService {
    private static final List<String> RETRY_TYPES = Arrays.asList("PARSE", "LOAD", "PROCESS");

    private final ReportBatchMapper batchMapper;
    private final ReportTaskMapper taskMapper;
    private final ReportTaskLogMapper logMapper;
    private final ReportWorkflowMapper workflowMapper;
    private final ApplicationEventPublisher publisher;

    public ReportTaskService(ReportBatchMapper batchMapper, ReportTaskMapper taskMapper,
                             ReportTaskLogMapper logMapper, ReportWorkflowMapper workflowMapper,
                             ApplicationEventPublisher publisher) {
        this.batchMapper = batchMapper;
        this.taskMapper = taskMapper;
        this.logMapper = logMapper;
        this.workflowMapper = workflowMapper;
        this.publisher = publisher;
    }

    public static LocalDate monthEnd(String value) {
        try {
            return YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth();
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new IllegalArgumentException("账期必须为 yyyy-MM", exception);
        }
    }

    @Transactional
    public ReportTask queueRetry(String batchId, String requestedTaskType,
                                 String userId, String username) {
        String taskType = requestedTaskType == null ? "" : requestedTaskType.trim().toUpperCase(Locale.ROOT);
        if (!RETRY_TYPES.contains(taskType)) {
            throw new IllegalArgumentException("只允许重新解析、重新入库或再次加工");
        }
        ReportBatch batch = workflowMapper.findBatchForUpdate(batchId);
        if (batch == null || Integer.valueOf(1).equals(batch.getDelFlag())) {
            throw new IllegalArgumentException("上报批次不存在或已逻辑删除");
        }
        if (workflowMapper.countActiveTasks(batchId) > 0) {
            throw new IllegalStateException("该批次已有排队或执任务，请勿并发重试");
        }
        ReportTask previous = workflowMapper.findLatestTask(batchId, taskType);
        if (previous != null && ("QUEUED".equals(previous.getStatus()) || "PROCESSING".equals(previous.getStatus()))) {
            throw new IllegalStateException("该阶段已在排队或执行，请勿重复提交");
        }
        Date now = new Date();
        ReportTask task = new ReportTask();
        task.setId(uuid());
        task.setBatchId(batchId);
        task.setRetryOfTaskId(previous == null ? null : previous.getId());
        task.setTaskType(taskType);
        task.setSequenceNo(previous == null ? sequence(taskType) : previous.getSequenceNo());
        task.setAttemptNo(previous == null || previous.getAttemptNo() == null ? 1 : previous.getAttemptNo() + 1);
        task.setStatus("QUEUED");
        task.setProgressPercent(0);
        task.setExecutorKey("reporting-" + taskType.toLowerCase(Locale.ROOT));
        task.setRequestParams("{\"batchId\":\"" + batchId + "\",\"accountingPeriod\":\""
                + accountingPeriod(batch) + "\"}");
        task.setCreateBy(username);
        task.setCreateTime(now);
        taskMapper.insert(task);

        ReportTaskLog log = new ReportTaskLog();
        log.setId(uuid());
        log.setBatchId(batchId);
        log.setTaskId(task.getId());
        log.setStage(taskType);
        log.setToStatus("QUEUED");
        log.setMessage("已按原批次账期提交重试");
        log.setProcessedRowCount(0L);
        log.setSuccessRowCount(0L);
        log.setErrorRowCount(0L);
        log.setOperatorId(userId);
        log.setOperatorName(username);
        log.setEventTime(now);
        logMapper.insert(log);

        batch.setStatus("PROCESSING");
        batch.setCurrentStage(taskType);
        batch.setUpdateBy(username);
        batch.setUpdateTime(now);
        batchMapper.updateById(batch);
        publishAfterCommit(new ReportBatchExecutionRequested(task.getId(), batchId, taskType, userId, username));
        return task;
    }

    public void publishInitial(String taskId, String batchId, String userId, String username) {
        publishAfterCommit(new ReportBatchExecutionRequested(taskId, batchId, "PARSE", userId, username));
    }

    private void publishAfterCommit(final ReportBatchExecutionRequested event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    publisher.publishEvent(event);
                }
            });
        } else {
            publisher.publishEvent(event);
        }
    }

    private String accountingPeriod(ReportBatch batch) {
        return batch.getAccountingPeriod() == null ? "" : new java.sql.Date(batch.getAccountingPeriod().getTime()).toString();
    }

    private int sequence(String taskType) {
        if ("PARSE".equals(taskType)) return 3;
        if ("LOAD".equals(taskType)) return 4;
        return 5;
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
