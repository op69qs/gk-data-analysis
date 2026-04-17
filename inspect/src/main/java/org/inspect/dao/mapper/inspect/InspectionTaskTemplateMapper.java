// inspectionTaskTemplateMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 监督检查，任务模板映射器
 * @author Created by Samer on 2019/10/11.
 */
@Repository
public interface InspectionTaskTemplateMapper {

    /**
     * 根据任务类型ID获取大流程步骤
     * @param TEMP_TYPE_ID 任务类型ID，从枚举表中获取
     * @return 返回任务类型下所有的大流程ID及名称
     */
    List<Map<String, String>> getTaskProcessByTypeId(@Param(value = "TEMP_TYPE_ID") String TEMP_TYPE_ID);

    /**
     * 根据大流程ID获取小流程
     * @param TEMP_PROC_ID 任务大流程ID
     * @return 返回大流程名称流程
     */
    List<Map<String, String>> getSubProcessByProcId(@Param(value = "TEMP_PROC_ID") String TEMP_PROC_ID);

}///:~
