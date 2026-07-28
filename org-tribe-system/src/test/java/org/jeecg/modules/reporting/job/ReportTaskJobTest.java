package org.jeecg.modules.reporting.job;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.jeecg.modules.reporting.service.ReportWorkflowService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportTaskJobTest {
    @Test
    public void recoveryRequeuesStaleWorkAndDispatchesExactTaskId() {
        ReportWorkflowMapper mapper = mock(ReportWorkflowMapper.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);
        ReportTask task = new ReportTask();
        task.setId("task-7");
        task.setBatchId("batch-1");
        task.setTaskType("PARSE");
        task.setCreateBy("operator");
        when(mapper.findQueuedTasks(20)).thenReturn(Collections.singletonList(task));

        new ReportTaskJob(mock(ReportWorkflowService.class), mapper,
                new ReportingProperties(), publisher).recoverAndDispatch();

        verify(mapper).requeueExpiredTasks(any());
        verify(mapper).failExpiredProcessTasks(any());
        verify(mapper).failBatchesWithExpiredProcessTasks();
        verify(mapper).failStaleProcessCalls(any());
        ArgumentCaptor<ReportBatchExecutionRequested> event =
                ArgumentCaptor.forClass(ReportBatchExecutionRequested.class);
        verify(publisher).publishEvent(event.capture());
        assertEquals("task-7", event.getValue().getTaskId());
        assertEquals("batch-1", event.getValue().getBatchId());
    }
}
