package org.jeecg.modules.reporting.parser;

import java.util.Collections;
import java.util.List;

public class TimsExcelParseResult {
    private final List<TimsReportRecord> records;
    private final List<TimsExcelParseError> errors;

    public TimsExcelParseResult(List<TimsReportRecord> records, List<TimsExcelParseError> errors) {
        this.records = Collections.unmodifiableList(records);
        this.errors = Collections.unmodifiableList(errors);
    }

    public List<TimsReportRecord> getRecords() { return records; }
    public List<TimsExcelParseError> getErrors() { return errors; }
}
