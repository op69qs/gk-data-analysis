package org.jeecg.modules.reporting.vo;

import lombok.Data;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ReportingBusinessQuery {
    private String guokuId;
    private String treCode;
    private String treName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bizDate;
    private String zipName;
    private String fileName;
    private String state;
    private String exeState;
    private String reported;
    private String statisticsCode;
    private String budgetLevel;
    private String type;
    private int offset;
    private int limit = 10;
}
