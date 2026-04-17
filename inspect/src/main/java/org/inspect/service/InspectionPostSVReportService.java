// InspectionPostSVReportService.java

package org.inspect.service;

import java.util.List;
import java.util.Map;

/**
 * 事后监督监督季度报告
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionPostSVReportService {

    /**
     * 获取记录
     * @param params
     */
    List<Map<String, Object>> getRecord(Map<String, Object> params);

    /**
     * 获取记录条数
     * @param params
     */
    Integer getRecordCount(Map<String, Object> params);

    /**
     * 修改记录
     * @param params
     */
    void editRecord(Map<String, Object> params);

    /**
     * 新增记录
     * @param params
     */
    void addRecord(Map<String, Object> params);
    

} ///:~
