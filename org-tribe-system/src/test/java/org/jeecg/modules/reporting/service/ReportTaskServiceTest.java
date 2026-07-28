package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportTaskServiceTest {

    @Test
    public void monthPeriodAlwaysUsesLastCalendarDay() {
        assertEquals(LocalDate.of(2024, 2, 29), ReportTaskService.monthEnd("2024-02"));
        assertEquals(LocalDate.of(2026, 7, 31), ReportTaskService.monthEnd("2026-07"));
    }

    @Test
    public void retryCreatesNewAttemptAndKeepsOriginalBatchPeriod() {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper logMapper = mock(ReportTaskLogMapper.class);
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setSourceDomain("TIMS");
        batch.setProcessCallStatus("WAITING_MANUAL");
        batch.setAccountingPeriod(Date.from(LocalDate.of(2026, 7, 31)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ReportTask previous = new ReportTask();
        previous.setId("task-old");
        previous.setAttemptNo(1);
        previous.setSequenceNo(5);
        when(workflowMapper.findBatchForUpdate("batch-1")).thenReturn(batch);
        when(workflowMapper.findLatestTask("batch-1", "PROCESS")).thenReturn(previous);

        ReportTask created = new ReportTaskService(
                batchMapper, taskMapper, logMapper, workflowMapper, publisher)
                .queueRetry("batch-1", "PROCESS", "u1", "operator");

        assertEquals(Integer.valueOf(2), created.getAttemptNo());
        assertEquals("task-old", created.getRetryOfTaskId());
        assertTrue(created.getRequestParams().contains("2026-07-31"));
        assertEquals("QUEUED", batch.getProcessCallStatus());
        verify(taskMapper).insert(created);
        ArgumentCaptor<ReportBatchExecutionRequested> event =
                ArgumentCaptor.forClass(ReportBatchExecutionRequested.class);
        verify(publisher).publishEvent(event.capture());
        assertEquals("batch-1", event.getValue().getBatchId());
        assertEquals("PROCESS", event.getValue().getRequestedTaskType());
        assertEquals(created.getId(), event.getValue().getTaskId());
    }

    @Test(expected = IllegalStateException.class)
    public void manualProcessRejectsUnverifiedDependencies() {
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setSourceDomain("TIMS");
        batch.setProcessCallStatus("DEPENDENCY_UNVERIFIED");
        when(workflowMapper.findBatchForUpdate("batch-1")).thenReturn(batch);

        new ReportTaskService(mock(ReportBatchMapper.class), mock(ReportTaskMapper.class),
                mock(ReportTaskLogMapper.class), workflowMapper, mock(ApplicationEventPublisher.class))
                .queueRetry("batch-1", "PROCESS", "u1", "operator");
    }

    @Test(expected = IllegalStateException.class)
    public void retryRejectsAnotherActiveAttempt() {
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        ReportTask active = new ReportTask();
        active.setStatus("PROCESSING");
        when(workflowMapper.findBatchForUpdate("batch-1")).thenReturn(batch);
        when(workflowMapper.countActiveTasks("batch-1")).thenReturn(1);
        when(workflowMapper.findLatestTask("batch-1", "PARSE")).thenReturn(active);

        new ReportTaskService(mock(ReportBatchMapper.class), mock(ReportTaskMapper.class),
                mock(ReportTaskLogMapper.class), workflowMapper, mock(ApplicationEventPublisher.class))
                .queueRetry("batch-1", "PARSE", "u1", "operator");
    }

    @Test(expected = ReportProcessBusyException.class)
    public void externalRunningProcessBlocksAnotherCall() {
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        when(workflowMapper.countExternalRunningProcess()).thenReturn(1);
        ReportProcessCallService service = new ReportProcessCallService(
                workflowMapper, mock(org.jeecg.modules.reporting.mapper.ReportProcessCallMapper.class));

        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setAccountingPeriod(new Date());
        service.callForBatch(batch, "task-1", "u1", "operator");
    }
}
