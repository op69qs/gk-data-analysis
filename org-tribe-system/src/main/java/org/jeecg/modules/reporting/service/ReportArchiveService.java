package org.jeecg.modules.reporting.service;

import lombok.Getter;
import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.exception.ReportFileHandlingException;
import org.jeecg.modules.reporting.util.SafeZipExtractor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
public class ReportArchiveService {

    private static final int BUFFER_SIZE = 8192;

    private final ReportingProperties properties;
    private final SafeZipExtractor extractor;

    public ReportArchiveService(ReportingProperties properties) {
        this.properties = properties;
        this.extractor = new SafeZipExtractor(
                properties.getMaxZipEntries(),
                properties.getMaxTotalUncompressedBytes(),
                properties.getMaxSingleEntryBytes());
    }

    /** Backward-compatible ZIP-only entry used by older callers/tests. */
    public ArchiveResult archiveAndExtract(MultipartFile file,
                                           String sourceDomain,
                                           String accountingPeriod,
                                           String batchId) throws IOException {
        return archiveAndExtract(file, sourceDomain, accountingPeriod, batchId, null);
    }

    /**
     * Archives the upload and produces an extract tree for PARSE.
     * FLASH_INCOME additionally accepts a bare .xls / .xlsx (DIS-compatible);
     * other business types still require ZIP.
     */
    public ArchiveResult archiveAndExtract(MultipartFile file,
                                           String sourceDomain,
                                           String accountingPeriod,
                                           String batchId,
                                           String businessType) throws IOException {
        String originalFileName = validateUpload(file, businessType);
        String lowerName = originalFileName.toLowerCase(Locale.ROOT);
        boolean flashExcel = isFlashIncome(businessType)
                && (lowerName.endsWith(".xls") || lowerName.endsWith(".xlsx"));

        String safeSourceDomain = safeSegment(sourceDomain, "sourceDomain").toLowerCase(Locale.ROOT);
        String safePeriod = safeSegment(accountingPeriod, "accountingPeriod");
        String safeBatchId = safeSegment(batchId, "batchId");

        Path archiveRoot = Paths.get(properties.getArchiveRoot()).toAbsolutePath().normalize();
        Path batchRoot = archiveRoot.resolve(safeSourceDomain).resolve(safePeriod).resolve(safeBatchId).normalize();
        if (!batchRoot.startsWith(archiveRoot)) {
            throw new IOException("Reporting archive path escapes configured root");
        }

        Path archiveDirectory = batchRoot.resolve("archive");
        Path extractRoot = batchRoot.resolve("extracted");
        Files.createDirectories(archiveDirectory);
        Files.createDirectories(extractRoot);

        if (flashExcel) {
            return archiveSingleExcel(file, originalFileName, archiveDirectory, extractRoot);
        }
        return archiveZip(file, originalFileName, archiveDirectory, extractRoot);
    }

    private ArchiveResult archiveSingleExcel(MultipartFile file,
                                             String originalFileName,
                                             Path archiveDirectory,
                                             Path extractRoot) throws IOException {
        String extension = extensionOf(originalFileName);
        Path archivePath = archiveDirectory.resolve("source." + extension);
        Path temporaryPath = archiveDirectory.resolve("source." + extension + ".part");
        ArchiveCopy archiveCopy = copyAndDigest(file, temporaryPath);
        try {
            moveAtomically(temporaryPath, archivePath);
        } catch (RuntimeException | IOException exception) {
            Files.deleteIfExists(temporaryPath);
            throw exception;
        }

        String safeExtractName = sanitizeFileName(originalFileName);
        if (!safeExtractName.toLowerCase(Locale.ROOT).endsWith("." + extension)) {
            safeExtractName = "flash_income." + extension;
        }
        Path extractedFile = extractRoot.resolve(safeExtractName).normalize();
        if (!extractedFile.startsWith(extractRoot)) {
            throw new IOException("Extracted excel path escapes batch directory");
        }
        Files.copy(archivePath, extractedFile, StandardCopyOption.REPLACE_EXISTING);
        return new ArchiveResult(originalFileName, archivePath, extractRoot,
                archiveCopy.fileSize, archiveCopy.sha256,
                Collections.singletonList(extractedFile),
                contentTypeForExtension(extension), extension);
    }

    private ArchiveResult archiveZip(MultipartFile file,
                                     String originalFileName,
                                     Path archiveDirectory,
                                     Path extractRoot) throws IOException {
        Path archivePath = archiveDirectory.resolve("source.zip");
        Path temporaryPath = archiveDirectory.resolve("source.zip.part");
        ArchiveCopy archiveCopy = copyAndDigest(file, temporaryPath);
        try {
            if (!hasZipSignature(temporaryPath)) {
                throw new IllegalArgumentException("上传内容不是有效的 ZIP 文件");
            }
            moveAtomically(temporaryPath, archivePath);
        } catch (RuntimeException | IOException exception) {
            Files.deleteIfExists(temporaryPath);
            throw exception;
        }

        List<Path> extractedFiles;
        try {
            extractedFiles = extractor.extract(archivePath, extractRoot);
            if (extractedFiles.isEmpty()) {
                throw new IllegalArgumentException("ZIP 中没有可处理的文件");
            }
        } catch (IOException | IllegalArgumentException exception) {
            throw new ReportFileHandlingException(
                    "EXTRACT", archivePath, exception.getMessage(), exception);
        }
        return new ArchiveResult(originalFileName, archivePath, extractRoot,
                archiveCopy.fileSize, archiveCopy.sha256, extractedFiles,
                "application/zip", "zip");
    }

