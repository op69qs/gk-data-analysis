package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionCaseService {

    List<Map<String,Object>> getInspectionCasePage(PageData pd);

    List<Map<String,Object>> getInspectionCaseData(PageData pd);

    Integer countCase(PageData pd);

    void addInspectionCase(PageData pd);

    void editInspectionCase(PageData pd);

    void delInspectionCase(PageData pd);
}
