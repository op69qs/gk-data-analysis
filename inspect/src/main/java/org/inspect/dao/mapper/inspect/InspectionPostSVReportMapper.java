// InspectionPostSVReportMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 事后监督监督季度报告
 * @author Created by Samer on 2019/11/21.
 */
public interface InspectionPostSVReportMapper {

    /**
     * 获取记录
     * @param params
     */
    List<Map<String, Object>> getRecord(@Param(value = "params") Map<String, Object> params);

    /**
     * 获取记录条数
     * @param params
     */
    Integer getRecordCount(@Param(value = "params") Map<String, Object> params);

    /**
     * 修改记录
     * @param params
     */
    void editRecord(@Param(value = "params") Map<String, Object> params);

    /**
     * 新增记录
     * @param params
     */
    void addRecord(@Param(value = "params") Map<String, Object> params);


}///:~
