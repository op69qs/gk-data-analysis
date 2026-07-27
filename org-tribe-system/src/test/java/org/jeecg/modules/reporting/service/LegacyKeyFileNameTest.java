package org.jeecg.modules.reporting.service;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LegacyKeyFileNameTest {
    @Test
    public void derivesBusinessDateAndTreasuryUsingOriginalJarMarkers() {
        LegacyKeyFileName value = LegacyKeyFileName.parse("report-k2026-07-31t5000000000.zip");
        assertEquals(LocalDate.of(2026, 7, 31), value.getBusinessDate());
        assertEquals("5000000000", value.getTreasuryCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNameWithoutOriginalKAndTMarkers() {
        LegacyKeyFileName.parse("普通文件.zip");
    }
}
