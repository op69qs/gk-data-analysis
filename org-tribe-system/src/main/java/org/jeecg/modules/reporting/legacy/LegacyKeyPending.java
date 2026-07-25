package org.jeecg.modules.reporting.legacy;

import lombok.Data;

import java.util.Date;

@Data
public class LegacyKeyPending {
    private String id;
    private String treCode;
    private Date bizDate;
    private String zipName;
    private String srName;
    private String srNameState;
    private Integer srCount;
    private String srException;
    private String zcName;
    private String zcNameState;
    private Integer zcCount;
    private String zcException;
    private String kcName;
    private String kcNameState;
    private Integer kcCount;
    private String kcException;
    private String tkName;
    private String tkNameState;
    private Integer tkCount;
    private String tkException;
    private String zipPath;
    private String unzipDir;
    private String state;
    private String addUserId;
    private Date addTime;
    private String modUserId;
    private Date modTime;
}
