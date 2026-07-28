package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.event.ReportBatchExecutionRequested;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportParseErrorMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportWorkflowServiceTest {

    @Test
    public void staleWorkerCannotProcessOrOverwriteAfterLeaseIsLost() throws Exception {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper logMapper = mock(ReportTaskLogMapper.class);
        ReportParseErrorMapper errorMapper = mock(ReportParseErrorMapper.class);
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        KeyReportProcessingService keyService = mock(KeyReportProcessingService.class);
        TimsReportProcessingService timsService = mock(TimsReportProcessingService.class);
        ReportProcessCallService processService = mock(ReportProcessCallService.class);
        ReportBatch batch = batch("TIMS", "INCOME");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(taskMapper.selectById("parse-1")).thenReturn(task("parse-1", "PARSE"));
        when(workflowMapper.claimTask(any(), any(), any(), any(), any())).thenReturn(1);
        when(workflowMapper.renewAndLockOwnedTask(any(), any(), any(), any())).thenReturn(0);
        when(workflowMapper.lockOwnedTask(any(), any())).thenReturn(0);

        ReportWorkflowService service = new ReportWorkflowService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, new ReportingProperties());
        service.execute(new ReportBatchExecutionRequested("parse-1", "batch-1", "PARSE", "u1", "operator"));

        verify(timsService, never()).process(any(), any(), any(), any(), any(), any());
        verify(workflowMapper, never()).updateBatchState(any());
        verify(workflowMapper, never()).completeOwnedTask(any(), any());
    }

    @Test
    public void successfulTimsParseStopsAfterStgAndWaitsForManualProcedureCall() throws Exception {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper logMapper = mock(ReportTaskLogMapper.class);
        ReportParseErrorMapper errorMapper = mock(ReportParseErrorMapper.class);
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        KeyReportProcessingService keyService = mock(KeyReportProcessingService.class);
        TimsReportProcessingService timsService = mock(TimsReportProcessingService.class);
        ReportProcessCallService processService = mock(ReportProcessCallService.class);
        ReportingProperties properties = new ReportingProperties();
        properties.setProcessDependenciesVerified(true);

        ReportBatch batch = batch("TIMS", "INCOME");
        ReportTask parseTask = task("parse-1", "PARSE");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(taskMapper.selectById("parse-1")).thenReturn(parseTask);
        when(workflowMapper.claimTask(any(), any(), eq("operator"), any(), any())).thenReturn(1);
        when(workflowMapper.renewAndLockOwnedTask(any(), any(), any(), any())).thenReturn(1);
        when(workflowMapper.updateOwnedTaskProgress(any(), any(), any(Integer.class), any(), any(), any())).thenReturn(1);
        when(workflowMapper.completeOwnedTask(any(), any())).thenReturn(1);
        when(workflowMapper.findBatchFiles("batch-1")).thenReturn(Collections.singletonList(archiveFile()));
        doAnswer(invocation -> {
            TimsLoadCommitAction action = invocation.getArgument(4);
            TimsPreparationAction preparation = invocation.getArgument(5);
            preparation.afterPrepared(2, 8L);
            action.afterCommittedRowsLoaded(8L);
            return new TimsReportProcessingResult(2, 8, Collections.emptyList());
        }).when(timsService).process(any(), eq(TimsBusinessType.INCOME),
                eq(java.time.YearMonth.of(2026, 7)), any(), any(), any());

        ReportWorkflowService service = new ReportWorkflowService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, properties);
        service.execute(new ReportBatchExecutionRequested("parse-1", "batch-1", "PARSE", "u1", "operator"));

        verify(timsService).process(eq(Paths.get("/tmp/reporting/batch-1/extracted")),
                eq(TimsBusinessType.INCOME), eq(java.time.YearMonth.of(2026, 7)),
                org.mockito.ArgumentMatchers.isNull(),
                org.mockito.ArgumentMatchers.any(TimsLoadCommitAction.class),
                org.mockito.ArgumentMatchers.any(TimsPreparationAction.class));
        verify(processService, never()).callForBatch(any(), any(), any(), any());
        assertEquals("SUCCEEDED", batch.getStatus());
        assertEquals("WAITING_MANUAL", batch.getProcessCallStatus());
        assertEquals(Integer.valueOf(100), batch.getProgressPercent());
        assertEquals(Long.valueOf(8), batch.getSuccessRowCount());
        verify(workflowMapper, org.mockito.Mockito.atLeastOnce()).updateBatchState(batch);
    }

    @Test
    public void parseErrorsStopBeforeOriginalProcedureAndRemainVisibleOnBatch() throws Exception {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper logMapper = mock(ReportTaskLogMapper.class);
        ReportParseErrorMapper errorMapper = mock(ReportParseErrorMapper.class);
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        KeyReportProcessingService keyService = mock(KeyReportProcessingService.class);
        TimsReportProcessingService timsService = mock(TimsReportProcessingService.class);
        ReportProcessCallService processService = mock(ReportProcessCallService.class);
        ReportingProperties properties = new ReportingProperties();
        properties.setProcessDependenciesVerified(true);

        ReportBatch batch = batch("TIMS", "INCOME");
        ReportTask parseTask = task("parse-1", "PARSE");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(taskMapper.selectById("parse-1")).thenReturn(parseTask);
        when(workflowMapper.claimTask(eq("parse-1"), any(), eq("operator"), any(), any())).thenReturn(1);
        when(workflowMapper.renewAndLockOwnedTask(any(), any(), any(), any())).thenReturn(1);
        when(workflowMapper.completeOwnedTask(any(), any())).thenReturn(1);
        when(workflowMapper.findBatchFiles("batch-1")).thenReturn(Collections.singletonList(archiveFile()));
        org.jeecg.modules.reporting.parser.TimsExcelParseError error =
                new org.jeecg.modules.reporting.parser.TimsExcelParseError(
                        "收入1.xls", "收入数据", 2, "日期", "202513", "日期格式错误");
        when(timsService.process(any(), any(), any(), any(), any(), any()))
                .thenReturn(new TimsReportProcessingResult(1, 0, Collections.singletonList(error)));

        ReportWorkflowService service = new ReportWorkflowService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, properties);
        service.execute(new ReportBatchExecutionRequested("parse-1", "batch-1", "PARSE", "u1", "operator"));

        verify(errorMapper).insert(any());
        verify(processService, never()).callForBatch(any(), any(), any(), any());
        assertEquals("FAILED", batch.getStatus());
        assertEquals("PARSE", batch.getCurrentStage());
        assertEquals(Long.valueOf(1), batch.getErrorRowCount());
        ArgumentCaptor<ReportTask> task = ArgumentCaptor.forClass(ReportTask.class);
        verify(workflowMapper).completeOwnedTask(task.capture(), any());
        assertEquals("FAILED", task.getValue().getStatus());
    }

    private ReportBatch batch(String source, String type) {
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setSourceDomain(source);
        batch.setBusinessType(type);
        batch.setAccountingPeriod(Date.from(LocalDate.of(2026, 7, 31)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        batch.setOriginalFileName("收入.zip");
        batch.setAutoProcessRequired(1);
        batch.setDelFlag(0);
        return batch;
    }

    private ReportTask task(String id, String type) {
        ReportTask task = new ReportTask();
        task.setId(id);
        task.setBatchId("batch-1");
        task.setTaskType(type);
        task.setAttemptNo(1);
        task.setStatus("QUEUED");
        task.setCreateTime(new Date());
        return task;
    }

    private ReportFile archiveFile() {
        ReportFile file = new ReportFile();
        file.setId("archive-1");
        file.setBatchId("batch-1");
        file.setFileRole("ARCHIVE");
        file.setOriginalName("收入.zip");
        file.setStoragePath("/tmp/reporting/batch-1/archive/source.zip");
        return file;
    }
}
