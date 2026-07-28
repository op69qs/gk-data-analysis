package org.jeecg.modules.reporting.service;

public class ReportProcessBusyException extends RuntimeException {
    public ReportProcessBusyException(String message) {
        super(message);
    }
}
