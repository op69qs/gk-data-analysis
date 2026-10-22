package org.fixedReport.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.fixedReport.service.ReportService;

import java.util.HashMap;
import java.util.Map;

/** Records a failed generation without replacing its original error. */
@Slf4j
public final class ReportGenerationStatus {
    private ReportGenerationStatus() { }

    public static void markFailed(ReportService service, JSONObject params) {
        if (params == null || StringUtils.isBlank(params.getString("REPORT_ID"))) {
            return;
        }
        Map<String, Object> failed = new HashMap<>();
        failed.put("REPORT_ID", params.getString("REPORT_ID"));
        failed.put("STATUS", "2");
        try {
            service.updateEntityReport(failed);
        } catch (Exception statusFailure) {
            log.error("报告失败状态保存失败，REPORT_ID={}", params.getString("REPORT_ID"), statusFailure);
        }
    }
}
