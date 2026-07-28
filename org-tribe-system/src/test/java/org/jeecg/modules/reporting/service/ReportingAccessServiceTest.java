package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportingAccessServiceTest {
    @Test
    public void batchInsideServerDerivedPrefixIsAllowed() {
        ReportingUserScopeService scope = mock(ReportingUserScopeService.class);
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportBatch batch = batch("2201000000");
        when(scope.requireTreasuryPrefix("operator")).thenReturn("2201");
        when(batchMapper.selectById("batch-1")).thenReturn(batch);

        ReportBatch result = new ReportingAccessService(scope, batchMapper, mock(ReportFileMapper.class))
                .requireBatch("batch-1", "operator");

        assertEquals("batch-1", result.getId());
    }

    @Test(expected = SecurityException.class)
    public void otherTreasuryBatchIsRejectedEvenWhenIdIsKnown() {
        ReportingUserScopeService scope = mock(ReportingUserScopeService.class);
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        when(scope.requireTreasuryPrefix("operator")).thenReturn("2201");
        when(batchMapper.selectById("batch-1")).thenReturn(batch("5000"));

        new ReportingAccessService(scope, batchMapper, mock(ReportFileMapper.class))
                .requireBatch("batch-1", "operator");
    }

    private ReportBatch batch(String treasuryCode) {
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setTreasuryCode(treasuryCode);
        batch.setDelFlag(0);
        return batch;
    }
}
