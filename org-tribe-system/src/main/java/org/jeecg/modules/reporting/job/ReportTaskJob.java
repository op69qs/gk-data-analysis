package org.jeecg.modules.reporting.job;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.service.ReportWorkflowService;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReportTaskJob {
    private final ReportWorkflowService workflowService;
    private final ReportWorkflowMapper workflowMapper;
    private final ReportingProperties properties;
    private final ApplicationEventPublisher publisher;

    public ReportTaskJob(ReportWorkflowService workflowService, ReportWorkflowMapper workflowMapper,
                         ReportingProperties properties, ApplicationEventPublisher publisher) {
        this.workflowService = workflowService;
        this.workflowMapper = workflowMapper;
        this.properties = properties;
        this.publisher = publisher;
    }

    @Async("reportingTaskExecutor")
    @EventListener
    public void execute(ReportBatchExecutionRequested event) {
        workflowService.execute(event);
    }

    /** Database-backed recovery makes queued work survive application restarts. */
    @Scheduled(fixedDelayString = "${reporting.task-scan-delay-ms:10000}")
    public void recoverAndDispatch() {
        java.util.Date now = new java.util.Date();
        workflowMapper.requeueExpiredTasks(now);
        workflowMapper.failExpiredProcessTasks(now);
        workflowMapper.failBatchesWithExpiredProcessTasks();
        workflowMapper.failStaleProcessCalls(new java.util.Date(
                now.getTime() - properties.getProcessStaleTimeoutMinutes() * 60_000L));
        for (ReportTask task : workflowMapper.findQueuedTasks(properties.getTaskScanBatchSize())) {
            String operator = task.getCreateBy() == null ? "system-recovery" : task.getCreateBy();
            publisher.publishEvent(new ReportBatchExecutionRequested(
                    task.getId(), task.getBatchId(), task.getTaskType(), operator, operator));
        }
    }
}
