package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.legacy.LegacyTimsPending;
import org.jeecg.modules.reporting.mapper.LegacyPendingMapper;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
}
