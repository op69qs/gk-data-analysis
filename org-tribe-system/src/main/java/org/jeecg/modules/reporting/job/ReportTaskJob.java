package org.jeecg.modules.reporting.job;

import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.service.ReportWorkflowService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ReportTaskJob {
    private final ReportWorkflowService workflowService;

    public ReportTaskJob(ReportWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @Async("reportingTaskExecutor")
    @EventListener
    public void execute(ReportBatchExecutionRequested event) {
        workflowService.execute(event);
    }
}
