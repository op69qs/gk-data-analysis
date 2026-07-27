package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsReportRecord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/** Length-prefixed binary codec; unlike CSV it cannot confuse tabs/newlines in business text. */
final class TimsSpoolCodec {
    byte[] encode(TimsReportRecord row) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
        try (DataOutputStream output = new DataOutputStream(buffer)) {
            writeString(output, row.getDAcctText());
            writeString(output, row.getDAcct() == null ? null : row.getDAcct().toString());
            writeString(output, row.getTreCode());
            writeString(output, row.getTreasuryName());
            writeString(output, row.getTaxOrgCode());
            writeString(output, row.getLevel());
            writeString(output, row.getSubjectCode());
            writeString(output, row.getSubjectName());
            writeDecimal(output, row.getCurrentAmount());
            writeDecimal(output, row.getYearAmount());
            writeString(output, row.getAccount());
            writeDecimal(output, row.getDebitAmount());
            writeDecimal(output, row.getCreditAmount());
            writeDecimal(output, row.getBalance());
            writeString(output, row.getFileName());
            writeString(output, row.getSheetName());
            output.writeLong(row.getRowNumber());
        }
        return buffer.toByteArray();
    }

    TimsReportRecord decode(byte[] payload) throws IOException {
        TimsReportRecord row = new TimsReportRecord();
        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(payload))) {
            row.setDAcctText(readString(input));
            String parsedDate = readString(input);
            row.setDAcct(parsedDate == null ? null : LocalDate.parse(parsedDate));
            row.setTreCode(readString(input));
            row.setTreasuryName(readString(input));
            row.setTaxOrgCode(readString(input));
            row.setLevel(readString(input));
            row.setSubjectCode(readString(input));
            row.setSubjectName(readString(input));
            row.setCurrentAmount(readDecimal(input));
            row.setYearAmount(readDecimal(input));
            row.setAccount(readString(input));
            row.setDebitAmount(readDecimal(input));
            row.setCreditAmount(readDecimal(input));
            row.setBalance(readDecimal(input));
            row.setFileName(readString(input));
            row.setSheetName(readString(input));
            row.setRowNumber(input.readLong());
            if (input.available() != 0) throw new IOException("TIMS 临时记录包含未识别内容");
        }
        return row;
    }

    private void writeDecimal(DataOutputStream output, BigDecimal value) throws IOException {
        writeString(output, value == null ? null : value.toPlainString());
    }

    private BigDecimal readDecimal(DataInputStream input) throws IOException {
        String value = readString(input);
        return value == null ? null : new BigDecimal(value);
    }

    private void writeString(DataOutputStream output, String value) throws IOException {
        if (value == null) {
            output.writeInt(-1);
            return;
        }
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        output.writeInt(bytes.length);
        output.write(bytes);
    }

    private String readString(DataInputStream input) throws IOException {
        int length = input.readInt();
        if (length == -1) return null;
        if (length < 0 || length > 16 * 1024 * 1024) throw new IOException("TIMS 临时字段长度非法：" + length);
        byte[] bytes = new byte[length];
        input.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
