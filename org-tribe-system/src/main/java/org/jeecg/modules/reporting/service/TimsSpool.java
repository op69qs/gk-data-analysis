package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsReportRecord;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class TimsSpool implements AutoCloseable {
    private static final int MAX_RECORD_BYTES = 16 * 1024 * 1024;
    private final Path path;
    private final long rowCount;
    private final String sha256;
    private final TimsSpoolCodec codec = new TimsSpoolCodec();

    private TimsSpool(Path path, long rowCount, String sha256) {
        this.path = path;
        this.rowCount = rowCount;
        this.sha256 = sha256;
    }

    public static Writer create(Path workRoot) throws IOException {
        Path normalized = workRoot.toAbsolutePath().normalize();
        Files.createDirectories(normalized);
        Path path = Files.createTempFile(normalized, "tims-", ".spool").toAbsolutePath().normalize();
        if (!path.startsWith(normalized)) throw new IOException("TIMS 临时文件越出批次工作目录");
        try {
            Set<PosixFilePermission> ownerOnly = EnumSet.of(
                    PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
            Files.setPosixFilePermissions(path, ownerOnly);
        } catch (UnsupportedOperationException ignored) {
            // Non-POSIX deployment; the configured archive directory permissions remain authoritative.
        }
        return new Writer(path);
    }

    public Path getPath() { return path; }
    public long getRowCount() { return rowCount; }
    public String getSha256() { return sha256; }

    public void readBatches(int batchSize, Consumer<List<TimsReportRecord>> consumer) throws IOException {
        if (batchSize < 1) throw new IllegalArgumentException("TIMS 批量大小必须大于 0");
        long readCount = 0;
        List<TimsReportRecord> batch = new ArrayList<>(batchSize);
        try (DataInputStream input = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            while (true) {
                int length;
                try {
                    length = input.readInt();
                } catch (EOFException complete) {
                    break;
                }
                if (length < 1 || length > MAX_RECORD_BYTES) {
                    throw new IOException("TIMS 临时记录长度非法：" + length);
                }
                byte[] payload = new byte[length];
                input.readFully(payload);
                batch.add(codec.decode(payload));
                readCount++;
                if (batch.size() == batchSize) {
                    consumer.accept(new ArrayList<>(batch));
                    batch.clear();
                }
            }
        }
        if (!batch.isEmpty()) consumer.accept(new ArrayList<>(batch));
        if (readCount != rowCount) throw new IOException("TIMS 临时文件行数不一致");
    }

    @Override
    public void close() throws IOException {
        Files.deleteIfExists(path);
    }

    public static final class Writer implements AutoCloseable {
        private final Path path;
        private final DataOutputStream output;
        private final TimsSpoolCodec codec = new TimsSpoolCodec();
        private long rowCount;
        private boolean finished;

        private Writer(Path path) throws IOException {
            this.path = path;
            this.output = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)));
        }

        public void write(TimsReportRecord row) throws IOException {
            if (finished) throw new IllegalStateException("TIMS 临时文件已经完成");
            byte[] payload = codec.encode(row);
            output.writeInt(payload.length);
            output.write(payload);
            rowCount++;
        }

        public TimsSpool finish() throws IOException {
            if (finished) throw new IllegalStateException("TIMS 临时文件已经完成");
            output.close();
            finished = true;
            return new TimsSpool(path, rowCount, sha256(path));
        }

        @Override
        public void close() throws IOException {
            if (!finished) {
                try {
                    output.close();
                } finally {
                    Files.deleteIfExists(path);
                }
            }
        }

        private static String sha256(Path path) throws IOException {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(path))) {
                    byte[] buffer = new byte[8192];
                    int count;
                    while ((count = input.read(buffer)) >= 0) digest.update(buffer, 0, count);
                }
                StringBuilder result = new StringBuilder(64);
                for (byte value : digest.digest()) result.append(String.format("%02x", value & 0xff));
                return result.toString();
            } catch (NoSuchAlgorithmException impossible) {
                throw new IllegalStateException("JVM 不支持 SHA-256", impossible);
            }
        }
    }
}
