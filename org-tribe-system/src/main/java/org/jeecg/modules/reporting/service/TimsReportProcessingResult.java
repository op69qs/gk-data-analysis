package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsExcelParseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimsReportProcessingResult {
    private final int fileCount;
    private final int successCount;
    private final List<TimsExcelParseError> errors;

    public TimsReportProcessingResult(int fileCount, int successCount, List<TimsExcelParseError> errors) {
        this.fileCount = fileCount;
        this.successCount = successCount;
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
    }

    public int getFileCount() { return fileCount; }
    public int getSuccessCount() { return successCount; }
    public int getErrorCount() { return errors.size(); }
    public List<TimsExcelParseError> getErrors() { return errors; }
}
