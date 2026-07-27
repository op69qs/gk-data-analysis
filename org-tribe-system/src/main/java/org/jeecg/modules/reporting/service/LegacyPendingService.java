package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.legacy.LegacyKeyPending;
import org.jeecg.modules.reporting.legacy.LegacyTimsPending;
import org.jeecg.modules.reporting.mapper.LegacyPendingMapper;
import org.jeecg.modules.reporting.parser.KeyFileParseError;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/** Maintains the two original pending tables so the JAR-era monitoring contract remains available. */
@Service
public class LegacyPendingService {
    private static final String ORIGINAL_TIMS_TREASURY_CODE = "2200000000";
    private final LegacyPendingMapper mapper;

    public LegacyPendingService(LegacyPendingMapper mapper) {
        this.mapper = mapper;
    }

    /** Original JAR rejects a KEY ZIP when its base name already occurs in pending records. */
    public void assertKeyUploadAvailable(String originalFileName) {
        String zipBase = baseName(originalFileName);
        if (mapper.countKeyPendingByZipBase(zipBase) > 0) {
            throw new IllegalArgumentException("文件【" + originalFileName + "】重复上传");
        }
    }

    public void create(ReportBatch batch, List<ReportFile> files, String userId) {
        if ("KEY".equalsIgnoreCase(batch.getSourceDomain())) {
            mapper.insertKeyPending(keyPending(batch, files, userId));
        } else if ("TIMS".equalsIgnoreCase(batch.getSourceDomain())) {
            mapper.insertTimsPending(timsPending(batch, files, userId));
        }
    }

    public void completeKey(ReportBatch batch, KeyReportProcessingResult result, String userId) {
        LegacyKeyPending record = new LegacyKeyPending();
        record.setZipName(batch.getOriginalFileName());
        record.setSrCount(count(result, "SR"));
        record.setZcCount(count(result, "ZC"));
        record.setKcCount(count(result, "KC"));
        record.setTkCount(count(result, "TK"));
        record.setSrException(errors(result, "sr"));
        record.setZcException(errors(result, "zc"));
        record.setKcException(errors(result, "kc"));
        record.setTkException(errors(result, "tk"));
        record.setState("0");
        record.setModUserId(userId);
        record.setModTime(new Date());
        mapper.updateKeyPending(record);
    }

    public void completeTims(ReportBatch batch, long committedRows, String userId) {
        LegacyTimsPending record = new LegacyTimsPending();
        record.setId(batch.getId());
        record.setBizDate(batch.getAccountingPeriod());
        record.setDataCount(Math.toIntExact(committedRows));
        record.setFileException("");
        record.setState("0");
        record.setModUserId(userId);
        record.setModTime(new Date());
        mapper.updateTimsPending(record);
    }

    public void fail(ReportBatch batch, String message, String userId) {
        if ("TIMS".equalsIgnoreCase(batch.getSourceDomain())) {
            LegacyTimsPending record = new LegacyTimsPending();
            record.setId(batch.getId());
            record.setBizDate(batch.getAccountingPeriod());
            record.setDataCount(0);
            record.setFileException(message);
            record.setState("0");
            record.setModUserId(userId);
            record.setModTime(new Date());
            mapper.updateTimsPending(record);
        }
    }

    /** Remove the JAR-era monitoring row while retaining the new audit/archive records. */
    public void delete(ReportBatch batch) {
        if ("KEY".equalsIgnoreCase(batch.getSourceDomain())) {
            mapper.deleteKeyPending(batch.getId(), batch.getOriginalFileName());
        } else if ("TIMS".equalsIgnoreCase(batch.getSourceDomain())) {
            mapper.deleteTimsPending(batch.getId());
        }
    }

    private LegacyKeyPending keyPending(ReportBatch batch, List<ReportFile> files, String userId) {
        LegacyKeyPending record = new LegacyKeyPending();
        record.setId(batch.getId());
        record.setTreCode(batch.getTreasuryCode());
        record.setBizDate(batch.getAccountingPeriod());
        record.setZipName(batch.getOriginalFileName());
        String zipBase = baseName(batch.getOriginalFileName());
        for (ReportFile file : files) {
            String name = file.getOriginalName();
            if (name == null || !name.endsWith(".txt")) continue;
            String state = name.contains(zipBase) ? "0" : "1";
            if (name.contains("sr")) { record.setSrName(name); record.setSrNameState(state); }
            else if (name.contains("zc")) { record.setZcName(name); record.setZcNameState(state); }
            else if (name.contains("kc")) { record.setKcName(name); record.setKcNameState(state); }
            else if (name.contains("tk")) { record.setTkName(name); record.setTkNameState(state); }
        }
        ReportFile archive = archive(files);
        record.setZipPath(archive == null ? null : archive.getStoragePath());
        record.setUnzipDir(extractDirectory(archive));
        record.setState("1");
        record.setAddUserId(userId);
        record.setAddTime(new Date());
        return record;
    }

    private LegacyTimsPending timsPending(ReportBatch batch, List<ReportFile> files, String userId) {
        LegacyTimsPending record = new LegacyTimsPending();
        record.setId(batch.getId());
        // The JAR writes this fixed aggregate treasury code for every TIMS upload.
        record.setTreCode(ORIGINAL_TIMS_TREASURY_CODE);
        record.setBizType(timsType(batch.getBusinessType()));
        record.setBizDate(batch.getAccountingPeriod());
        record.setFileName(batch.getOriginalFileName());
        ReportFile archive = archive(files);
        // Preserve the original JAR's counter-intuitive column meanings exactly.
        record.setFilePath(archive == null ? null : archive.getStoragePath());
        record.setZipFilePath(extractDirectory(archive));
        record.setDataCount(0);
        record.setFileException("");
        record.setState("1");
        record.setAddUserId(userId);
        record.setAddTime(new Date());
        return record;
    }

    private int count(KeyReportProcessingResult result, String code) {
        KeyReportProcessingResult.TypeResult value = result.getTypeResult(code);
        return value == null ? 0 : value.getSuccessCount();
    }

    private String errors(KeyReportProcessingResult result, String marker) {
        return result.getErrors().stream()
                .filter(error -> error.getFileName() != null && error.getFileName().contains(marker))
                .map(this::format)
                .collect(Collectors.joining("\n"));
    }

    private String format(KeyFileParseError error) {
        return error.getFileName() + ":" + error.getLineNumber() + " " + error.getMessage();
    }

    private ReportFile archive(List<ReportFile> files) {
        for (ReportFile file : files) if ("ARCHIVE".equals(file.getFileRole())) return file;
        return null;
    }

    private String extractDirectory(ReportFile archive) {
        if (archive == null || archive.getStoragePath() == null) return null;
        java.nio.file.Path path = java.nio.file.Paths.get(archive.getStoragePath()).toAbsolutePath().normalize();
        return path.getParent() == null || path.getParent().getParent() == null
                ? null : path.getParent().getParent().resolve("extracted").toString();
    }

    private String baseName(String name) {
        if (name == null) return "";
        int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(0, dot);
    }

    private String timsType(String type) {
        String value = type == null ? "" : type.toUpperCase(Locale.ROOT);
        if ("INCOME".equals(value)) return "1";
        if ("PAYOUT".equals(value)) return "2";
        if ("STOCK".equals(value)) return "3";
        throw new IllegalArgumentException("TIMS 业务类型不合法：" + type);
    }
}
