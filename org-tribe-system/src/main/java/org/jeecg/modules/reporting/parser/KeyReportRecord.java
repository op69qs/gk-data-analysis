package org.jeecg.modules.reporting.parser;

import java.math.BigDecimal;

/**
 * 四类 KEY 明细的统一内存模型；未适用于当前文件类型的字段保持 null。
 */
public class KeyReportRecord {
    private String dAcct;
    private String treCode;
    private String subjectCode;
    private String taxOrgCode;
    private String budgetType;
    private String codeType;
    private String level;
    private String accountCode;
    private String backReason;
    private BigDecimal fAmount;
    private BigDecimal yearAmount;
    private BigDecimal fBalance;
    private BigDecimal yearInitialBalance;
    private String keyZipName;

    public String getDAcct() { return dAcct; }
    public void setDAcct(String dAcct) { this.dAcct = dAcct; }
    public String getTreCode() { return treCode; }
    public void setTreCode(String treCode) { this.treCode = treCode; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getTaxOrgCode() { return taxOrgCode; }
    public void setTaxOrgCode(String taxOrgCode) { this.taxOrgCode = taxOrgCode; }
    public String getBudgetType() { return budgetType; }
    public void setBudgetType(String budgetType) { this.budgetType = budgetType; }
    public String getCodeType() { return codeType; }
    public void setCodeType(String codeType) { this.codeType = codeType; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }
    public String getBackReason() { return backReason; }
    public void setBackReason(String backReason) { this.backReason = backReason; }
    public BigDecimal getFAmount() { return fAmount; }
    public void setFAmount(BigDecimal fAmount) { this.fAmount = fAmount; }
    public BigDecimal getYearAmount() { return yearAmount; }
    public void setYearAmount(BigDecimal yearAmount) { this.yearAmount = yearAmount; }
    public BigDecimal getFBalance() { return fBalance; }
    public void setFBalance(BigDecimal fBalance) { this.fBalance = fBalance; }
    public BigDecimal getYearInitialBalance() { return yearInitialBalance; }
    public void setYearInitialBalance(BigDecimal yearInitialBalance) { this.yearInitialBalance = yearInitialBalance; }
    public String getKeyZipName() { return keyZipName; }
    public void setKeyZipName(String keyZipName) { this.keyZipName = keyZipName; }
}
