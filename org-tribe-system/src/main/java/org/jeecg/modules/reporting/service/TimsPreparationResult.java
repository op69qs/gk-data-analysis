package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsExcelParseError;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public final class TimsPreparationResult implements AutoCloseable {
    private final int fileCount;
    private final long rowCount;
    private final List<TimsExcelParseError> errors;
    private final TimsSpool spool;

    TimsPreparationResult(int fileCount, long rowCount,
                          List<TimsExcelParseError> errors, TimsSpool spool) {
        this.fileCount = fileCount;
        this.rowCount = rowCount;
        this.errors = Collections.unmodifiableList(errors);
        this.spool = spool;
    }

    public int getFileCount() { return fileCount; }
    public long getRowCount() { return rowCount; }
    public List<TimsExcelParseError> getErrors() { return errors; }
    public TimsSpool getSpool() { return spool; }

    @Override
    public void close() throws IOException {
        if (spool != null) spool.close();
    }
}
