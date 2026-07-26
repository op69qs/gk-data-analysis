package org.fixedReport.controller;

import com.alibaba.fastjson.JSONObject;
import org.fixedReport.service.QuarterReportService;
import org.fixedReport.service.ReportService;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuarterReportControllerTest {

    @Test
    public void quarterReportUsesLiveTable3EvenWhenSnapshotExists() {
        QuarterReportController controller = new QuarterReportController();
        ReportService reportService = mock(ReportService.class);
        QuarterReportService quarterReportService = mock(QuarterReportService.class);
        ReflectionTestUtils.setField(controller, "reportService", reportService);
        ReflectionTestUtils.setField(controller, "quarterReportService", quarterReportService);

        Map<String, Object> storedReport = storedQuarterReport(
                "[{AREA_DSCR=重庆市, INDEX_VALUE=509.97}, {AREA_DSCR=万州区, INDEX_VALUE=21.06}]");
        when(reportService.getMonthlyReport(any())).thenReturn(storedReport);
        Map<String, Object> liveRow = new HashMap<>();
        liveRow.put("AREA_DSCR", "实时重庆市");
        when(quarterReportService.getTableParams3(any()))
                .thenReturn(Collections.singletonList(liveRow));

        Map<String, Object> response = controller.getMonthlyReport(new JSONObject());
        Map<String, Object> rows = (Map<String, Object>) response.get("rows");
        List<Map<String, Object>> table3 = (List<Map<String, Object>>) rows.get("tableParams3");

        assertEquals("success", response.get("result"));
        assertEquals(1, table3.size());
        assertEquals("实时重庆市", table3.get(0).get("AREA_DSCR"));
        verify(quarterReportService).getTableParams3(any());
    }

    private static Map<String, Object> storedQuarterReport(String table3) {
        Map<String, Object> storedReport = new HashMap<>();
        storedReport.put("textList", "{YEAR=2021,MONTH=4}");
        storedReport.put("tableParams", "{table1=1}");
        storedReport.put("tableParams2", "{table2=2}");
        storedReport.put("tableParams3", table3);
        storedReport.put("echartsData", "[]");
        storedReport.put("AMT_UNIT_NAME", "亿元");
        return storedReport;
    }
}
