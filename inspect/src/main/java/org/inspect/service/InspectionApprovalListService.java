package org.inspect.service;


import org.inspect.util.PageData;

import java.util.Map;

public interface InspectionApprovalListService {

    Map<String,Object> getApprovalListData(PageData pd);

    void addApprovalList(PageData pd);

    void editApprovalList(PageData pd);

    void delApprovalList(PageData pd);
}
