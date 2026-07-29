package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.TimsReportMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimsAtomicLoadServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void deletesOnceAndInsertsSpoolInBoundedBatchesBeforeReconciling() throws Exception {
        TimsReportMapper mapper = mock(TimsReportMapper.class);
        when(mapper.insertStgIncome(anyList(), eq("202511"), eq("20260727")))
                .thenAnswer(invocation -> ((List<?>) invocation.getArgument(0)).size());
        when(mapper.countStgIncome("202511")).thenReturn(3L);
        TimsPreparationResult prepared = prepared(3);

        long committed = new TimsAtomicLoadService(mapper, 2)
                .load(prepared, TimsBusinessType.INCOME, YearMonth.of(2025, 11), "20260727");

        assertEquals(3L, committed);
        verify(mapper).deleteStgIncome("202511");
        ArgumentCaptor<List<TimsReportRecord>> batches = ArgumentCaptor.forClass(List.class);
        verify(mapper, org.mockito.Mockito.times(2))
                .insertStgIncome(batches.capture(), eq("202511"), eq("20260727"));
        assertTrue(batches.getAllValues().stream().allMatch(rows -> rows.size() <= 2));
    }

    @Test(expected = IllegalStateException.class)
    public void reconciliationMismatchAbortsTheTransaction() throws Exception {
        TimsReportMapper mapper = mock(TimsReportMapper.class);
        when(mapper.insertStgIncome(anyList(), eq("202511"), eq("20260727")))
                .thenAnswer(invocation -> ((List<?>) invocation.getArgument(0)).size());
        when(mapper.countStgIncome("202511")).thenReturn(2L);

        new TimsAtomicLoadService(mapper, 2)
                .load(prepared(3), TimsBusinessType.INCOME, YearMonth.of(2025, 11), "20260727");
    }

    @Test
    public void completionRunsOnlyAfterRowCountReconciliation() throws Exception {
        TimsReportMapper mapper = mock(TimsReportMapper.class);
        when(mapper.insertStgIncome(anyList(), eq("202511"), eq("20260727"))).thenReturn(1);
        when(mapper.countStgIncome("202511")).thenReturn(1L);
        AtomicBoolean completed = new AtomicBoolean();

        new TimsAtomicLoadService(mapper, 2).load(prepared(1), TimsBusinessType.INCOME,
                YearMonth.of(2025, 11), "20260727", null, rows -> {
                    assertEquals(1L, rows);
                    completed.set(true);
                });

        assertTrue(completed.get());
    }

    private TimsPreparationResult prepared(int count) throws Exception {
        Path work = temporaryFolder.newFolder().toPath();
        TimsSpool spool;
        try (TimsSpool.Writer writer = TimsSpool.create(work)) {
            for (int i = 0; i < count; i++) writer.write(row(i));
            spool = writer.finish();
        }
        return new TimsPreparationResult(1, count, java.util.Collections.emptyList(),
                java.util.Collections.emptyMap(), spool);
    }

    private TimsReportRecord row(int index) {
        TimsReportRecord row = new TimsReportRecord();
        row.setDAcct(LocalDate.of(2025, 11, 1));
        row.setDAcctText("202511");
        row.setTreCode("220000000" + index);
        row.setTreasuryName("国库" + index);
        row.setTaxOrgCode("111");
        row.setLevel("1");
        row.setSubjectCode("101");
        row.setSubjectName("收入");
        row.setCurrentAmount(new BigDecimal("1.00"));
        row.setYearAmount(new BigDecimal("2.00"));
        return row;
    }
}
