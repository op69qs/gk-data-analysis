package org.jeecg.modules.audit.vo;

import lombok.Data;

@Data
public class DataAnalysisMenuEntryAuditRequest {

    private String menuId;
    private String menuTitle;
    private String menuPath;
    private String routePath;
    private String fullPath;
    private String routeName;
}
