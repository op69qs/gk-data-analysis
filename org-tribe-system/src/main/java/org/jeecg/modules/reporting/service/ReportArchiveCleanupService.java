package org.jeecg.modules.reporting.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReportArchiveCleanupService {
    private final ReportBatchMapper batchMapper;
    private final ReportFileMapper fileMapper;
    private final ReportingProperties properties;

    public ReportArchiveCleanupService(ReportBatchMapper batchMapper, ReportFileMapper fileMapper,
                                       ReportingProperties properties) {
        this.batchMapper = batchMapper;
        this.fileMapper = fileMapper;
        this.properties = properties;
    }

    /** Only logically deleted batches older than the explicitly configured retention period are eligible. */
    public int cleanup() {
        if (properties.getRetentionDays() <= 0) return 0;
        Date cutoff = Date.from(Instant.now().minus(properties.getRetentionDays(), ChronoUnit.DAYS));
        List<ReportBatch> batches = batchMapper.selectList(new QueryWrapper<ReportBatch>()
                .eq("del_flag", 1).lt("update_time", cutoff));
        int cleaned = 0;
        for (ReportBatch batch : batches) {
            List<ReportFile> files = fileMapper.selectList(new QueryWrapper<ReportFile>()
                    .eq("batch_id", batch.getId()).eq("retained", 1));
            Path batchRoot = batchRoot(files);
            if (batchRoot == null || !deleteBatchRoot(batchRoot)) continue;
            for (ReportFile file : files) {
                file.setRetained(0);
                file.setUpdateTime(new Date());
                fileMapper.updateById(file);
            }
            cleaned++;
        }
        return cleaned;
    }

    private Path batchRoot(List<ReportFile> files) {
        Path archiveRoot = Paths.get(properties.getArchiveRoot()).toAbsolutePath().normalize();
        for (ReportFile file : files) {
            if (!"ARCHIVE".equals(file.getFileRole()) || file.getStoragePath() == null) continue;
            Path archive = Paths.get(file.getStoragePath()).toAbsolutePath().normalize();
            Path directory = archive.getParent();
            Path batchRoot = directory == null ? null : directory.getParent();
            if (batchRoot != null && batchRoot.startsWith(archiveRoot) && !batchRoot.equals(archiveRoot)) return batchRoot;
        }
        return null;
    }

    private boolean deleteBatchRoot(Path batchRoot) {
        if (!Files.exists(batchRoot)) return true;
        try (Stream<Path> paths = Files.walk(batchRoot)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try { Files.deleteIfExists(path); }
                catch (IOException exception) { throw new CleanupFailure(exception); }
            });
            return true;
        } catch (IOException | CleanupFailure exception) {
            return false;
        }
    }

    private static final class CleanupFailure extends RuntimeException {
        private CleanupFailure(IOException cause) { super(cause); }
    }
}
