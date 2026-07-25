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
    private LegacyPendingService legacyPendingService;

    public ReportWorkflowService(ReportBatchMapper batchMapper, ReportFileMapper fileMapper,
                                 ReportTaskMapper taskMapper, ReportTaskLogMapper logMapper,
                                 ReportParseErrorMapper errorMapper, ReportWorkflowMapper workflowMapper,
                                 KeyReportProcessingService keyService,
                                 TimsReportProcessingService timsService,
                                 ReportProcessCallService processService,
                                 ReportingProperties properties) {
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
        ReportTask task = workflowMapper.findLatestTask(batch.getId(), requested);
        if (task == null || !"QUEUED".equals(task.getStatus())) {
            return;
        }
        if ("PROCESS".equals(requested)) {
            executeProcess(batch, task, event);
            return;
        }
        executeParseAndLoad(batch, task, event);
    }

    private void executeParseAndLoad(ReportBatch batch, ReportTask requestedTask,
                                     ReportBatchExecutionRequested event) {
        Date started = new Date();
        startTask(batch, requestedTask, event, started);
        List<ReportFile> files = workflowMapper.findBatchFiles(batch.getId());
        try {
            Path extractRoot = extractRoot(files);
            ProcessingSummary summary;
            if ("KEY".equalsIgnoreCase(batch.getSourceDomain())) {
                summary = processKey(batch, requestedTask, files, extractRoot);
            } else if ("TIMS".equalsIgnoreCase(batch.getSourceDomain())) {
                summary = processTims(batch, requestedTask, files, extractRoot);
            } else {
                throw new IllegalArgumentException("不支持的上报来源：" + batch.getSourceDomain());
            }
            if (summary.errorCount > 0) {
                failTaskAndBatch(batch, requestedTask, event, started,
                        "解析发现 " + summary.errorCount + " 条错误，未触发后续加工",
                        summary.successCount, summary.errorCount);
                updateFileStatuses(files, "FAILED", summary.successCount, summary.errorCount,
                        "存在行级解析错误");
                return;
            }

            finishTask(requestedTask, event, started, "解析完成，共 " + summary.successCount + " 条",
                    summary.successCount, 0L);
            ReportTask loadTask = "LOAD".equals(requestedTask.getTaskType())
                    ? requestedTask : createCompletedLoadTask(batch, requestedTask, event, summary.successCount);
            updateFileStatuses(files, "SUCCEEDED", summary.successCount, 0L, null);
            batch.setSuccessRowCount(summary.successCount);
            batch.setErrorRowCount(0L);
            batch.setCurrentStage("LOAD");
            batch.setProgressPercent(85);
            batch.setResultSummary("解析及入库完成，共 " + summary.successCount + " 条");

            if (shouldAutomaticallyProcess(batch)) {
                executeProcess(batch, createQueuedProcessTask(batch, loadTask, event), event);
            } else {
                batch.setStatus("SUCCEEDED");
                batch.setProgressPercent(100);
                batch.setProcessCallStatus("NOT_REQUIRED");
                touchBatch(batch, event.getUsername());
            }
        } catch (Exception exception) {
            failTaskAndBatch(batch, requestedTask, event, started,
                    safeMessage(exception), 0L, 1L);
            if (legacyPendingService != null) {
                legacyPendingService.fail(batch, safeMessage(exception), event.getUserId());
            }
        }
    }

    private ProcessingSummary processKey(ReportBatch batch, ReportTask task,
                                         List<ReportFile> files, Path extractRoot) throws Exception {
        KeyReportProcessingResult result = keyService.process(extractRoot, batch.getOriginalFileName());
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
                                          List<ReportFile> files, Path extractRoot) throws Exception {
        YearMonth period = YearMonth.from(batch.getAccountingPeriod().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        TimsBusinessType type = TimsBusinessType.valueOf(batch.getBusinessType().toUpperCase(Locale.ROOT));
        TimsReportProcessingResult result = timsService.process(extractRoot, type, period);
        for (TimsExcelParseError error : result.getErrors()) {
            persistError(batch, task, findFileId(files, error.getFileName()), error.getSheetName(),
                    error.getRowNumber(), error.getColumnName(), error.getRawValue(), error.getMessage());
        }
        if (legacyPendingService != null) {
            legacyPendingService.completeTims(batch, result, task.getCreateBy());
        }
        return new ProcessingSummary(result.getSuccessCount(), result.getErrorCount());
    }

    private void executeProcess(ReportBatch batch, ReportTask task, ReportBatchExecutionRequested event) {
        Date started = new Date();
        startTask(batch, task, event, started);
        batch.setProcessCallStatus("PROCESSING");
        touchBatch(batch, event.getUsername());
        try {
            processService.callForBatch(batch, task.getId(), event.getUserId(), event.getUsername());
            finishTask(task, event, started, "原报送数据加工过程调用完成", 0L, 0L);
            batch.setCurrentStage("PROCESS");
            batch.setStatus("SUCCEEDED");
            batch.setProgressPercent(100);
            batch.setProcessCallStatus("SUCCEEDED");
            batch.setResultSummary("文件解析、入库及数据加工均已完成");
            batch.setErrorSummary(null);
            touchBatch(batch, event.getUsername());
        } catch (RuntimeException exception) {
            failTaskAndBatch(batch, task, event, started, safeMessage(exception), 0L, 1L);
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
        taskMapper.updateById(task);
        log(task, "QUEUED", "PROCESSING", "后台任务开始执行", event, 0L, 0L);
        batch.setStatus("PROCESSING");
        batch.setCurrentStage(task.getTaskType());
        touchBatch(batch, event.getUsername());
    }

    private void finishTask(ReportTask task, ReportBatchExecutionRequested event, Date started,
                            String message, long successCount, long errorCount) {
        Date ended = new Date();
        task.setStatus("SUCCEEDED");
        task.setProgressPercent(100);
        task.setResultSummary(message);
        task.setEndedTime(ended);
        task.setDurationMs(ended.getTime() - started.getTime());
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(ended);
        taskMapper.updateById(task);
        log(task, "PROCESSING", "SUCCEEDED", message, event, successCount, errorCount);
    }

    private void failTaskAndBatch(ReportBatch batch, ReportTask task, ReportBatchExecutionRequested event,
                                  Date started, String message, long successCount, long errorCount) {
        Date ended = new Date();
        task.setStatus("FAILED");
        task.setErrorMessage(message);
        task.setEndedTime(ended);
        task.setDurationMs(ended.getTime() - started.getTime());
        task.setUpdateBy(event.getUsername());
        task.setUpdateTime(ended);
        taskMapper.updateById(task);
        log(task, "PROCESSING", "FAILED", message, event, successCount, errorCount);
        batch.setStatus("FAILED");
        batch.setCurrentStage(task.getTaskType());
        batch.setErrorSummary(message);
        batch.setSuccessRowCount(successCount);
        batch.setErrorRowCount(errorCount);
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

    private ReportTask createQueuedProcessTask(ReportBatch batch, ReportTask parent,
                                               ReportBatchExecutionRequested event) {
        Date now = new Date();
        ReportTask task = newTask(batch, parent, "PROCESS", 5, event.getUsername(), now);
        task.setStatus("QUEUED");
        task.setProgressPercent(0);
        taskMapper.insert(task);
        log(task, null, "QUEUED", "等待调用原报送数据加工过程", event, 0L, 0L);
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

    private boolean shouldAutomaticallyProcess(ReportBatch batch) {
        return "TIMS".equalsIgnoreCase(batch.getSourceDomain())
                && Integer.valueOf(1).equals(batch.getAutoProcessRequired())
                && properties.isAutoProcessEnabled();
    }

    private void touchBatch(ReportBatch batch, String username) {
        batch.setUpdateBy(username);
        batch.setUpdateTime(new Date());
        batchMapper.updateById(batch);
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

    private static final class ProcessingSummary {
        private final long successCount;
        private final long errorCount;

        private ProcessingSummary(long successCount, long errorCount) {
            this.successCount = successCount;
            this.errorCount = errorCount;
        }
    }
}
