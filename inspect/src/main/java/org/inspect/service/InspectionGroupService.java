package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionGroupService {

    List<Map<String,Object>> getInspectionGroupPage(PageData pd);

    List<Map<String,Object>> getInspectionGroupData(PageData pd);

    Integer getInspectionGroupCount(PageData pd);

    void addInspectionGroup(PageData pd);

    void editInspectionGroup(PageData pd);

    void delInspectionGroup(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);
}
