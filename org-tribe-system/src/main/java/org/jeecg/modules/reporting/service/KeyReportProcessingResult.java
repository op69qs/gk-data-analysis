package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.KeyFileParseError;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KeyReportProcessingResult {
    private final Map<String, TypeResult> typeResults;
    private final List<KeyFileParseError> errors;

    public KeyReportProcessingResult(Map<String, TypeResult> typeResults, List<KeyFileParseError> errors) {
        this.typeResults = Collections.unmodifiableMap(new LinkedHashMap<>(typeResults));
        this.errors = Collections.unmodifiableList(errors);
    }

    public int getSuccessCount() {
        int total = 0;
        for (TypeResult result : typeResults.values()) {
            total += result.getSuccessCount();
        }
        return total;
    }

    public int getErrorCount() {
        return errors.size();
    }

    public TypeResult getTypeResult(String fileCode) {
        return typeResults.get(fileCode);
    }

    public Map<String, TypeResult> getTypeResults() {
        return typeResults;
    }

    public List<KeyFileParseError> getErrors() {
        return errors;
    }

    public static class TypeResult {
        private final int fileCount;
        private final int successCount;
        private final int errorCount;

        public TypeResult(int fileCount, int successCount, int errorCount) {
            this.fileCount = fileCount;
            this.successCount = successCount;
            this.errorCount = errorCount;
        }

        public int getFileCount() { return fileCount; }
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
    }
}
