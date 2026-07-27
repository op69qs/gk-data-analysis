package org.jeecg.modules.reporting.controller;

import org.jeecg.modules.reporting.service.ReportBatchService;
import org.jeecg.modules.reporting.vo.ReportBatchUploadResult;
import org.jeecg.modules.reporting.vo.ReportUploadCommand;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ReportBatchControllerTest {

    @Test
    public void uploadReturnsBatchIdAndInitialTrackedState() throws Exception {
        ReportBatchService service = mock(ReportBatchService.class);
        when(service.createUploadBatch(any(), any(ReportUploadCommand.class), anyString(), anyString()))
                .thenReturn(new ReportBatchUploadResult(
                        "batch-1", "RPT-1", "PROCESSING", "PARSE", 30));
        MockMvc mvc = standaloneSetup(new ReportBatchController(service)).build();
        MockMultipartFile file = new MockMultipartFile(
                "file", "收入.zip", "application/zip", new byte[]{'P', 'K', 3, 4});

        mvc.perform(multipart("/reporting/batches/upload")
                        .file(file)
                        .param("sourceDomain", "TIMS")
                        .param("businessType", "INCOME")
                        .param("accountingPeriod", "2026-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.batchId").value("batch-1"))
                .andExpect(jsonPath("$.result.status").value("PROCESSING"))
                .andExpect(jsonPath("$.result.currentStage").value("PARSE"));
    }
}
