// InspectionReportMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * 数据核查
 * @author Created by Samer on 2019/10/25.
 */
public interface InspectionDataCheckMapper {



    /**
     * 根据ID更新数据核查
     * @param params
     */
    void updateDataCheckById(@Param(value = "params") PageData params);

    /**
     * 根据任务ID或者数据核查ID获取数据核查
     * @return
     */
    Map<String, String> getDataCheckById(@Param(value = "params") PageData params);


    /**
     * 新增数据核查
     * @param params
     */
    void addInspectionDataCheck(@Param(value = "params") PageData params);
    void delInspectionCheck(@Param(value = "params") PageData params);

    List<Map<String, Object>> getInspectionCheck(@Param(value = "params") PageData params);
    List<Map<String, Object>> getInspectionCheckInspected(@Param(value = "params") PageData params);
    List<Map<String, Object>> checkRepeat(@Param(value = "params") PageData params);
    List<Map<String, Object>> getInspectionCheckOne(@Param(value = "params") PageData params);
    int getInspectionCheckCount(@Param(value = "params") PageData params);

}///:~
