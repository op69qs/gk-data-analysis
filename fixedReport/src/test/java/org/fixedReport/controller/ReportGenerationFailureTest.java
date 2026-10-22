package org.fixedReport.controller;

import com.alibaba.fastjson.JSONObject;
import org.fixedReport.service.NewsFlashQuarterService;
import org.fixedReport.service.QuarterReportService;
import org.fixedReport.service.ReportService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReportGenerationFailureTest {

    @Test
    public void allRemainingTemplatesMarkSqlFailuresInsteadOfLeavingGenerating() {
        for (String type : new String[]{"1", "3", "4"}) {
            Harness harness = new Harness(type, true, false);
            Map<String, Object> result = harness.generate.apply(harness.request);
            assertEquals(type, "fail", result.get("result"));
            assertEquals("source query failed", result.get("rows"));
            ArgumentCaptor<Map> update = ArgumentCaptor.forClass(Map.class);
            verify(harness.storage).updateEntityReport(update.capture());
            assertEquals(type, "2", update.getValue().get("STATUS"));
            assertEquals("report-" + type, update.getValue().get("REPORT_ID"));
            verify(harness.storage, never()).insertFact(any());
        }
    }

    @Test
    public void missingTextDoesNotCreateAnEmptySuccessfulReport() {
        for (String type : new String[]{"1", "3", "4"}) {
            Harness harness = new Harness(type, false, true);
            Map<String, Object> result = harness.generate.apply(harness.request);
            assertEquals(type, "fail", result.get("result"));
            ArgumentCaptor<Map> update = ArgumentCaptor.forClass(Map.class);
            verify(harness.storage).updateEntityReport(update.capture());
            assertEquals("2", update.getValue().get("STATUS"));
            verify(harness.storage, never()).insertFact(any());
        }
    }

    @Test
    public void availableTextStillCompletesEveryRemainingTemplate() {
        for (String type : new String[]{"1", "3", "4"}) {
            Harness harness = new Harness(type, false, false);
            Map<String, Object> result = harness.generate.apply(harness.request);
            assertEquals(type, "success", result.get("result"));
            verify(harness.storage).insertFact(any());
        }
    }

    private static class Harness {
        final ReportService storage = mock(ReportService.class);
        final JSONObject request = new JSONObject();
        final Function<JSONObject, Map<String, Object>> generate;

        Harness(String type, boolean fail, boolean empty) {
            request.put("REPORT_ID", "report-" + type);
            request.put("ACCOUNT_PERIOD", "1".equals(type) ? "2026-06" : "2026Q2");
            request.put("REPORT_TYPE_ID", type);
            if ("1".equals(type)) {
                ReportController controller = new ReportController();
                ReflectionTestUtils.setField(controller, "reportService", storage);
                if (fail) when(storage.getTextAndParams(any())).thenThrow(new IllegalStateException("source query failed"));
                else if (!empty) when(storage.getTextAndParams(any())).thenReturn(Collections.singletonList(Collections.emptyMap()));
                generate = controller::addMonthlyReport;
            } else if ("3".equals(type)) {
                QuarterReportController controller = new QuarterReportController();
                QuarterReportService source = mock(QuarterReportService.class);
                ReflectionTestUtils.setField(controller, "reportService", storage);
                ReflectionTestUtils.setField(controller, "quarterReportService", source);
                if (fail) when(source.getTextAndParams(any())).thenThrow(new IllegalStateException("source query failed"));
                else if (!empty) when(source.getTextAndParams(any())).thenReturn(Collections.singletonList(Collections.emptyMap()));
                generate = controller::addQuarterReport;
            } else {
                NewsFlashQuarterController controller = new NewsFlashQuarterController();
                NewsFlashQuarterService source = mock(NewsFlashQuarterService.class);
                ReflectionTestUtils.setField(controller, "reportService", storage);
                ReflectionTestUtils.setField(controller, "newsFlashQuarterService", source);
                if (fail) when(source.getTextAndParams(any())).thenThrow(new IllegalStateException("source query failed"));
                else if (!empty) when(source.getTextAndParams(any())).thenReturn(Collections.singletonList(Collections.emptyMap()));
                generate = controller::addNewsFlashQuarterReport;
            }
        }
    }
}
