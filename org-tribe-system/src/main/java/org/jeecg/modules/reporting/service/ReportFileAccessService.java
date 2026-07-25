package org.jeecg.modules.reporting.service;

import lombok.Getter;
import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.entity.ReportFile;
import org.jeecg.modules.reporting.mapper.ReportFileMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ReportFileAccessService {
    private final ReportFileMapper fileMapper;
    private final ReportingProperties properties;

    public ReportFileAccessService(ReportFileMapper fileMapper, ReportingProperties properties) {
        this.fileMapper = fileMapper;
        this.properties = properties;
    }

    public Download download(String fileId) throws IOException {
        ReportFile file = fileMapper.selectById(fileId);
        if (file == null || Integer.valueOf(1).equals(file.getDelFlag())
                || !Integer.valueOf(1).equals(file.getRetained())) {
            throw new IllegalArgumentException("文件不存在或已不再保留");
        }
        Path root = Paths.get(properties.getArchiveRoot()).toAbsolutePath().normalize();
        Path stored = Paths.get(file.getStoragePath()).toAbsolutePath().normalize();
        if (!stored.startsWith(root) || !Files.isRegularFile(stored)) {
            throw new IllegalArgumentException("文件存储路径不合法或文件已不存在");
        }
        Path realRoot = root.toRealPath();
        Path realStored = stored.toRealPath();
        if (!realStored.startsWith(realRoot)) {
            throw new IllegalArgumentException("文件存储路径超出上报归档目录");
        }
        return new Download(realStored, safeName(file.getOriginalName()), file.getContentType());
    }

    private String safeName(String name) {
        if (name == null || name.trim().isEmpty()) return "report-file";
        String value = name.replace('\r', '_').replace('\n', '_').replace('"', '_');
        return value.replace('\\', '_').replace('/', '_');
    }

    @Getter
    public static final class Download {
        private final Path path;
        private final String fileName;
        private final String contentType;

        private Download(Path path, String fileName, String contentType) {
            this.path = path;
            this.fileName = fileName;
            this.contentType = contentType;
        }
    }
}
