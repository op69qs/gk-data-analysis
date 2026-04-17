package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionFileService {

    List<Map<String,Object>> getInspectionFileData(PageData pd);

    void addInspectionFile(PageData pd);

    void editInspectionFile(PageData pd);

    void delInspectionFile(PageData pd);
}
