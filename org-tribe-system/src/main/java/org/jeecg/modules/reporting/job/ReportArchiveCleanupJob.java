package org.jeecg.modules.reporting.job;

import org.jeecg.modules.reporting.service.ReportArchiveCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportArchiveCleanupJob {
    private final ReportArchiveCleanupService service;

    public ReportArchiveCleanupJob(ReportArchiveCleanupService service) { this.service = service; }

    @Scheduled(cron = "${reporting.cleanup-cron:0 30 2 * * *}")
    public void cleanup() { service.cleanup(); }
}
