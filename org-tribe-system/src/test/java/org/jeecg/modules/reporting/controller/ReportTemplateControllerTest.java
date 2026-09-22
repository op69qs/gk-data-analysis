package org.jeecg.modules.reporting.controller;

import org.jeecg.modules.reporting.service.ReportTemplateService;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ReportTemplateControllerTest {

    @Test
    public void downloadFlashIncomeTemplateReturnsAttachment() throws Exception {
        MockMvc mvc = standaloneSetup(new ReportTemplateController(new ReportTemplateService())).build();
        mvc.perform(get("/reporting/templates/FLASH_INCOME"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("filename*=UTF-8''")));
    }
}
