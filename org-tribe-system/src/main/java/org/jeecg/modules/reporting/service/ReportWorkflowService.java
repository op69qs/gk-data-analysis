package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportParseError;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportParseErrorMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.jeecg.modules.reporting.parser.KeyFileParseError;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsExcelParseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/** Executes one tracked reporting stage. The listener invokes this outside the upload transaction. */
@Service
public class ReportWorkflowService {
    private final ReportBatchMapper batchMapper;
    private final ReportFileMapper fileMapper;
    private final ReportTaskMapper taskMapper;
    private final ReportTaskLogMapper logMapper;
    private final ReportParseErrorMapper errorMapper;
    private final ReportWorkflowMapper workflowMapper;
    private final KeyReportProcessingService keyService;
    private final TimsReportProcessingService timsService;
    private final ReportProcessCallService processService;
    private final ReportingProperties properties;
    private final TransactionTemplate transactionTemplate;
    private LegacyPendingService legacyPendingService;

    public ReportWorkflowService(ReportBatchMapper batchMapper, ReportFileMapper fileMapper,
                                 ReportTaskMapper taskMapper, ReportTaskLogMapper logMapper,
                                 ReportParseErrorMapper errorMapper, ReportWorkflowMapper workflowMapper,
                                 KeyReportProcessingService keyService,
                                 TimsReportProcessingService timsService,
                                 ReportProcessCallService processService,
                                 ReportingProperties properties) {
        this(batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, properties, null);
    }

    @Autowired
    public ReportWorkflowService(ReportBatchMapper batchMapper, ReportFileMapper fileMapper,
                                 ReportTaskMapper taskMapper, ReportTaskLogMapper logMapper,
                                 ReportParseErrorMapper errorMapper, ReportWorkflowMapper workflowMapper,
                                 KeyReportProcessingService keyService,
                                 TimsReportProcessingService timsService,
                                 ReportProcessCallService processService,
                                 ReportingProperties properties,
                                 PlatformTransactionManager transactionManager) {
        this.batchMapper = batchMapper;
        this.fileMapper = fileMapper;
        this.taskMapper = taskMapper;
        this.logMapper = logMapper;
        this.errorMapper = errorMapper;
        this.workflowMapper = workflowMapper;
        this.keyService = keyService;
        this.timsService = timsService;
        this.processService = processService;
        this.properties = properties;
        this.transactionTemplate = transactionManager == null ? null : new TransactionTemplate(transactionManager);
    }

    @Autowired(required = false)
    public void setLegacyPendingService(LegacyPendingService legacyPendingService) {
        this.legacyPendingService = legacyPendingService;
    }

    public void execute(ReportBatchExecutionRequested event) {
        ReportBatch batch = batchMapper.selectById(event.getBatchId());
        if (batch == null || Integer.valueOf(1).equals(batch.getDelFlag())) {
            return;
        }
        String requested = normalizeStage(event.getRequestedTaskType());
        ReportTask task = taskMapper.selectById(event.getTaskId());
        String leaseToken = properties.getTaskInstanceId() + ":" + uuid();
        if (task == null || !batch.getId().equals(task.getBatchId())
                || !requested.equals(task.getTaskType())
                || workflowMapper.claimTask(task.getId(), new Date(), event.getUsername(),
                leaseToken, leaseUntil()) != 1) {
            return;
        }
        task.setStatus("PROCESSING");
        task.setLeaseOwner(leaseToken);
        if ("PROCESS".equals(requested)) {
            executeFenced(batch, task, event, leaseToken, true);
            return;
        }
        executeFenced(batch, task, event, leaseToken, false);
    }

    private void executeFenced(ReportBatch batch, ReportTask task,
                               ReportBatchExecutionRequested event, String leaseToken,
                               boolean process) {
        try {
            if (process) {
                inTransaction(() -> {
                    requireOwnedLease(task.getId(), leaseToken);
                    executeProcess(batch, task, event, leaseToken);
                });
            } else {
                requireOwnedLease(task.getId(), leaseToken);
                executeParseAndLoad(batch, task, event, leaseToken);
            }
        } catch (TaskLeaseLostException ignored) {
            // A recovery worker owns this task now. The old execution must not write terminal state.
        } catch (Exception exception) {
            recordExecutionFailure(batch, task, event, leaseToken, exception);
        }
    }

