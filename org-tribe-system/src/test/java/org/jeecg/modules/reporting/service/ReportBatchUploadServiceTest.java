package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportBatch;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.entity.ReportTask;
import org.jeecg.modules.reporting.entity.ReportTaskLog;
import org.jeecg.modules.reporting.mapper.ReportBatchMapper;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskLogMapper;
import org.jeecg.modules.reporting.mapper.ReportTaskMapper;
import org.jeecg.modules.reporting.vo.ReportBatchUploadResult;
import org.jeecg.modules.reporting.vo.ReportUploadCommand;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReportBatchUploadServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void createsTrackedBatchFilesAndQueuedParseTask() throws Exception {
        ReportBatchMapper batchMapper = mock(ReportBatchMapper.class);
        ReportFileMapper fileMapper = mock(ReportFileMapper.class);
        ReportTaskMapper taskMapper = mock(ReportTaskMapper.class);
        ReportTaskLogMapper taskLogMapper = mock(ReportTaskLogMapper.class);
        when(batchMapper.insert(any(ReportBatch.class))).thenReturn(1);
        when(batchMapper.updateById(any(ReportBatch.class))).thenReturn(1);
        when(fileMapper.insert(any(ReportFile.class))).thenReturn(1);
        when(taskMapper.insert(any(ReportTask.class))).thenReturn(1);

        ReportingProperties properties = new ReportingProperties();
        properties.setArchiveRoot(temporaryFolder.getRoot().toPath().resolve("reporting").toString());
        properties.setMaxUploadBytes(1024 * 1024);
        properties.setMaxZipEntries(100);
        properties.setMaxTotalUncompressedBytes(1024 * 1024);
        properties.setMaxSingleEntryBytes(512 * 1024);
        ReportArchiveService archiveService = new ReportArchiveService(properties);
        ReportBatchService service = new ReportBatchService(
                batchMapper, fileMapper, taskMapper, taskLogMapper, archiveService);

        ReportUploadCommand command = new ReportUploadCommand();
        command.setSourceDomain("TIMS");
        command.setBusinessType("INCOME");
        command.setAccountingPeriod("2026-07");
        command.setTreasuryCode("5000000000");
        MockMultipartFile file = new MockMultipartFile(
                "file", "收入.zip", "application/zip", zipBytes("收入/收入1.xls", "row"));

        ReportBatchUploadResult result = service.createUploadBatch(file, command, "u-1", "tester");

        assertNotNull(result.getBatchId());
        assertEquals("PROCESSING", result.getStatus());
        assertEquals("PARSE", result.getCurrentStage());
        verify(fileMapper, times(2)).insert(any(ReportFile.class));
        verify(taskMapper, times(3)).insert(any(ReportTask.class));

        ArgumentCaptor<ReportBatch> batchCaptor = ArgumentCaptor.forClass(ReportBatch.class);
        verify(batchMapper).insert(batchCaptor.capture());
        ReportBatch batch = batchCaptor.getValue();
        LocalDate period = batch.getAccountingPeriod().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(LocalDate.of(2026, 7, 31), period);
        assertEquals("tester", batch.getCreateBy());

        ArgumentCaptor<ReportTask> taskCaptor = ArgumentCaptor.forClass(ReportTask.class);
        verify(taskMapper, times(3)).insert(taskCaptor.capture());
        assertEquals("PARSE", taskCaptor.getAllValues().get(2).getTaskType());
        assertEquals("QUEUED", taskCaptor.getAllValues().get(2).getStatus());

        ArgumentCaptor<ReportTaskLog> logCaptor = ArgumentCaptor.forClass(ReportTaskLog.class);
        verify(taskLogMapper, times(3)).insert(logCaptor.capture());
        assertEquals("u-1", logCaptor.getAllValues().get(2).getOperatorId());
        assertEquals("tester", logCaptor.getAllValues().get(2).getOperatorName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void keyMetadataCannotBypassLegacyFileNameContract() throws Exception {
        ReportingProperties properties = new ReportingProperties();
        properties.setArchiveRoot(temporaryFolder.getRoot().toPath().resolve("reporting-key").toString());
        ReportBatchService service = new ReportBatchService(
                mock(ReportBatchMapper.class), mock(ReportFileMapper.class), mock(ReportTaskMapper.class),
                mock(ReportTaskLogMapper.class), new ReportArchiveService(properties));
        ReportUploadCommand command = new ReportUploadCommand();
        command.setSourceDomain("KEY");
        command.setBusinessType("ALL");
        command.setAccountingPeriod("2026-07");
        command.setTreasuryCode("2200000000");

        service.createUploadBatch(new MockMultipartFile(
                "file", "arbitrary.zip", "application/zip", zipBytes("data.txt", "row")),
                command, "u-1", "tester");
    }

    private byte[] zipBytes(String name, String value) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (ZipOutputStream zip = new ZipOutputStream(output, StandardCharsets.UTF_8)) {
            zip.putNextEntry(new ZipEntry(name));
            zip.write(value.getBytes(StandardCharsets.UTF_8));
            zip.closeEntry();
        }
        return output.toByteArray();
    }
}
