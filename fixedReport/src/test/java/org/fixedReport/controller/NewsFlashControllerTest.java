package org.fixedReport.controller;

import com.alibaba.fastjson.JSONObject;
import org.fixedReport.service.NewsFlashService;
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

public class NewsFlashControllerTest {

    @Test
    public void monthlyReportUsesLiveTable3EvenWhenSnapshotExists() {
        NewsFlashController controller = new NewsFlashController();
        ReportService reportService = mock(ReportService.class);
        NewsFlashService newsFlashService = mock(NewsFlashService.class);
        ReflectionTestUtils.setField(controller, "reportService", reportService);
        ReflectionTestUtils.setField(controller, "newsFlashService", newsFlashService);

        Map<String, Object> storedReport = new HashMap<>();
        storedReport.put("textList", "{YEAR=2024,MONTH=12}");
        storedReport.put("tableParams", "{table1=2228.25}");
        storedReport.put("tableParams2", "{table421=2595.36}");
        storedReport.put("tableParams3",
                "[{PROJECT=北京, T010101_RATE=3.10}, {PROJECT=重庆, T010101_RATE=5.51}]");
        storedReport.put("AMT_UNIT_NAME", "亿元");
        when(reportService.getMonthlyReport(any())).thenReturn(storedReport);
        Map<String, Object> liveRow = new HashMap<>();
        liveRow.put("PROJECT", "实时重庆");
        when(newsFlashService.getTableParams3(any()))
                .thenReturn(Collections.singletonList(liveRow));

        JSONObject request = new JSONObject();
        request.put("REPORT_ID", "bd7240a3a6d643edb5263c171a95a398");
        request.put("ACCOUNT_PERIOD", "2024-12");

        Map<String, Object> response = controller.getMonthlyReport(request);
        Map<String, Object> rows = (Map<String, Object>) response.get("rows");
        List<Map<String, Object>> table3 = (List<Map<String, Object>>) rows.get("tableParams3");

        assertEquals("success", response.get("result"));
        assertEquals(1, table3.size());
        assertEquals("实时重庆", table3.get(0).get("PROJECT"));
        verify(newsFlashService).getTableParams3(any());
    }

    @Test
    public void monthlyReportFallsBackToLiveTable3WhenSnapshotIsEmpty() {
        NewsFlashController controller = new NewsFlashController();
        ReportService reportService = mock(ReportService.class);
        NewsFlashService newsFlashService = mock(NewsFlashService.class);
        ReflectionTestUtils.setField(controller, "reportService", reportService);
        ReflectionTestUtils.setField(controller, "newsFlashService", newsFlashService);

        Map<String, Object> storedReport = new HashMap<>();
        storedReport.put("textList", "{YEAR=2024,MONTH=12}");
        storedReport.put("tableParams", "{table1=2228.25}");
        storedReport.put("tableParams2", "{table421=2595.36}");
        storedReport.put("tableParams3", "");
        storedReport.put("AMT_UNIT_NAME", "亿元");
        when(reportService.getMonthlyReport(any())).thenReturn(storedReport);

        Map<String, Object> liveRow = new HashMap<>();
        liveRow.put("PROJECT", "重庆");
        when(newsFlashService.getTableParams3(any()))
                .thenReturn(Collections.singletonList(liveRow));

        Map<String, Object> response = controller.getMonthlyReport(new JSONObject());
        Map<String, Object> rows = (Map<String, Object>) response.get("rows");
        List<Map<String, Object>> table3 = (List<Map<String, Object>>) rows.get("tableParams3");

        assertEquals(1, table3.size());
        assertEquals("重庆", table3.get(0).get("PROJECT"));
        verify(newsFlashService).getTableParams3(any());
    }
}
