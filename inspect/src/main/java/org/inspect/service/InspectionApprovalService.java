package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionApprovalService {
    List<Map<String,Object>> getInspectionApprovalPage(PageData pd);

    List<Map<String,Object>> getInspectionApprovalData(PageData pd);
    List<Map<String,Object>> getAppravalProcess(PageData pd);

    Integer getInspectionApprovalCount(PageData pd);

    void addInspectionApproval(PageData pd);

    void editInspectionApproval(PageData pd);

    void delInspectionApproval(PageData pd);
    void updateApproval(PageData pd);
}
