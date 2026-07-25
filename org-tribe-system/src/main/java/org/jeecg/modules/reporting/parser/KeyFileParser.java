package org.jeecg.modules.reporting.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 按原 JAR 的 UTF-8、逐行、TAB 分隔规则解析 KEY 文本。
 */
public class KeyFileParser {

    public KeyFileParseResult parse(Path file, KeyFileType type, String keyZipName) throws IOException {
        List<KeyReportRecord> records = new ArrayList<>();
        List<KeyFileParseError> errors = new ArrayList<>();
        String fileName = file.getFileName().toString();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            long lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split("\t", -1);
                if (fields.length < type.getFieldCount()) {
                    errors.add(new KeyFileParseError(fileName, lineNumber, line,
                            "字段数不足：应至少为 " + type.getFieldCount() + "，实际为 " + fields.length));
                    continue;
                }
                try {
                    records.add(toRecord(fields, type, keyZipName));
                } catch (IllegalArgumentException ex) {
                    errors.add(new KeyFileParseError(fileName, lineNumber, line, ex.getMessage()));
                }
            }
        }
        return new KeyFileParseResult(records, errors);
    }

    private KeyReportRecord toRecord(String[] fields, KeyFileType type, String keyZipName) {
        KeyReportRecord record = new KeyReportRecord();
        record.setDAcct(fields[0]);
        record.setTreCode(fields[1]);
        record.setKeyZipName(keyZipName);

        switch (type) {
            case INCOME:
                record.setSubjectCode(fields[2]);
                record.setTaxOrgCode(fields[3]);
                record.setBudgetType(fields[4]);
                record.setLevel(fields[5]);
                record.setFAmount(decimal(fields[6], "F_AMT"));
                record.setYearAmount(decimal(fields[7], "YEAR_AMT"));
                break;
            case PAYOUT:
                record.setSubjectCode(fields[2]);
                record.setTaxOrgCode(fields[3]);
                record.setCodeType(fields[4]);
                record.setLevel(fields[5]);
                record.setFAmount(decimal(fields[6], "F_AMT"));
                record.setYearAmount(decimal(fields[7], "YEAR_AMT"));
                break;
            case STOCK:
                record.setLevel(fields[2]);
                record.setAccountCode(fields[3]);
                record.setFBalance(decimal(fields[4], "F_BAL"));
                record.setYearInitialBalance(decimal(fields[5], "YEAR_INIT_BAL"));
                break;
            case BACK:
                record.setSubjectCode(fields[2]);
                record.setBudgetType(fields[3]);
                record.setLevel(fields[4]);
                record.setTaxOrgCode(fields[5]);
                record.setBackReason(fields[6]);
                record.setFAmount(decimal(fields[7], "F_AMT"));
                record.setYearAmount(decimal(fields[8], "YEAR_AMT"));
                break;
            default:
                throw new IllegalArgumentException("不支持的 KEY 文件类型：" + type);
        }
        return record;
    }

    private BigDecimal decimal(String value, String fieldName) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " 不是有效数字：" + value);
        }
    }
}
