package org.jeecg.modules.reporting.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** The original KEY upload derives period and treasury from the k...t....zip file name. */
public final class LegacyKeyFileName {
    private final LocalDate businessDate;
    private final String treasuryCode;

    private LegacyKeyFileName(LocalDate businessDate, String treasuryCode) {
        this.businessDate = businessDate;
        this.treasuryCode = treasuryCode;
    }

    public static LegacyKeyFileName parse(String fileName) {
        String value = fileName == null ? "" : fileName;
        int k = value.indexOf('k');
        int t = k < 0 ? -1 : value.indexOf('t', k + 1);
        int dot = value.indexOf('.', t + 1);
        if (k < 0 || t <= k + 1 || dot <= t + 1) {
            throw new IllegalArgumentException("KEY 文件名必须包含原程序规定的 k<业务日期>t<国库代码>.zip");
        }
        String date = value.substring(k + 1, t);
        String treasury = value.substring(t + 1, dot);
        if (!treasury.matches("[A-Za-z0-9_-]+")) {
            throw new IllegalArgumentException("KEY 文件名中的国库代码不合法");
        }
        return new LegacyKeyFileName(parseDate(date), treasury);
    }

    private static LocalDate parseDate(String value) {
        for (DateTimeFormatter formatter : new DateTimeFormatter[]{
                DateTimeFormatter.ISO_LOCAL_DATE, DateTimeFormatter.BASIC_ISO_DATE}) {
            try { return LocalDate.parse(value, formatter); }
            catch (DateTimeParseException ignored) { }
        }
        try { return YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth(); }
        catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("KEY 文件名中的业务日期必须为 yyyy-MM-dd、yyyyMMdd 或 yyyy-MM", exception);
        }
    }

    public LocalDate getBusinessDate() { return businessDate; }
    public String getTreasuryCode() { return treasuryCode; }
}
