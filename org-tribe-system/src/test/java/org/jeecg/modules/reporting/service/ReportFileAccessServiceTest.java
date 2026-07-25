package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportFileAccessServiceTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void retainedFileInsideArchiveRootCanBeDownloaded() throws Exception {
        Path root = folder.newFolder("reporting").toPath();
        Path stored = root.resolve("key/2026-07/batch/archive/source.zip");
        Files.createDirectories(stored.getParent());
        Files.write(stored, "zip".getBytes(StandardCharsets.UTF_8));
        ReportFile file = file(stored);
        ReportFileMapper mapper = mock(ReportFileMapper.class);
        when(mapper.selectById("file-1")).thenReturn(file);
        ReportingProperties properties = new ReportingProperties();
        properties.setArchiveRoot(root.toString());

        ReportFileAccessService.Download download = new ReportFileAccessService(mapper, properties)
                .download("file-1");

        assertEquals(stored.toRealPath(), download.getPath());
        assertEquals("收入.zip", download.getFileName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void storedPathOutsideDedicatedArchiveRootIsRejected() throws Exception {
        Path root = folder.newFolder("reporting").toPath();
        Path outside = folder.newFile("outside.zip").toPath();
        ReportFileMapper mapper = mock(ReportFileMapper.class);
        when(mapper.selectById("file-1")).thenReturn(file(outside));
        ReportingProperties properties = new ReportingProperties();
        properties.setArchiveRoot(root.toString());

        new ReportFileAccessService(mapper, properties).download("file-1");
    }

    private ReportFile file(Path path) {
        ReportFile file = new ReportFile();
        file.setId("file-1");
        file.setOriginalName("收入.zip");
        file.setStoragePath(path.toString());
        file.setRetained(1);
        file.setDelFlag(0);
        return file;
    }
}
