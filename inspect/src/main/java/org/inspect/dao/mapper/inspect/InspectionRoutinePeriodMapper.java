// InspectionRoutinePeriodMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/2/24.
 */
public interface InspectionRoutinePeriodMapper {

    /**
     * 获取例行检查类型与周期记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getRoutinePeriodInfo(@Param(value = "params") PageData params);

    /**
     * 根据检查类型删除周期
     * @param params
     * @return
     */
    void delRoutinePeriodByTypeId(@Param(value = "params") PageData params);


    /**
     * 新增检查类型周期
     * @param params
     * @return
     */
    void insertRoutinePeriod(@Param(value = "params") PageData params);


}///:~
