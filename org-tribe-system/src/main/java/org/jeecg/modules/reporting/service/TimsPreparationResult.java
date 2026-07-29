package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsExcelParseError;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class TimsPreparationResult implements AutoCloseable {
    private final int fileCount;
    private final long rowCount;
    private final List<TimsExcelParseError> errors;
    private final Map<String, FileStat> fileStats;
    private final TimsSpool spool;

    TimsPreparationResult(int fileCount, long rowCount,
                          List<TimsExcelParseError> errors,
                          Map<String, FileStat> fileStats,
                          TimsSpool spool) {
        this.fileCount = fileCount;
        this.rowCount = rowCount;
        this.errors = Collections.unmodifiableList(errors);
        this.fileStats = Collections.unmodifiableMap(fileStats);
        this.spool = spool;
    }

    public int getFileCount() { return fileCount; }
    public long getRowCount() { return rowCount; }
    public List<TimsExcelParseError> getErrors() { return errors; }
    public Map<String, FileStat> getFileStats() { return fileStats; }
    public TimsSpool getSpool() { return spool; }

    @Override
    public void close() throws IOException {
        if (spool != null) spool.close();
    }

    public static final class FileStat {
        private final long successRowCount;
        private final long errorRowCount;

        public FileStat(long successRowCount, long errorRowCount) {
            this.successRowCount = successRowCount;
            this.errorRowCount = errorRowCount;
        }

        public long getSuccessRowCount() { return successRowCount; }
        public long getErrorRowCount() { return errorRowCount; }
        public long getTotalRowCount() { return successRowCount + errorRowCount; }
    }
}
