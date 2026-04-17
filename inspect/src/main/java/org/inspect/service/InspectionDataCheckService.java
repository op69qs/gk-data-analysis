// InspectionReportService.java

package org.inspect.service;

import org.inspect.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据核查
 * @author Created by Samer on 2019/10/25.
 */
@Service
public interface InspectionDataCheckService {

    /**
     * 根据ID更新检查报告
     * @param pd
     */
    void updateDataCheckById(PageData pd);
//    void updateReportById(PageData pd);

    /**
     * 根据任务ID或者核查数据ID获取报告信息
     * @return
     */
    Map<String, String> getDataCheckById(PageData pd);

    /**
     * 新增数据核查
     * @param pd
     */
//    void addInspectionReport(PageData pd);
    void addInspectionDataCheck(PageData pd);


    List<Map<String, Object>> getInspectionCheck(PageData pd);
    List<Map<String, Object>> getInspectionCheckInspected(PageData pd);
    List<Map<String,Object>> checkRepeat(PageData pd);
    List<Map<String, Object>> getInspectionCheckOne(PageData pd);
    void delInspectionCheck(PageData pd);
    int getInspectionCheckCount(PageData pd);

}///:~
