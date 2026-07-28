package org.jeecg.modules.reporting.parser;

public class KeyFileParseError {
    private final String fileName;
    private final long lineNumber;
    private final String rawContent;
    private final String message;

    public KeyFileParseError(String fileName, long lineNumber, String rawContent, String message) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.rawContent = rawContent;
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getRawContent() {
        return rawContent;
    }

    public String getMessage() {
        return message;
    }
}
