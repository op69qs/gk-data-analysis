package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsExcelParseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.sql.Date;

public class TimsReportProcessingResult {
    private final int fileCount;
    private final int successCount;
    private final List<TimsExcelParseError> errors;
    private final List<TreasuryCount> treasuryCounts;

    public TimsReportProcessingResult(int fileCount, int successCount, List<TimsExcelParseError> errors) {
        this(fileCount, successCount, errors, Collections.emptyList());
    }

    public TimsReportProcessingResult(int fileCount, int successCount, List<TimsExcelParseError> errors,
                                      List<TreasuryCount> treasuryCounts) {
        this.fileCount = fileCount;
        this.successCount = successCount;
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        this.treasuryCounts = Collections.unmodifiableList(new ArrayList<>(treasuryCounts));
    }

    public int getFileCount() { return fileCount; }
    public int getSuccessCount() { return successCount; }
    public int getErrorCount() { return errors.size(); }
    public List<TimsExcelParseError> getErrors() { return errors; }
    public List<TreasuryCount> getTreasuryCounts() { return treasuryCounts; }

    public static final class TreasuryCount {
        private final Date businessDate;
        private final String treasuryCode;
        private final int rowCount;

        public TreasuryCount(Date businessDate, String treasuryCode, int rowCount) {
            this.businessDate = businessDate;
            this.treasuryCode = treasuryCode;
            this.rowCount = rowCount;
        }

        public Date getBusinessDate() { return businessDate; }
        public String getTreasuryCode() { return treasuryCode; }
        public int getRowCount() { return rowCount; }
    }
}
