package org.jeecg.modules.reporting.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts an archive into one batch directory while preventing ZIP slip and
 * bounded-resource attacks. macOS metadata is ignored before any path is made.
 */
public class SafeZipExtractor {

    private static final int BUFFER_SIZE = 8192;

    private final int maxEntries;
    private final long maxTotalUncompressedBytes;
    private final long maxSingleEntryBytes;

    public SafeZipExtractor(int maxEntries, long maxTotalUncompressedBytes, long maxSingleEntryBytes) {
        if (maxEntries <= 0 || maxTotalUncompressedBytes <= 0 || maxSingleEntryBytes <= 0) {
            throw new IllegalArgumentException("ZIP limits must be positive");
        }
        if (maxSingleEntryBytes > maxTotalUncompressedBytes) {
            throw new IllegalArgumentException("Single ZIP entry limit cannot exceed total limit");
        }
        this.maxEntries = maxEntries;
        this.maxTotalUncompressedBytes = maxTotalUncompressedBytes;
        this.maxSingleEntryBytes = maxSingleEntryBytes;
    }

    public List<Path> extract(Path zipFile, Path destination) throws IOException {
        if (zipFile == null || !Files.isRegularFile(zipFile)) {
            throw new IOException("ZIP file does not exist: " + zipFile);
        }
        if (destination == null) {
            throw new IOException("ZIP destination is required");
        }

        Path safeRoot = destination.toAbsolutePath().normalize();
        Files.createDirectories(safeRoot);
        List<Path> extractedFiles = new ArrayList<>();
        long totalBytes = 0L;
        int entryCount = 0;

        try (InputStream input = Files.newInputStream(zipFile);
             ZipInputStream zipInput = new ZipInputStream(input, StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zipInput.getNextEntry()) != null) {
                entryCount++;
                if (entryCount > maxEntries) {
                    throw new IOException("ZIP contains more than " + maxEntries + " entries");
                }

                String normalizedName = normalizeEntryName(entry.getName());
                if (isIgnoredEntry(normalizedName)) {
                    totalBytes = drainIgnoredEntry(zipInput, normalizedName, totalBytes);
                    zipInput.closeEntry();
                    continue;
                }

                Path target = resolveSafeTarget(safeRoot, normalizedName);
                if (entry.isDirectory()) {
                    Files.createDirectories(target);
                    zipInput.closeEntry();
                    continue;
                }

                if (target.getParent() != null) {
                    Files.createDirectories(target.getParent());
                }
                long entryBytes = 0L;
                try (OutputStream output = Files.newOutputStream(target,
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read;
                    while ((read = zipInput.read(buffer)) != -1) {
                        entryBytes += read;
                        totalBytes += read;
                        if (entryBytes > maxSingleEntryBytes) {
                            throw new IOException("ZIP entry exceeds size limit: " + normalizedName);
                        }
                        if (totalBytes > maxTotalUncompressedBytes) {
                            throw new IOException("ZIP exceeds total uncompressed size limit");
                        }
                        output.write(buffer, 0, read);
                    }
                } catch (FileAlreadyExistsException exception) {
                    throw new IOException("ZIP contains a duplicate output path: " + normalizedName, exception);
                } catch (IOException exception) {
                    Files.deleteIfExists(target);
                    throw exception;
                }
                extractedFiles.add(target);
                zipInput.closeEntry();
            }
        }

        return Collections.unmodifiableList(extractedFiles);
    }

    private long drainIgnoredEntry(ZipInputStream zipInput, String entryName, long totalBytes)
            throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long entryBytes = 0L;
        int read;
        while ((read = zipInput.read(buffer)) != -1) {
            entryBytes += read;
            totalBytes += read;
            if (entryBytes > maxSingleEntryBytes) {
                throw new IOException("ZIP entry exceeds size limit: " + entryName);
            }
            if (totalBytes > maxTotalUncompressedBytes) {
                throw new IOException("ZIP exceeds total uncompressed size limit");
            }
        }
        return totalBytes;
    }

    public List<Path> findBusinessFiles(Path root, String... extensions) throws IOException {
        if (root == null || !Files.isDirectory(root)) {
            return Collections.emptyList();
        }
        Set<String> accepted = normalizeExtensions(extensions);
        List<Path> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(root)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> !isIgnoredEntry(root.relativize(path).toString()))
                    .filter(path -> accepted.contains(extensionOf(path.getFileName().toString())))
                    .forEach(result::add);
        }
        result.sort(Comparator.comparing(path -> root.relativize(path).toString()));
        return result;
    }

    private Path resolveSafeTarget(Path safeRoot, String entryName) throws IOException {
        if (entryName.startsWith("/") || entryName.startsWith("\\")
                || entryName.matches("^[A-Za-z]:.*")) {
            throw new IOException("ZIP contains an absolute path: " + entryName);
        }
        Path target = safeRoot.resolve(entryName).normalize();
        if (!target.startsWith(safeRoot) || target.equals(safeRoot)) {
            throw new IOException("ZIP entry escapes the batch directory: " + entryName);
        }
        return target;
    }

    private String normalizeEntryName(String name) throws IOException {
        if (name == null || name.trim().isEmpty() || name.indexOf('\0') >= 0) {
            throw new IOException("ZIP contains an invalid entry name");
        }
        return name.replace('\\', '/');
    }

    private boolean isIgnoredEntry(String entryName) {
        String normalized = entryName.replace('\\', '/');
        List<String> parts = Arrays.asList(normalized.split("/"));
        if (parts.contains("__MACOSX")) {
            return true;
        }
        String fileName = parts.isEmpty() ? normalized : parts.get(parts.size() - 1);
        return ".DS_Store".equals(fileName) || fileName.startsWith("._");
    }

    private Set<String> normalizeExtensions(String... extensions) {
        Set<String> result = new HashSet<>();
        if (extensions == null) {
            return result;
        }
        for (String extension : extensions) {
            if (extension != null && !extension.trim().isEmpty()) {
                String normalized = extension.trim().toLowerCase(Locale.ROOT);
                result.add(normalized.startsWith(".") ? normalized.substring(1) : normalized);
            }
        }
        return result;
    }

    private String extensionOf(String fileName) {
        int separator = fileName.lastIndexOf('.');
        if (separator < 0 || separator == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(separator + 1).toLowerCase(Locale.ROOT);
    }
}
