package org.jeecg.modules.reporting.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReportChangeCommand {
    private Date accountingDate;
    private String treasuryCode;
    private String statisticsCode;
    private String budgetLevel;
    private String type;
    private BigDecimal oldAmount;
    private BigDecimal newAmount;
    private BigDecimal differenceAmount;
    private Date updateDate;
    private String updateUser;
}
