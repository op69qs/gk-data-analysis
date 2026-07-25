package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;
import org.jeecg.modules.reporting.exception.ReportFileHandlingException;
import org.jeecg.modules.reporting.exception.ReportUploadException;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.service.ReportArchiveService.ArchiveResult;
import org.jeecg.modules.reporting.vo.ReportBatchUploadResult;
import org.jeecg.modules.reporting.vo.ReportUploadCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class ReportBatchService {

    private static final String STATUS_QUEUED = "QUEUED";
    private static final String STATUS_PROCESSING = "PROCESSING";
    private static final String STATUS_SUCCEEDED = "SUCCEEDED";
    private static final String STATUS_FAILED = "FAILED";

    private final ReportBatchMapper batchMapper;
    private final ReportFileMapper fileMapper;
    private final ReportTaskMapper taskMapper;
    private final ReportTaskLogMapper taskLogMapper;
    private final ReportArchiveService archiveService;
    private ReportTaskService reportTaskService;
    private LegacyPendingService legacyPendingService;

    public ReportBatchService(ReportBatchMapper batchMapper,
                              ReportFileMapper fileMapper,
                              ReportTaskMapper taskMapper,
                              ReportTaskLogMapper taskLogMapper,
                              ReportArchiveService archiveService) {
        this.batchMapper = batchMapper;
        this.fileMapper = fileMapper;
        this.taskMapper = taskMapper;
        this.taskLogMapper = taskLogMapper;
        this.archiveService = archiveService;
    }

    @Autowired(required = false)
    public void setReportTaskService(ReportTaskService reportTaskService) {
        this.reportTaskService = reportTaskService;
    }

    @Autowired(required = false)
    public void setLegacyPendingService(LegacyPendingService legacyPendingService) {
        this.legacyPendingService = legacyPendingService;
    }

    @Transactional(noRollbackFor = ReportUploadException.class)
    public ReportBatchUploadResult createUploadBatch(MultipartFile file,
                                                      ReportUploadCommand command,
                                                      String userId,
                                                      String username) {
        String originalFileName = originalBaseName(file);
        ValidatedCommand validated = validate(command, originalFileName);
        if ("KEY".equals(validated.sourceDomain) && legacyPendingService != null) {
            legacyPendingService.assertKeyUploadAvailable(originalFileName);
        }
        String batchId = uuid();
        Date now = new Date();
        ReportBatch batch = new ReportBatch();
        batch.setId(batchId);
        batch.setBatchNo(createBatchNo(batchId));
        batch.setSourceDomain(validated.sourceDomain);
        batch.setBusinessType(validated.businessType);
        batch.setAccountingPeriod(validated.accountingPeriodDate);
        batch.setTreasuryCode("TIMS".equals(validated.sourceDomain) && trimToNull(command.getAllowedTreasuryPrefix()) != null
                ? trimToNull(command.getAllowedTreasuryPrefix()) : trimToNull(command.getTreasuryCode()) == null
                ? validated.derivedTreasuryCode : trimToNull(command.getTreasuryCode()));
        batch.setTreasuryName(trimToNull(command.getTreasuryName()));
        batch.setOriginalFileName(originalFileName);
        batch.setCurrentStage("ARCHIVE");
        batch.setStatus(STATUS_PROCESSING);
        batch.setProgressPercent(0);
        batch.setFileCount(0);
        batch.setSuccessRowCount(0L);
        batch.setErrorRowCount(0L);
        batch.setAutoProcessRequired(1);
        batch.setProcessCallStatus(STATUS_QUEUED);
        batch.setDelFlag(0);
        batch.setCreateBy(username);
        batch.setCreateTime(now);
        batchMapper.insert(batch);

        try {
            ArchiveResult archive = archiveService.archiveAndExtract(
                    file, validated.sourceDomain, validated.archivePeriod, batchId);
            PersistedFiles persistedFiles = persistFiles(batch, archive, username, now);
            String archiveFileId = persistedFiles.archiveFileId;
            persistCompletedTask(batchId, "ARCHIVE", 10, 1, archive.getOriginalFileName(),
                    userId, username, now);
            persistCompletedTask(batchId, "EXTRACT", 30, 2,
                    "已解压 " + archive.getExtractedFiles().size() + " 个业务文件",
                    userId, username, now);
            ReportTask parseTask = persistQueuedTask(batchId, "PARSE", 3, userId, username, now);

            batch.setOriginalFileName(archive.getOriginalFileName());
            batch.setCurrentStage("PARSE");
            batch.setStatus(STATUS_PROCESSING);
            batch.setProgressPercent(30);
            batch.setFileCount(archive.getExtractedFiles().size() + 1);
            batch.setResultSummary("文件已归档并安全解压，解析任务已排队；归档文件ID=" + archiveFileId);
            batch.setUpdateBy(username);
            batch.setUpdateTime(new Date());
            batchMapper.updateById(batch);
            if (legacyPendingService != null) {
                legacyPendingService.create(batch, persistedFiles.files, userId);
            }
            if (reportTaskService != null) {
                reportTaskService.publishInitial(parseTask.getId(), batchId, userId, username);
            }
            return toResult(batch);
        } catch (ReportFileHandlingException exception) {
            persistFailedArchive(batch, exception.getArchivePath(), username);
            persistFailedFileHandling(batch, exception.getStage(), userId, username, exception);
            throw new ReportUploadException(batchId, exception.getStage(), exception.getMessage(), exception);
        } catch (IllegalArgumentException | IOException exception) {
            persistFailedFileHandling(batch, "ARCHIVE", userId, username, exception);
            throw new ReportUploadException(batchId, "ARCHIVE", exception.getMessage(), exception);
        }
    }

    private void persistFailedArchive(ReportBatch batch, Path archivePath, String username) {
        if (archivePath == null || !Files.isRegularFile(archivePath)) return;
        try {
            ReportFile archive = new ReportFile();
            archive.setId(uuid());
            archive.setBatchId(batch.getId());
            archive.setFileRole("ARCHIVE");
            archive.setBusinessType(batch.getBusinessType());
            archive.setOriginalName(batch.getOriginalFileName());
            archive.setArchiveName(archivePath.getFileName().toString());
            archive.setRelativePath("archive/" + archivePath.getFileName());
            archive.setStoragePath(archivePath.toString());
            archive.setContentType("application/zip");
            archive.setFileExtension("zip");
            archive.setFileSize(Files.size(archivePath));
            archive.setSha256(sha256(archivePath));
            archive.setArchiveStatus(STATUS_SUCCEEDED);
            archive.setExtractStatus(STATUS_FAILED);
            archive.setParseStatus("NOT_STARTED");
            archive.setErrorSummary("解压失败，已保留归档以供追踪");
            initializeFileCounts(archive);
            archive.setRetained(1);
            archive.setDelFlag(0);
            archive.setCreateBy(username);
            archive.setCreateTime(new Date());
            fileMapper.insert(archive);
        } catch (IOException ignored) {
            // The batch/task failure remains visible even if damaged media metadata cannot be read.
        }
    }

    private PersistedFiles persistFiles(ReportBatch batch, ArchiveResult archive, String username, Date now)
            throws IOException {
        List<ReportFile> persisted = new ArrayList<>();
        ReportFile zipFile = new ReportFile();
        zipFile.setId(uuid());
        zipFile.setBatchId(batch.getId());
        zipFile.setFileRole("ARCHIVE");
        zipFile.setBusinessType(batch.getBusinessType());
        zipFile.setOriginalName(archive.getOriginalFileName());
        zipFile.setArchiveName(archive.getArchivePath().getFileName().toString());
        zipFile.setRelativePath("archive/" + archive.getArchivePath().getFileName());
        zipFile.setStoragePath(archive.getArchivePath().toString());
        zipFile.setContentType("application/zip");
        zipFile.setFileExtension("zip");
        zipFile.setFileSize(archive.getFileSize());
        zipFile.setSha256(archive.getSha256());
        zipFile.setArchiveStatus(STATUS_SUCCEEDED);
        zipFile.setExtractStatus(STATUS_SUCCEEDED);
        zipFile.setParseStatus(STATUS_QUEUED);
        initializeFileCounts(zipFile);
        zipFile.setRetained(1);
        zipFile.setDelFlag(0);
        zipFile.setCreateBy(username);
        zipFile.setCreateTime(now);
        fileMapper.insert(zipFile);
        persisted.add(zipFile);

        for (Path extracted : archive.getExtractedFiles()) {
            ReportFile child = new ReportFile();
            child.setId(uuid());
            child.setBatchId(batch.getId());
            child.setParentFileId(zipFile.getId());
            child.setFileRole("EXTRACTED");
            child.setBusinessType(batch.getBusinessType());
            child.setOriginalName(extracted.getFileName().toString());
            child.setRelativePath(archive.getExtractRoot().relativize(extracted).toString().replace('\\', '/'));
            child.setStoragePath(extracted.toString());
            child.setFileExtension(extensionOf(extracted.getFileName().toString()));
            child.setFileSize(Files.size(extracted));
            child.setSha256(sha256(extracted));
            child.setArchiveStatus("NOT_APPLICABLE");
            child.setExtractStatus(STATUS_SUCCEEDED);
            child.setParseStatus(STATUS_QUEUED);
            initializeFileCounts(child);
            child.setRetained(1);
            child.setDelFlag(0);
            child.setCreateBy(username);
            child.setCreateTime(now);
            fileMapper.insert(child);
            persisted.add(child);
        }
        return new PersistedFiles(zipFile.getId(), persisted);
    }

    private void initializeFileCounts(ReportFile file) {
        file.setTotalRowCount(0L);
        file.setSuccessRowCount(0L);
        file.setErrorRowCount(0L);
    }

    private void persistCompletedTask(String batchId,
                                      String taskType,
                                      int progress,
                                      int sequence,
                                      String message,
                                      String userId,
                                      String username,
                                      Date now) {
        ReportTask task = newTask(batchId, taskType, sequence, STATUS_SUCCEEDED, progress, username, now);
        task.setStartedTime(now);
        task.setEndedTime(now);
        task.setDurationMs(0L);
        task.setResultSummary(message);
        taskMapper.insert(task);
        persistLog(task, null, STATUS_SUCCEEDED, message, userId, username, now);
    }

    private ReportTask persistQueuedTask(String batchId,
                                   String taskType,
                                   int sequence,
                                   String userId,
                                   String username,
                                   Date now) {
        ReportTask task = newTask(batchId, taskType, sequence, STATUS_QUEUED, 0, username, now);
        taskMapper.insert(task);
        persistLog(task, null, STATUS_QUEUED, "等待后台执行", userId, username, now);
        return task;
    }

    private ReportTask newTask(String batchId,
                               String taskType,
                               int sequence,
                               String status,
                               int progress,
                               String username,
                               Date now) {
        ReportTask task = new ReportTask();
        task.setId(uuid());
        task.setBatchId(batchId);
        task.setTaskType(taskType);
        task.setSequenceNo(sequence);
        task.setAttemptNo(1);
        task.setStatus(status);
        task.setProgressPercent(progress);
        task.setExecutorKey("reporting-" + taskType.toLowerCase(Locale.ROOT));
        task.setCreateBy(username);
        task.setCreateTime(now);
        return task;
    }

    private void persistLog(ReportTask task,
                            String fromStatus,
                            String toStatus,
                            String message,
                            String userId,
                            String username,
                            Date now) {
        ReportTaskLog log = new ReportTaskLog();
        log.setId(uuid());
        log.setBatchId(task.getBatchId());
        log.setTaskId(task.getId());
        log.setStage(task.getTaskType());
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setMessage(message);
        log.setProcessedRowCount(0L);
        log.setSuccessRowCount(0L);
        log.setErrorRowCount(0L);
        log.setOperatorId(userId);
        log.setOperatorName(username);
        log.setEventTime(now);
        taskLogMapper.insert(log);
    }

    private void persistFailedFileHandling(ReportBatch batch,
                                           String stage,
                                           String userId,
                                           String username,
                                           Exception exception) {
        Date now = new Date();
        int sequence = "EXTRACT".equals(stage) ? 2 : 1;
        ReportTask task = newTask(batch.getId(), stage, sequence, STATUS_FAILED, 0, username, now);
        task.setStartedTime(batch.getCreateTime());
        task.setEndedTime(now);
        task.setDurationMs(now.getTime() - batch.getCreateTime().getTime());
        task.setErrorMessage(exception.getMessage());
        taskMapper.insert(task);
        persistLog(task, STATUS_PROCESSING, STATUS_FAILED, exception.getMessage(), userId, username, now);

        batch.setStatus(STATUS_FAILED);
        batch.setCurrentStage(stage);
        batch.setProgressPercent(0);
        batch.setErrorSummary(exception.getMessage());
        batch.setUpdateBy(username);
        batch.setUpdateTime(now);
        batchMapper.updateById(batch);
    }

    private ValidatedCommand validate(ReportUploadCommand command, String originalFileName) {
        if (command == null) {
            throw new IllegalArgumentException("上报参数不能为空");
        }
        String sourceDomain = upper(command.getSourceDomain());
        if (!"KEY".equals(sourceDomain) && !"TIMS".equals(sourceDomain)) {
            throw new IllegalArgumentException("上报来源只能是 KEY 或 TIMS");
        }

        String businessType = upper(command.getBusinessType());
        if ("TIMS".equals(sourceDomain)) {
            if (!"INCOME".equals(businessType) && !"PAYOUT".equals(businessType)
                    && !"STOCK".equals(businessType)) {
                throw new IllegalArgumentException("TIMS 类型只能是收入、支出或库存");
            }
            YearMonth period = parsePeriod(command.getAccountingPeriod());
            return new ValidatedCommand(sourceDomain, businessType, period.toString(),
                    toDate(period.atEndOfMonth()), null);
        }

        if (businessType == null) {
            businessType = "ALL";
        }
        if (!"ALL".equals(businessType) && !"INCOME".equals(businessType)
                && !"PAYOUT".equals(businessType) && !"STOCK".equals(businessType)
                && !"BACK".equals(businessType)) {
            throw new IllegalArgumentException("KEY 类型不合法");
        }
        LegacyKeyFileName legacy = LegacyKeyFileName.parse(originalFileName);
        String allowedPrefix = trimToNull(command.getAllowedTreasuryPrefix());
        if (allowedPrefix != null && !legacy.getTreasuryCode().startsWith(allowedPrefix)) {
            throw new IllegalArgumentException("KEY 文件名中的国库超出当前用户数据范围");
        }
        String rawPeriod = trimToNull(command.getAccountingPeriod());
        if (rawPeriod != null && !YearMonth.from(legacy.getBusinessDate()).equals(parsePeriod(rawPeriod))) {
            throw new IllegalArgumentException("KEY 页面账期与文件名中的业务日期不一致");
        }
        String requestedTreasury = trimToNull(command.getTreasuryCode());
        if (requestedTreasury != null && !requestedTreasury.equals(legacy.getTreasuryCode())) {
            throw new IllegalArgumentException("KEY 页面国库代码与文件名中的国库代码不一致");
        }
        YearMonth period = YearMonth.from(legacy.getBusinessDate());
        return new ValidatedCommand(sourceDomain, businessType, period.toString(),
                toDate(legacy.getBusinessDate()), legacy.getTreasuryCode());
    }

    private YearMonth parsePeriod(String value) {
        try {
            return YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new IllegalArgumentException("账期必须为 yyyy-MM", exception);
        }
    }

    private Date toDate(LocalDate value) {
        return Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private String upper(String value) {
        String trimmed = trimToNull(value);
        return trimmed == null ? null : trimmed.toUpperCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    private String originalBaseName(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return "upload.zip";
        }
        String value = file.getOriginalFilename().replace('\\', '/');
        int separator = value.lastIndexOf('/');
        return (separator >= 0 ? value.substring(separator + 1) : value)
                .replaceAll("[\\p{Cntrl}]", "").trim();
    }

    private String createBatchNo(String batchId) {
        return "RPT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))
                + "-" + batchId.substring(0, 8);
    }

    private String extensionOf(String name) {
        int separator = name.lastIndexOf('.');
        return separator < 0 ? "" : name.substring(separator + 1).toLowerCase(Locale.ROOT);
    }

    private String sha256(Path file) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
        try (InputStream input = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        StringBuilder value = new StringBuilder(64);
        for (byte current : digest.digest()) {
            value.append(String.format("%02x", current & 0xff));
        }
        return value.toString();
    }

    private ReportBatchUploadResult toResult(ReportBatch batch) {
        return new ReportBatchUploadResult(batch.getId(), batch.getBatchNo(), batch.getStatus(),
                batch.getCurrentStage(), batch.getProgressPercent());
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final class ValidatedCommand {
        private final String sourceDomain;
        private final String businessType;
        private final String archivePeriod;
        private final Date accountingPeriodDate;
        private final String derivedTreasuryCode;

        private ValidatedCommand(String sourceDomain,
                                 String businessType,
                                 String archivePeriod,
                                 Date accountingPeriodDate,
                                 String derivedTreasuryCode) {
            this.sourceDomain = sourceDomain;
            this.businessType = businessType;
            this.archivePeriod = archivePeriod;
            this.accountingPeriodDate = accountingPeriodDate;
            this.derivedTreasuryCode = derivedTreasuryCode;
        }
    }

    private static final class PersistedFiles {
        private final String archiveFileId;
        private final List<ReportFile> files;

        private PersistedFiles(String archiveFileId, List<ReportFile> files) {
            this.archiveFileId = archiveFileId;
            this.files = files;
        }
    }
}
