package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.legacy.LegacyTimsPending;
import org.jeecg.modules.reporting.mapper.LegacyPendingMapper;
import org.junit.Test;

import java.util.Collections;
import java.util.Arrays;
import java.sql.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import org.mockito.ArgumentCaptor;

public class LegacyPendingServiceTest {

    @Test
    public void rejectsKeyZipWhoseBaseNameAlreadyExistsLikeOriginalJar() {
        LegacyPendingMapper mapper = mock(LegacyPendingMapper.class);
        when(mapper.countKeyPendingByZipBase("k2026-07-31t2200000000")).thenReturn(1);
        LegacyPendingService service = new LegacyPendingService(mapper);

        try {
            service.assertKeyUploadAvailable("k2026-07-31t2200000000.zip");
            fail("duplicate KEY archive should be rejected");
        } catch (IllegalArgumentException exception) {
            assertTrue(exception.getMessage().contains("重复上传"));
        }
    }

    @Test
    public void preservesOriginalTimsPathColumnMeanings() {
        LegacyPendingMapper mapper = mock(LegacyPendingMapper.class);
        LegacyPendingService service = new LegacyPendingService(mapper);
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setSourceDomain("TIMS");
        batch.setBusinessType("INCOME");
        batch.setOriginalFileName("收入.zip");
        ReportFile archive = new ReportFile();
        archive.setFileRole("ARCHIVE");
        archive.setStoragePath("/reports/tims/2026-07/batch-1/archive/source.zip");

        service.create(batch, Collections.singletonList(archive), "u1");

        ArgumentCaptor<LegacyTimsPending> captor = ArgumentCaptor.forClass(LegacyTimsPending.class);
        verify(mapper).insertTimsPending(captor.capture());
        assertEquals("2200000000", captor.getValue().getTreCode());
        assertEquals("/reports/tims/2026-07/batch-1/archive/source.zip", captor.getValue().getFilePath());
        assertEquals("/reports/tims/2026-07/batch-1/extracted", captor.getValue().getZipFilePath());
    }

    @Test
    public void successfulTimsProcessingRebuildsPendingForEveryTreasury() {
        LegacyPendingMapper mapper = mock(LegacyPendingMapper.class);
        LegacyTimsPending uploaded = new LegacyTimsPending();
        uploaded.setId("batch-1");
        uploaded.setFileName("收入.zip");
        uploaded.setFilePath("/archive/source.zip");
        uploaded.setZipFilePath("/extracted");
        when(mapper.findTimsPendingById("batch-1")).thenReturn(uploaded);
        TimsReportProcessingResult result = new TimsReportProcessingResult(2, 3,
                Collections.emptyList(), Arrays.asList(
                new TimsReportProcessingResult.TreasuryCount(Date.valueOf("2025-11-30"), "2200000000", 1),
                new TimsReportProcessingResult.TreasuryCount(Date.valueOf("2025-11-30"), "2200100000", 2)));
        ReportBatch batch = new ReportBatch();
        batch.setId("batch-1");
        batch.setSourceDomain("TIMS");
        batch.setBusinessType("INCOME");
        batch.setOriginalFileName("收入.zip");

        new LegacyPendingService(mapper).completeTims(batch, result, "u1");

        verify(mapper, times(2)).deleteTimsPendingScope(
                org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq("1"),
                org.mockito.ArgumentMatchers.eq(Date.valueOf("2025-11-30")),
                org.mockito.ArgumentMatchers.anyString());
        ArgumentCaptor<LegacyTimsPending> records = ArgumentCaptor.forClass(LegacyTimsPending.class);
        verify(mapper, times(2)).insertTimsPending(records.capture());
        assertEquals("batch-1-1", records.getAllValues().get(0).getId());
        assertEquals("batch-1-2", records.getAllValues().get(1).getId());
        assertEquals("2200100000", records.getAllValues().get(1).getTreCode());
        assertEquals(Integer.valueOf(2), records.getAllValues().get(1).getDataCount());
        assertEquals("/archive/source.zip", records.getAllValues().get(1).getFilePath());
    }
}
