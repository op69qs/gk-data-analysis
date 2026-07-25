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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportWorkflowServiceTest {

    @Test
    public void successfulTimsParseLoadsDataAndAutomaticallyCallsOriginalProcedure() throws Exception {
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
        properties.setAutoProcessEnabled(true);

        ReportBatch batch = batch("TIMS", "INCOME");
        ReportTask parseTask = task("parse-1", "PARSE");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(workflowMapper.findLatestTask("batch-1", "PARSE")).thenReturn(parseTask);
        when(workflowMapper.findBatchFiles("batch-1")).thenReturn(Collections.singletonList(archiveFile()));
        when(timsService.process(any(), eq(TimsBusinessType.INCOME), eq(java.time.YearMonth.of(2026, 7))))
                .thenReturn(new TimsReportProcessingResult(2, 8, Collections.emptyList()));

        ReportWorkflowService service = new ReportWorkflowService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, properties);
        service.execute(new ReportBatchExecutionRequested("batch-1", "PARSE", "u1", "operator"));

        verify(timsService).process(Paths.get("/tmp/reporting/batch-1/extracted"),
                TimsBusinessType.INCOME, java.time.YearMonth.of(2026, 7));
        verify(processService).callForBatch(eq(batch), any(), eq("u1"), eq("operator"));
        assertEquals("SUCCEEDED", batch.getStatus());
        assertEquals("SUCCEEDED", batch.getProcessCallStatus());
        assertEquals(Integer.valueOf(100), batch.getProgressPercent());
        assertEquals(Long.valueOf(8), batch.getSuccessRowCount());
        verify(batchMapper, org.mockito.Mockito.atLeastOnce()).updateById(batch);
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
        properties.setAutoProcessEnabled(true);

        ReportBatch batch = batch("TIMS", "INCOME");
        ReportTask parseTask = task("parse-1", "PARSE");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(workflowMapper.findLatestTask("batch-1", "PARSE")).thenReturn(parseTask);
        when(workflowMapper.findBatchFiles("batch-1")).thenReturn(Collections.singletonList(archiveFile()));
        org.jeecg.modules.reporting.parser.TimsExcelParseError error =
                new org.jeecg.modules.reporting.parser.TimsExcelParseError(
                        "收入1.xls", "收入数据", 2, "日期", "202513", "日期格式错误");
        when(timsService.process(any(), any(), any()))
                .thenReturn(new TimsReportProcessingResult(1, 0, Collections.singletonList(error)));

        ReportWorkflowService service = new ReportWorkflowService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, workflowMapper,
                keyService, timsService, processService, properties);
        service.execute(new ReportBatchExecutionRequested("batch-1", "PARSE", "u1", "operator"));

        verify(errorMapper).insert(any());
        verify(processService, never()).callForBatch(any(), any(), any(), any());
        assertEquals("FAILED", batch.getStatus());
        assertEquals("PARSE", batch.getCurrentStage());
        assertEquals(Long.valueOf(1), batch.getErrorRowCount());
        ArgumentCaptor<ReportTask> task = ArgumentCaptor.forClass(ReportTask.class);
        verify(taskMapper, org.mockito.Mockito.atLeastOnce()).updateById(task.capture());
        assertEquals("FAILED", task.getAllValues().get(0).getStatus());
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
