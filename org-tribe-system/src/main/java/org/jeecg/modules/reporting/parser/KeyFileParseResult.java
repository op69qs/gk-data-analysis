package org.jeecg.modules.reporting.parser;

import java.util.Collections;
import java.util.List;

public class KeyFileParseResult {
    private final List<KeyReportRecord> records;
    private final List<KeyFileParseError> errors;

    public KeyFileParseResult(List<KeyReportRecord> records, List<KeyFileParseError> errors) {
        this.records = Collections.unmodifiableList(records);
        this.errors = Collections.unmodifiableList(errors);
    }

    public List<KeyReportRecord> getRecords() {
        return records;
    }

    public List<KeyFileParseError> getErrors() {
        return errors;
    }
}
