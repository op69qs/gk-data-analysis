package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportParseErrorMapper;
import org.jeecg.modules.reporting.mapper.ReportProcessCallMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.mapper.ReportWorkflowMapper;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportBatchQueryServiceTest {
    @Test
    public void detailContainsAllTrackingDimensionsAndDeleteIsLogical() {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper logMapper = mock(ReportTaskLogMapper.class);
        ReportParseErrorMapper errorMapper = mock(ReportParseErrorMapper.class);
        ReportProcessCallMapper callMapper = mock(ReportProcessCallMapper.class);
        LegacyPendingService legacyPendingService = mock(LegacyPendingService.class);
        ReportWorkflowMapper workflowMapper = mock(ReportWorkflowMapper.class);
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setDelFlag(0);
        ReportFile file = new ReportFile();
        file.setId("file-1");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);
        when(workflowMapper.findBatchForUpdate("batch-1")).thenReturn(batch);
        when(fileMapper.selectList(any())).thenReturn(Collections.singletonList(file));
        when(taskMapper.selectList(any())).thenReturn(Collections.emptyList());
        when(logMapper.selectList(any())).thenReturn(Collections.emptyList());
        when(errorMapper.selectList(any())).thenReturn(Collections.emptyList());
        when(callMapper.selectList(any())).thenReturn(Collections.emptyList());

        ReportBatchQueryService service = new ReportBatchQueryService(
                batchMapper, fileMapper, taskMapper, logMapper, errorMapper, callMapper);
        service.setLegacyPendingService(legacyPendingService);
        service.setWorkflowMapper(workflowMapper);

        assertEquals(1, service.detail("batch-1").getFiles().size());
        service.logicalDelete("batch-1", "operator");

        assertEquals(Integer.valueOf(1), batch.getDelFlag());
        assertEquals("LOGICALLY_DELETED", batch.getStatus());
        verify(legacyPendingService).delete(batch);
        verify(batchMapper).updateById(batch);
    }
}
