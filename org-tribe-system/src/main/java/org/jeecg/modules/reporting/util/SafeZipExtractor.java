package org.jeecg.modules.reporting.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
    private static final List<Charset> ZIP_NAME_CHARSETS = Arrays.asList(
            StandardCharsets.UTF_8,
            Charset.forName("GBK"),
            Charset.forName("CP437"));

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
        IOException lastFailure = null;
        for (Charset charset : ZIP_NAME_CHARSETS) {
            clearDirectoryContents(safeRoot);
            try {
                return Collections.unmodifiableList(extractWithCharset(zipFile, safeRoot, charset));
            } catch (IOException exception) {
                lastFailure = exception;
                if (!looksLikeZipNameEncodingProblem(exception) || charset.equals(ZIP_NAME_CHARSETS.get(ZIP_NAME_CHARSETS.size() - 1))) {
                    break;
                }
            }
        }
        clearDirectoryContents(safeRoot);
        if (lastFailure != null && looksLikeZipNameEncodingProblem(lastFailure)) {
            throw new IOException("ZIP 文件名编码不兼容或压缩包已损坏", lastFailure);
        }
        throw lastFailure == null ? new IOException("ZIP 解压失败") : lastFailure;
    }

    private List<Path> extractWithCharset(Path zipFile, Path safeRoot, Charset charset) throws IOException {
        List<Path> extractedFiles = new ArrayList<>();
        long totalBytes = 0L;
        int entryCount = 0;

        try (InputStream input = Files.newInputStream(zipFile);
             ZipInputStream zipInput = new ZipInputStream(input, charset)) {
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
        } catch (IllegalArgumentException exception) {
            throw new IOException(exception.getMessage(), exception);
        }
        return extractedFiles;
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

    private boolean looksLikeZipNameEncodingProblem(Throwable error) {
        for (Throwable current = error; current != null; current = current.getCause()) {
            String message = current.getMessage();
            if (message != null) {
                String normalized = message.toLowerCase(Locale.ROOT);
                if (normalized.contains("malformed")
                        || normalized.contains("illegal byte sequence")
                        || normalized.contains("input length = 1")
                        || normalized.contains("unmappable character")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearDirectoryContents(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(root)) {
            paths.sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(root))
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException exception) {
                            throw new IllegalStateException(exception);
                        }
                    });
        } catch (IllegalStateException exception) {
            if (exception.getCause() instanceof IOException) {
                throw (IOException) exception.getCause();
            }
            throw exception;
        }
    }
}
