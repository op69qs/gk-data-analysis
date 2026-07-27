package org.jeecg.modules.reporting.exception;

import lombok.Getter;

@Getter
public class ReportUploadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String batchId;
    private final String stage;

    public ReportUploadException(String batchId, String stage, String message, Throwable cause) {
        super(message, cause);
        this.batchId = batchId;
        this.stage = stage;
    }
}
