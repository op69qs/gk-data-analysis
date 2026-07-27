package org.jeecg.modules.reporting.exception;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Path;

@Getter
public class ReportFileHandlingException extends IOException {
    private static final long serialVersionUID = 1L;

    private final String stage;
    private final Path archivePath;

    public ReportFileHandlingException(String stage, Path archivePath, String message, Throwable cause) {
        super(message, cause);
        this.stage = stage;
        this.archivePath = archivePath;
    }
}
