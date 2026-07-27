package org.jeecg.modules.reporting.legacy;

import lombok.Data;

import java.util.Date;

@Data
public class LegacyTimsPending {
    private String id;
    private String treCode;
    private String bizType;
    private Date bizDate;
    private String fileName;
    private String filePath;
    private String zipFilePath;
    private Integer dataCount;
    private String fileException;
    private String state;
    private String addUserId;
    private Date addTime;
    private String modUserId;
    private Date modTime;
}
