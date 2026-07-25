package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.exception.ReportFileHandlingException;
import org.jeecg.modules.reporting.service.ReportArchiveService.ArchiveResult;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReportArchiveServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void archivesWithServerNameAndExtractsNestedExcel() throws Exception {
        ReportingProperties properties = properties();
        ReportArchiveService service = new ReportArchiveService(properties);
        MockMultipartFile file = new MockMultipartFile(
                "file", "收入.zip", "application/zip", zipBytes("收入/收入1.xls", "row"));

        ArchiveResult result = service.archiveAndExtract(file, "TIMS", "2026-07", "batch-1");

        assertEquals("收入.zip", result.getOriginalFileName());
        assertEquals("source.zip", result.getArchivePath().getFileName().toString());
        assertTrue(Files.isRegularFile(result.getArchivePath()));
        assertEquals(1, result.getExtractedFiles().size());
        assertEquals("收入/收入1.xls", result.getExtractRoot()
                .relativize(result.getExtractedFiles().get(0)).toString().replace('\\', '/'));
        assertEquals(64, result.getSha256().length());
    }

    @Test
    public void rejectsNonZipUpload() throws Exception {
        ReportingProperties properties = properties();
        ReportArchiveService service = new ReportArchiveService(properties);
        MockMultipartFile file = new MockMultipartFile(
                "file", "收入.xls", "application/vnd.ms-excel", "not a zip".getBytes(StandardCharsets.UTF_8));

        try {
            service.archiveAndExtract(file, "TIMS", "2026-07", "batch-2");
            fail("Expected non-ZIP upload to be rejected");
        } catch (IllegalArgumentException expected) {
            assertTrue(expected.getMessage().contains("ZIP"));
        }
    }

    @Test
    public void identifiesUnsafeArchiveContentAsExtractFailure() throws Exception {
        ReportingProperties properties = properties();
        ReportArchiveService service = new ReportArchiveService(properties);
        MockMultipartFile file = new MockMultipartFile(
                "file", "收入.zip", "application/zip", zipBytes("../escaped.xls", "unsafe"));

        try {
            service.archiveAndExtract(file, "TIMS", "2026-07", "batch-3");
            fail("Expected unsafe archive content to be rejected");
        } catch (ReportFileHandlingException expected) {
            assertEquals("EXTRACT", expected.getStage());
            assertTrue(Files.isRegularFile(expected.getArchivePath()));
        }
    }

    private ReportingProperties properties() {
        ReportingProperties properties = new ReportingProperties();
        properties.setArchiveRoot(temporaryFolder.getRoot().toPath().resolve("reporting").toString());
        properties.setMaxUploadBytes(1024 * 1024);
        properties.setMaxZipEntries(100);
        properties.setMaxTotalUncompressedBytes(1024 * 1024);
        properties.setMaxSingleEntryBytes(512 * 1024);
        return properties;
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
