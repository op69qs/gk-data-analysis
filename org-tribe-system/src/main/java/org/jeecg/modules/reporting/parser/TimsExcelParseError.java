package org.jeecg.modules.reporting.parser;

public class TimsExcelParseError {
    private final String fileName;
    private final String sheetName;
    private final long rowNumber;
    private final String columnName;
    private final String rawValue;
    private final String message;

    public TimsExcelParseError(String fileName, String sheetName, long rowNumber,
                               String columnName, String rawValue, String message) {
        this.fileName = fileName;
        this.sheetName = sheetName;
        this.rowNumber = rowNumber;
        this.columnName = columnName;
        this.rawValue = rawValue;
        this.message = message;
    }

    public String getFileName() { return fileName; }
    public String getSheetName() { return sheetName; }
    public long getRowNumber() { return rowNumber; }
    public String getColumnName() { return columnName; }
    public String getRawValue() { return rawValue; }
    public String getMessage() { return message; }
}
