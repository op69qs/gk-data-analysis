// InspectionReportService.java

package org.inspect.service;

import org.inspect.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 检查报告
 * @author Created by Samer on 2019/10/25.
 */
@Service
public interface InspectionReportService {

    /**
     * 获取检查项目
     * @return
     */
    List<Map<String, String>> getInspectItemsByTypeId(PageData params);

    /**
     * 获取检查组信息
     * @return
     */
    List<Map<String, String>> getGroupUsers(PageData pd);

    /**
     * 根据ID更新检查报告
     * @param pd
     */
    void updateReportById(PageData pd);

    /**
     * 根据任务ID或者报告ID获取报告信息
     * @return
     */
    Map<String, String> getReportById(PageData pd);

    /**
     * 新增检查报告
     * @param pd
     */
    void addInspectionReport(PageData pd);

}///:~
