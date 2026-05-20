package org.jeecg.modules.audit.component;

import org.jeecg.modules.audit.service.IDataAnalysisBizAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataAnalysisBizAuditRetryTask {

    @Autowired
    private IDataAnalysisBizAuditService auditService;

    @Value("${gk-data-analysis.biz-audit.retry-enabled:true}")
    private boolean retryEnabled;

    @Scheduled(fixedDelayString = "${gk-data-analysis.biz-audit.retry-delay-ms:300000}")
    public void retryFailedAudits() {
        if (!retryEnabled) {
            return;
        }
        auditService.retryFailedAudits();
    }
}