    private void executeParseAndLoad(ReportBatch batch, ReportTask requestedTask,
                                     ReportBatchExecutionRequested event,
                                     String leaseToken) throws Exception {
        Date started = new Date();
        startTask(batch, requestedTask, event, started);
        List<ReportFile> files = workflowMapper.findBatchFiles(batch.getId());
        Path extractRoot = extractRoot(files);
        ProcessingSummary summary;
        if ("KEY".equalsIgnoreCase(batch.getSourceDomain())) {
            summary = processKey(batch, requestedTask, files, extractRoot);
        } else if ("TIMS".equalsIgnoreCase(batch.getSourceDomain())) {
            summary = processTims(batch, requestedTask, files, extractRoot, committedRows ->
                    completeSuccessfulLoad(batch, requestedTask, files, event, started,
                            leaseToken, committedRows, true));
        } else {
            throw new IllegalArgumentException("不支持的上报来源：" + batch.getSourceDomain());
        }
        if (summary.errorCount > 0) {
            if ("KEY".equalsIgnoreCase(batch.getSourceDomain()) && summary.successCount > 0) {
                partialTaskAndBatch(batch, requestedTask, event, started, leaseToken,
                        summary.successCount, summary.errorCount);
                updateFileStatuses(files, "PARTIALLY_SUCCEEDED", summary.successCount, summary.errorCount,
                        "部分行已入库，请查看行级错误");
                return;
            }
            failTaskAndBatch(batch, requestedTask, event, started,
                    "解析发现 " + summary.errorCount + " 条错误，未触发后续加工",
                    leaseToken, summary.successCount, summary.errorCount);
            updateFileStatuses(files, "FAILED", summary.successCount, summary.errorCount,
                    "存在行级解析错误");
            return;
        }
        if ("KEY".equalsIgnoreCase(batch.getSourceDomain())) {
            inTransaction(() -> completeSuccessfulLoad(batch, requestedTask, files, event, started,
                    leaseToken, summary.successCount, false));
        }
    }

    private ProcessingSummary processKey(ReportBatch batch, ReportTask task,
                                         List<ReportFile> files, Path extractRoot) throws Exception {
        KeyReportProcessingResult result = keyService.process(
                extractRoot, batch.getOriginalFileName(), batch.getTreasuryCode());
        for (KeyFileParseError error : result.getErrors()) {
            persistError(batch, task, findFileId(files, error.getFileName()), null,
                    error.getLineNumber(), null, error.getRawContent(), error.getMessage());
        }
        if (legacyPendingService != null) {
            legacyPendingService.completeKey(batch, result, task.getCreateBy());
        }
        return new ProcessingSummary(result.getSuccessCount(), result.getErrorCount());
    }

