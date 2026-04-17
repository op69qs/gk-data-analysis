// InspectionReportMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 检查报告
 * @author Created by Samer on 2019/10/25.
 */
public interface InspectionReportMapper {

    /**
     * 获取检查项目
     * @return
     */
    List<Map<String, String>> getInspectItemsByTypeId(@Param(value = "params")PageData params);

    /**
     * 根据任务ID或者报告ID获取报告信息
     * @return
     */
    List<Map<String, String>> getGroupUsers(@Param(value = "params")PageData params);

    /**
     * 根据ID更新检查报告
     * @param params
     */
    void updateReportById(@Param(value = "params")PageData params);

    /**
     * 根据任务ID或者报告ID获取报告信息
     * @return
     */
    Map<String, String> getReportById(@Param(value = "params")PageData params);

    /**
     * 新增检查报告
     * @param params
     */
    void addInspectionReport(@Param(value = "params")PageData params);

}///:~
