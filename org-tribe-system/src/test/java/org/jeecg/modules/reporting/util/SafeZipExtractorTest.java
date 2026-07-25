package org.jeecg.modules.reporting.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class SafeZipExtractorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void recursivelyFindsExcelAndIgnoresMacMetadata() throws Exception {
        Path zip = temporaryFolder.newFile("income.zip").toPath();
        createZip(zip, Arrays.asList(
                entry("__MACOSX/._收入", "metadata"),
                entry("收入/.DS_Store", "metadata"),
                entry("收入/._收入1.xls", "metadata"),
                entry("收入/收入1.xls", "first"),
                entry("收入/二级目录/收入2.xlsx", "second")
        ));
        Path destination = temporaryFolder.newFolder("output").toPath();

        SafeZipExtractor extractor = new SafeZipExtractor(100, 1024 * 1024, 512 * 1024);
        extractor.extract(zip, destination);
        List<Path> excelFiles = extractor.findBusinessFiles(destination, "xls", "xlsx");

        assertEquals(Arrays.asList("收入/二级目录/收入2.xlsx", "收入/收入1.xls"),
                relativeNames(destination, excelFiles));
        assertFalse(Files.exists(destination.resolve("__MACOSX")));
        assertFalse(Files.exists(destination.resolve("收入/.DS_Store")));
        assertFalse(Files.exists(destination.resolve("收入/._收入1.xls")));
    }

    @Test
    public void alsoFindsExcelDirectlyAtZipRoot() throws Exception {
        Path zip = temporaryFolder.newFile("direct.zip").toPath();
        createZip(zip, Collections.singletonList(entry("收入1.xls", "first")));
        Path destination = temporaryFolder.newFolder("direct-output").toPath();

        SafeZipExtractor extractor = new SafeZipExtractor(100, 1024 * 1024, 512 * 1024);
        extractor.extract(zip, destination);

        assertEquals(Collections.singletonList("收入1.xls"),
                relativeNames(destination, extractor.findBusinessFiles(destination, "xls", "xlsx")));
    }

    @Test
    public void rejectsPathTraversalEntry() throws Exception {
        Path zip = temporaryFolder.newFile("traversal.zip").toPath();
        createZip(zip, Collections.singletonList(entry("../escaped.xls", "unsafe")));
        Path destination = temporaryFolder.newFolder("safe-output").toPath();

        SafeZipExtractor extractor = new SafeZipExtractor(100, 1024 * 1024, 512 * 1024);
        try {
            extractor.extract(zip, destination);
            fail("Expected unsafe ZIP entry to be rejected");
        } catch (IOException expected) {
            // Expected: the archive must not write outside its batch directory.
        }

        assertFalse(Files.exists(destination.getParent().resolve("escaped.xls")));
    }

    @Test
    public void ignoredMetadataStillCountsTowardExtractionLimits() throws Exception {
        Path zip = temporaryFolder.newFile("metadata-bomb.zip").toPath();
        createZip(zip, Collections.singletonList(entry(
                "__MACOSX/._large", "01234567890123456789")));
        Path destination = temporaryFolder.newFolder("metadata-output").toPath();

        SafeZipExtractor extractor = new SafeZipExtractor(100, 10, 10);
        try {
            extractor.extract(zip, destination);
            fail("Expected oversized ignored metadata to be rejected");
        } catch (IOException expected) {
            // Ignoring a metadata file must not bypass ZIP bomb limits.
        }
    }

    private List<String> relativeNames(Path root, List<Path> paths) {
        java.util.ArrayList<String> names = new java.util.ArrayList<>();
        for (Path path : paths) {
            names.add(root.relativize(path).toString().replace('\\', '/'));
        }
        Collections.sort(names);
        return names;
    }

    private ZipContent entry(String name, String content) {
        return new ZipContent(name, content.getBytes(StandardCharsets.UTF_8));
    }

    private void createZip(Path zip, List<ZipContent> entries) throws IOException {
        try (OutputStream output = Files.newOutputStream(zip);
             ZipOutputStream zipOutput = new ZipOutputStream(output, StandardCharsets.UTF_8)) {
            for (ZipContent content : entries) {
                zipOutput.putNextEntry(new ZipEntry(content.name));
                zipOutput.write(content.value);
                zipOutput.closeEntry();
            }
        }
    }

    private static final class ZipContent {
        private final String name;
        private final byte[] value;

        private ZipContent(String name, byte[] value) {
            this.name = name;
            this.value = value;
        }
    }
}
