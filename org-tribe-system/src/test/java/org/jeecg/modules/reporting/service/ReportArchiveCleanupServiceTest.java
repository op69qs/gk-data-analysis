package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ReportArchiveCleanupServiceTest {
    @Test
    public void zeroRetentionKeepsPhysicalCleanupDisabled() {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportingProperties properties = new ReportingProperties();
        properties.setRetentionDays(0);

        int cleaned = new ReportArchiveCleanupService(batchMapper, fileMapper, properties).cleanup();

        assertEquals(0, cleaned);
        verifyZeroInteractions(batchMapper, fileMapper);
    }
}
