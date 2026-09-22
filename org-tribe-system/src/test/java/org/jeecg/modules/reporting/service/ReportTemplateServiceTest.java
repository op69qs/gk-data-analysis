package org.jeecg.modules.reporting.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReportTemplateServiceTest {

    private final ReportTemplateService service = new ReportTemplateService();

    @Test
    public void flashIncomeTemplateMatchesLegacyDisName() {
        ReportTemplateService.Template template = service.download("FLASH_INCOME");
        assertTrue(template.getResource().exists());
        assertEquals("快报_收入数据.xls", template.getFileName());
        assertEquals("application/vnd.ms-excel", template.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedBusinessTypeIsRejected() {
        service.download("INCOME");
    }
}
