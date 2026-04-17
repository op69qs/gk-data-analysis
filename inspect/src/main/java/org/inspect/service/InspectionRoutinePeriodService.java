// InspectionRoutinePeriodService.java

package org.inspect.service;

import org.inspect.util.PageData;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/2/24.
 */
public interface InspectionRoutinePeriodService {

    /**
     * 获取例行检查类型与周期记录
     * @param params
     * @return
     */
    List<Map<String, Object>> getRoutinePeriodInfo(PageData params);

    /**
     * 根据检查类型删除周期
     * @param params
     * @return
     */
    void delRoutinePeriodByTypeId(PageData params);


    /**
     * 新增检查类型周期
     * @param params
     * @return
     */
    void insertRoutinePeriod(PageData params);

}///:~
