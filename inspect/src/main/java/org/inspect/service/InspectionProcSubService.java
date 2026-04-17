package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionProcSubService {

    List<Map<String,Object>> getInspectionProcSubData(PageData pd);

    void addInspectionProcSub(PageData pd);

    void editInspectionProcSub(PageData pd);

    void editPostSVInspectionProcSub(PageData pd);

    void editProcBySubProc(PageData pd);
}
