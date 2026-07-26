package org.fixedReport.controller;

import com.alibaba.fastjson.JSONObject;
import org.fixedReport.service.NewsFlashQuarterService;
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

public class NewsFlashQuarterControllerTest {

    @Test
    public void quarterNewsFlashUsesLiveTable3EvenWhenSnapshotExists() {
        NewsFlashQuarterController controller = new NewsFlashQuarterController();
        ReportService reportService = mock(ReportService.class);
        NewsFlashQuarterService newsFlashQuarterService = mock(NewsFlashQuarterService.class);
        ReflectionTestUtils.setField(controller, "reportService", reportService);
        ReflectionTestUtils.setField(controller, "newsFlashQuarterService", newsFlashQuarterService);

        Map<String, Object> storedReport = new HashMap<>();
        storedReport.put("textList", "{YEAR=2022,QUARTER=4}");
        storedReport.put("tableParams", "{table1=1}");
        storedReport.put("tableParams2", "{table2=2}");
        storedReport.put("tableParams3",
                "[{bPROJECT=北京, bT010101_RATE=-3.67}, {bPROJECT=重庆, bT010101_RATE=5.51}]");
        storedReport.put("echartsData", "[]");
        storedReport.put("AMT_UNIT_NAME", "亿元");
        when(reportService.getMonthlyReport(any())).thenReturn(storedReport);
        when(newsFlashQuarterService.getTableParams(any())).thenReturn(Collections.emptyList());
        Map<String, Object> liveRow = new HashMap<>();
        liveRow.put("bPROJECT", "实时北京");
        when(newsFlashQuarterService.getTableParams3(any()))
                .thenReturn(Collections.singletonList(liveRow));

        Map<String, Object> response = controller.getMonthlyReport(new JSONObject());
        Map<String, Object> rows = (Map<String, Object>) response.get("rows");
        List<Map<String, Object>> table3 = (List<Map<String, Object>>) rows.get("tableParams3");

        assertEquals("success", response.get("result"));
        assertEquals(1, table3.size());
        assertEquals("实时北京", table3.get(0).get("bPROJECT"));
        verify(newsFlashQuarterService).getTableParams3(any());
    }
}