    private void moveAtomically(Path temporaryPath, Path archivePath) throws IOException {
        try {
            Files.move(temporaryPath, archivePath, StandardCopyOption.ATOMIC_MOVE);
        } catch (java.nio.file.AtomicMoveNotSupportedException exception) {
            Files.move(temporaryPath, archivePath);
        }
    }

    private String validateUpload(MultipartFile file, String businessType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(isFlashIncome(businessType)
                    ? "请选择 Excel 或 ZIP 上报文件"
                    : "请选择 ZIP 上报文件");
        }
        String originalFileName = baseName(file.getOriginalFilename());
        String lower = originalFileName.toLowerCase(Locale.ROOT);
        boolean zip = lower.endsWith(".zip");
        boolean excel = lower.endsWith(".xls") || lower.endsWith(".xlsx");
        if (isFlashIncome(businessType)) {
            if (!zip && !excel) {
                throw new IllegalArgumentException("快报收入仅支持 .xls / .xlsx 或 ZIP");
            }
        } else if (!zip) {
            throw new IllegalArgumentException("上报文件必须是 ZIP 格式");
        }
        if (file.getSize() > properties.getMaxUploadBytes()) {
            throw new IllegalArgumentException("上报文件超过允许的上传大小");
        }
        return originalFileName;
    }

    private boolean isFlashIncome(String businessType) {
        return businessType != null && "FLASH_INCOME".equalsIgnoreCase(businessType.trim());
    }

    private ArchiveCopy copyAndDigest(MultipartFile file, Path temporaryPath) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }

        long total = 0L;
        try (InputStream input = file.getInputStream();
             OutputStream output = Files.newOutputStream(temporaryPath,
                     StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = input.read(buffer)) != -1) {
                total += read;
                if (total > properties.getMaxUploadBytes()) {
                    throw new IllegalArgumentException("上报文件超过允许的上传大小");
                }
                digest.update(buffer, 0, read);
                output.write(buffer, 0, read);
            }
        } catch (RuntimeException | IOException exception) {
            Files.deleteIfExists(temporaryPath);
            throw exception;
        }
        return new ArchiveCopy(total, toHex(digest.digest()));
    }

    private boolean hasZipSignature(Path file) throws IOException {
        byte[] signature = new byte[4];
        try (InputStream input = Files.newInputStream(file)) {
            if (input.read(signature) != signature.length) {
                return false;
            }
        }
        return signature[0] == 'P' && signature[1] == 'K'
                && ((signature[2] == 3 && signature[3] == 4)
                || (signature[2] == 5 && signature[3] == 6)
                || (signature[2] == 7 && signature[3] == 8));
    }

    private String baseName(String originalFileName) {
        if (originalFileName == null) {
            return "";
        }
        String normalized = originalFileName.replace('\\', '/');
        int separator = normalized.lastIndexOf('/');
        String value = separator >= 0 ? normalized.substring(separator + 1) : normalized;
        return value.replaceAll("[\\p{Cntrl}]", "").trim();
    }

    private String sanitizeFileName(String fileName) {
        String base = baseName(fileName);
        String cleaned = base.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
        return cleaned.isEmpty() ? "upload.bin" : cleaned;
    }

    private String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) {
            return "bin";
        }
        return fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private String contentTypeForExtension(String extension) {
        if ("xlsx".equals(extension)) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        if ("xls".equals(extension)) {
            return "application/vnd.ms-excel";
        }
        return "application/octet-stream";
    }

    private String safeSegment(String value, String fieldName) {
        if (value == null || !value.matches("[A-Za-z0-9_-]{1,64}")) {
            throw new IllegalArgumentException(fieldName + " contains unsupported characters");
        }
        return value;
    }

    private String toHex(byte[] bytes) {
        StringBuilder value = new StringBuilder(bytes.length * 2);
        for (byte current : bytes) {
            value.append(String.format("%02x", current & 0xff));
        }
        return value.toString();
    }

    private static final class ArchiveCopy {
        private final long fileSize;
        private final String sha256;

        private ArchiveCopy(long fileSize, String sha256) {
            this.fileSize = fileSize;
            this.sha256 = sha256;
        }
    }

    @Getter
    public static final class ArchiveResult {
        private final String originalFileName;
        private final Path archivePath;
        private final Path extractRoot;
        private final long fileSize;
        private final String sha256;
        private final List<Path> extractedFiles;
        private final String contentType;
        private final String fileExtension;

        private ArchiveResult(String originalFileName,
                              Path archivePath,
                              Path extractRoot,
                              long fileSize,
                              String sha256,
                              List<Path> extractedFiles,
                              String contentType,
                              String fileExtension) {
            this.originalFileName = originalFileName;
            this.archivePath = archivePath;
            this.extractRoot = extractRoot;
            this.fileSize = fileSize;
            this.sha256 = sha256;
            this.extractedFiles = Collections.unmodifiableList(extractedFiles);
            this.contentType = contentType;
            this.fileExtension = fileExtension;
        }
    }
}
