package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TimsSpoolCodecTest {

    @Test
    public void binaryRoundTripPreservesAllJarFieldsAndControlCharacters() throws Exception {
        TimsReportRecord source = new TimsReportRecord();
        source.setDAcct(LocalDate.of(2025, 11, 1));
        source.setDAcctText("2025-11-01");
        source.setTreCode("2200000000");
        source.setTreasuryName("重庆\t市\n国库");
        source.setTaxOrgCode("");
        source.setLevel("1");
        source.setSubjectCode("101");
        source.setSubjectName("一般收入");
        source.setCurrentAmount(new BigDecimal("-10.20"));
        source.setYearAmount(new BigDecimal("30.00"));
        source.setFileName("收入1.xls");
        source.setSheetName("收入数据");
        source.setRowNumber(20001L);

        TimsReportRecord restored = new TimsSpoolCodec().decode(new TimsSpoolCodec().encode(source));

        assertEquals("2025-11-01", restored.getDAcctText());
        assertEquals(LocalDate.of(2025, 11, 1), restored.getDAcct());
        assertEquals("重庆\t市\n国库", restored.getTreasuryName());
        assertEquals("", restored.getTaxOrgCode());
        assertEquals(new BigDecimal("-10.20"), restored.getCurrentAmount());
        assertEquals(20001L, restored.getRowNumber());
        assertNull(restored.getBalance());
    }
}
