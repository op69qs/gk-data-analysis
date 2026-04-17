package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionProcService {

    List<Map<String,Object>> getInspectionProcData(PageData pd);

    void addInspectionProc(PageData pd);

    void editInspectionProc(PageData pd);

    void editProcActive(PageData pd);

    void editProcInfo(PageData pd);

}
