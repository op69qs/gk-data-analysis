package org.jeecg.modules.reporting.parser;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimsReportRecord {
    private LocalDate dAcct;
    private String treCode;
    private String treasuryName;
    private String taxOrgCode;
    private String level;
    private String subjectCode;
    private String subjectName;
    private BigDecimal currentAmount;
    private BigDecimal yearAmount;
    private String account;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private BigDecimal balance;
    private String fileName;
    private String sheetName;
    private long rowNumber;

    public LocalDate getDAcct() { return dAcct; }
    public void setDAcct(LocalDate dAcct) { this.dAcct = dAcct; }
    public Date getDatabaseDate() { return dAcct == null ? null : Date.valueOf(dAcct); }
    public String getPeriodKey() { return dAcct == null ? null : dAcct.format(DateTimeFormatter.ofPattern("yyyyMM")); }
    public String getTreCode() { return treCode; }
    public void setTreCode(String treCode) { this.treCode = treCode; }
    public String getTreasuryName() { return treasuryName; }
    public void setTreasuryName(String treasuryName) { this.treasuryName = treasuryName; }
    public String getTaxOrgCode() { return taxOrgCode; }
    public void setTaxOrgCode(String taxOrgCode) { this.taxOrgCode = taxOrgCode; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(BigDecimal currentAmount) { this.currentAmount = currentAmount; }
    public BigDecimal getYearAmount() { return yearAmount; }
    public void setYearAmount(BigDecimal yearAmount) { this.yearAmount = yearAmount; }
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public BigDecimal getDebitAmount() { return debitAmount; }
    public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }
    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getSheetName() { return sheetName; }
    public void setSheetName(String sheetName) { this.sheetName = sheetName; }
    public long getRowNumber() { return rowNumber; }
    public void setRowNumber(long rowNumber) { this.rowNumber = rowNumber; }
}