    private ProcessingSummary processTims(ReportBatch batch, ReportTask task,
                                          List<ReportFile> files, Path extractRoot,
                                          TimsLoadCommitAction completion) throws Exception {
        YearMonth period = YearMonth.from(batch.getAccountingPeriod().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        TimsBusinessType type = TimsBusinessType.valueOf(batch.getBusinessType().toUpperCase(Locale.ROOT));
        TimsReportProcessingResult result = timsService.process(
                extractRoot, type, period, null, committedRows -> {
                    if (legacyPendingService != null) {
                        legacyPendingService.completeTims(batch, committedRows, task.getCreateBy());
                    }
                    completion.afterCommittedRowsLoaded(committedRows);
                });
        for (TimsExcelParseError error : result.getErrors()) {
            persistError(batch, task, findFileId(files, error.getFileName()), error.getSheetName(),
                    error.getRowNumber(), error.getColumnName(), error.getRawValue(), error.getMessage());
        }
        return new ProcessingSummary(result.getSuccessCount(), result.getErrorCount());
    }

    private void completeSuccessfulLoad(ReportBatch batch, ReportTask requestedTask,
                                        List<ReportFile> files, ReportBatchExecutionRequested event,
                                        Date started, String leaseToken, long successCount,
                                        boolean tims) {
        finishTask(requestedTask, event, started, "解析完成并提交，共 " + successCount + " 条",
                leaseToken, successCount, 0L);
        if (!"LOAD".equals(requestedTask.getTaskType())) {
            createCompletedLoadTask(batch, requestedTask, event, successCount);
        }
        updateFileStatuses(files, "SUCCEEDED", successCount, 0L, null);
        batch.setSuccessRowCount(successCount);
        batch.setErrorRowCount(0L);
        batch.setCurrentStage("LOAD");
        batch.setStatus("SUCCEEDED");
        batch.setProgressPercent(100);
        batch.setErrorSummary(null);
        if (tims) {
            batch.setProcessCallStatus(properties.isProcessDependenciesVerified()
                    ? "WAITING_MANUAL" : "DEPENDENCY_UNVERIFIED");
            batch.setResultSummary("解析及 STG 入库已提交，共 " + successCount + " 条；下游加工需人工调用");
        } else {
            batch.setProcessCallStatus("NOT_REQUIRED");
            batch.setResultSummary("解析及入库完成，共 " + successCount + " 条");
        }
        touchBatch(batch, event.getUsername());
    }

    private void executeProcess(ReportBatch batch, ReportTask task,
                                ReportBatchExecutionRequested event, String leaseToken) {
        Date started = new Date();
        startTask(batch, task, event, started);
        try {
            if (!properties.isProcessDependenciesVerified()) {
                throw new IllegalStateException("自动加工尚未通过 ETL/ADM 依赖核验门禁");
            }
            batch.setProcessCallStatus("PROCESSING");
            touchBatch(batch, event.getUsername());
            processService.callForBatch(batch, task.getId(), event.getUserId(), event.getUsername());
            finishTask(task, event, started, "原报送数据加工过程调用完成", leaseToken, 0L, 0L);
            batch.setCurrentStage("PROCESS");
            batch.setStatus("SUCCEEDED");
            batch.setProgressPercent(100);
            batch.setProcessCallStatus("SUCCEEDED");
            batch.setResultSummary("文件解析、入库及数据加工均已完成");
            batch.setErrorSummary(null);
            touchBatch(batch, event.getUsername());
        } catch (RuntimeException exception) {
            failTaskAndBatch(batch, task, event, started, safeMessage(exception), leaseToken, 0L, 1L);
            batch.setProcessCallStatus("FAILED");
            touchBatch(batch, event.getUsername());
        }
    }

    private void startTask(ReportBatch batch, ReportTask task, ReportBatchExecutionRequested event, Date now) {
        task.setStatus("PROCESSING");
        task.setProgressPercent(10);
        task.setStartedTime(now);
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(now);
        log(task, "QUEUED", "PROCESSING", "后台任务开始执行", event, 0L, 0L);
        batch.setStatus("PROCESSING");
        batch.setCurrentStage(task.getTaskType());
        touchBatch(batch, event.getUsername());
    }

    private void finishTask(ReportTask task, ReportBatchExecutionRequested event, Date started,
                            String message, String leaseToken, long successCount, long errorCount) {
        Date ended = new Date();
        task.setStatus("SUCCEEDED");
        task.setProgressPercent(100);
        task.setResultSummary(message);
        task.setEndedTime(ended);
        task.setDurationMs(ended.getTime() - started.getTime());
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(ended);
        requireOwnedCompletion(task, leaseToken);
        log(task, "PROCESSING", "SUCCEEDED", message, event, successCount, errorCount);
    }

    private void failTaskAndBatch(ReportBatch batch, ReportTask task, ReportBatchExecutionRequested event,
                                  Date started, String message, String leaseToken,
                                  long successCount, long errorCount) {
        Date ended = new Date();
        task.setStatus("FAILED");
        task.setErrorMessage(message);
        task.setEndedTime(ended);
        task.setDurationMs(ended.getTime() - started.getTime());
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(ended);
        requireOwnedCompletion(task, leaseToken);
        log(task, "PROCESSING", "FAILED", message, event, successCount, errorCount);
        batch.setStatus("FAILED");
        batch.setCurrentStage(task.getTaskType());
        batch.setErrorSummary(message);
        batch.setSuccessRowCount(successCount);
        batch.setErrorRowCount(errorCount);
        touchBatch(batch, event.getUsername());
    }

    private void partialTaskAndBatch(ReportBatch batch, ReportTask task,
                                     ReportBatchExecutionRequested event, Date started,
                                     String leaseToken, long successCount, long errorCount) {
        Date ended = new Date();
        String message = "成功 " + successCount + " 条，失败 " + errorCount + " 条";
        task.setStatus("PARTIALLY_SUCCEEDED");
        task.setProgressPercent(100);
        task.setResultSummary(message);
        task.setEndedTime(ended);
        task.setDurationMs(ended.getTime() - started.getTime());
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(ended);
        requireOwnedCompletion(task, leaseToken);
        log(task, "PROCESSING", "PARTIALLY_SUCCEEDED", message, event, successCount, errorCount);
        batch.setStatus("PARTIALLY_SUCCEEDED");
        batch.setCurrentStage(task.getTaskType());
        batch.setProgressPercent(100);
        batch.setSuccessRowCount(successCount);
        batch.setErrorRowCount(errorCount);
        batch.setResultSummary(message);
        batch.setErrorSummary("存在行级错误，请查看批次详情");
        touchBatch(batch, event.getUsername());
    }

    private ReportTask createCompletedLoadTask(ReportBatch batch, ReportTask parent,
                                               ReportBatchExecutionRequested event, long count) {
        Date now = new Date();
        ReportTask task = newTask(batch, parent, "LOAD", 4, event.getUsername(), now);
        task.setStatus("SUCCEEDED");
        task.setProgressPercent(100);
        task.setStartedTime(now);
        task.setEndedTime(now);
        task.setDurationMs(0L);
        task.setResultSummary("业务表及数仓暂存表入库完成，共 " + count + " 条");
        taskMapper.insert(task);
        log(task, null, "SUCCEEDED", task.getResultSummary(), event, count, 0L);
        return task;
    }

    private ReportTask newTask(ReportBatch batch, ReportTask parent, String type, int sequence,
                               String username, Date now) {
        ReportTask task = new ReportTask();
        task.setId(uuid());
        task.setBatchId(batch.getId());
        task.setParentTaskId(parent == null ? null : parent.getId());
        task.setTaskType(type);
        task.setSequenceNo(sequence);
        ReportTask previous = workflowMapper.findLatestTask(batch.getId(), type);
        task.setAttemptNo(previous == null || previous.getAttemptNo() == null ? 1 : previous.getAttemptNo() + 1);
        task.setExecutorKey("reporting-" + type.toLowerCase(Locale.ROOT));
        task.setCreateBy(username);
        task.setCreateTime(now);
        return task;
    }

    private void persistError(ReportBatch batch, ReportTask task, String fileId, String sheet,
                              long row, String column, String rawValue, String message) {
        ReportParseError error = new ReportParseError();
        error.setId(uuid());
        error.setBatchId(batch.getId());
        error.setFileId(fileId);
        error.setTaskId(task.getId());
        error.setBusinessType(batch.getBusinessType());
        error.setSheetName(sheet);
        error.setRowNumber(row);
        error.setColumnName(column);
        error.setRawValue(rawValue);
        error.setErrorCode("PARSE_ERROR");
        error.setErrorMessage(message);
        error.setCreateTime(new Date());
        errorMapper.insert(error);
    }

    private void updateFileStatuses(List<ReportFile> files, String status, long success,
                                    long errors, String errorSummary) {
        for (ReportFile file : files) {
            if ("ARCHIVE".equals(file.getFileRole())) {
                file.setTotalRowCount(success + errors);
                file.setSuccessRowCount(success);
                file.setErrorRowCount(errors);
            }
            file.setParseStatus(status);
            file.setErrorSummary(errorSummary);
            file.setUpdateTime(new Date());
            fileMapper.updateById(file);
        }
    }

    private void log(ReportTask task, String from, String to, String message,
                     ReportBatchExecutionRequested event, long success, long errors) {
        ReportTaskLog log = new ReportTaskLog();
        log.setId(uuid());
        log.setBatchId(task.getBatchId());
        log.setTaskId(task.getId());
        log.setStage(task.getTaskType());
        log.setFromStatus(from);
        log.setToStatus(to);
        log.setMessage(message);
        log.setProcessedRowCount(success + errors);
        log.setSuccessRowCount(success);
        log.setErrorRowCount(errors);
        log.setOperatorId(event.getUserId());
        log.setOperatorName(event.getUsername());
        log.setEventTime(new Date());
        logMapper.insert(log);
    }

    private Path extractRoot(List<ReportFile> files) {
        for (ReportFile file : files) {
            if ("ARCHIVE".equals(file.getFileRole()) && file.getStoragePath() != null) {
                Path archive = Paths.get(file.getStoragePath()).toAbsolutePath().normalize();
                Path archiveDirectory = archive.getParent();
                if (archiveDirectory != null && archiveDirectory.getParent() != null) {
                    return archiveDirectory.getParent().resolve("extracted").normalize();
                }
            }
        }
        throw new IllegalStateException("批次缺少归档文件，无法定位解压目录");
    }

    private String findFileId(List<ReportFile> files, String fileName) {
        String fallback = null;
        for (ReportFile file : files) {
            if (fallback == null) fallback = file.getId();
            if (fileName != null && fileName.equals(file.getOriginalName())) return file.getId();
        }
        if (fallback == null) throw new IllegalStateException("批次没有可关联的文件记录");
        return fallback;
    }

    private void requireOwnedLease(String taskId, String leaseToken) {
        Date now = new Date();
        if (workflowMapper.renewAndLockOwnedTask(taskId, leaseToken, leaseUntil(), now) != 1) {
            throw new TaskLeaseLostException();
        }
    }

    private void requireOwnedCompletion(ReportTask task, String leaseToken) {
        if (workflowMapper.completeOwnedTask(task, leaseToken) != 1) {
            throw new TaskLeaseLostException();
        }
    }

    private void recordExecutionFailure(ReportBatch batch, ReportTask task,
                                        ReportBatchExecutionRequested event, String leaseToken,
                                        Exception exception) {
        try {
            inTransaction(() -> {
                if (workflowMapper.lockOwnedTask(task.getId(), leaseToken) != 1) {
                    throw new TaskLeaseLostException();
                }
                Date started = task.getStartedTime() == null ? new Date() : task.getStartedTime();
                String message = safeMessage(exception);
                failTaskAndBatch(batch, task, event, started, message, leaseToken, 0L, 1L);
                if (legacyPendingService != null) {
                    legacyPendingService.fail(batch, message, event.getUserId());
                }
            });
        } catch (TaskLeaseLostException ignored) {
            // Recovery already transferred ownership; the stale worker is fenced out.
        } catch (Exception ignored) {
            // The scheduled recovery will expose an expired task if even failure recording cannot commit.
        }
    }

    private void inTransaction(CheckedAction action) throws Exception {
        if (transactionTemplate == null) {
            action.run();
            return;
        }
        try {
            transactionTemplate.execute(status -> {
                try {
                    action.run();
                    return null;
                } catch (RuntimeException exception) {
                    throw exception;
                } catch (Exception exception) {
                    throw new TransactionExecutionException(exception);
                }
            });
        } catch (TransactionExecutionException exception) {
            throw (Exception) exception.getCause();
        }
    }

    private void touchBatch(ReportBatch batch, String username) {
        batch.setUpdateBy(username);
        batch.setUpdateTime(new Date());
        workflowMapper.updateBatchState(batch);
    }

    private String normalizeStage(String value) {
        String stage = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
        if (!"PARSE".equals(stage) && !"LOAD".equals(stage) && !"PROCESS".equals(stage)) {
            throw new IllegalArgumentException("不支持的任务阶段：" + value);
        }
        return stage;
    }

    private String safeMessage(Exception exception) {
        return exception.getMessage() == null ? exception.getClass().getSimpleName() : exception.getMessage();
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private Date leaseUntil() {
        return new Date(System.currentTimeMillis() + properties.getTaskStaleTimeoutMinutes() * 60_000L);
    }

    @FunctionalInterface
    private interface CheckedAction {
        void run() throws Exception;
    }

    private static final class TaskLeaseLostException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final class TransactionExecutionException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private TransactionExecutionException(Exception cause) {
            super(cause.getMessage(), cause);
        }
    }

    private static final class ProcessingSummary {
        private final long successCount;
        private final long errorCount;

        private ProcessingSummary(long successCount, long errorCount) {
            this.successCount = successCount;
            this.errorCount = errorCount;
        }
    }
}
