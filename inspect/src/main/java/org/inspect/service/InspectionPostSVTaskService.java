package org.inspect.service;


import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

public interface InspectionPostSVTaskService {

    List<Map<String,Object>> getInspectionTaskPage(PageData pd);

    List<Map<String,Object>> getInspectionTaskData(PageData pd);

    Integer getInspectionTaskCount(PageData pd);

    void addInspectionTask(PageData pd);

    void editInspectionTask(PageData pd);

    void editTaskLock(PageData pd);

    void delInspectionTask(PageData pd);

    List<Map<String,Object>> checkRepeat(PageData pd);

    Map<String,Object> getGKbyBook(PageData pd);

    List<Map<String,Object>> getPeriod(PageData pd);
}
