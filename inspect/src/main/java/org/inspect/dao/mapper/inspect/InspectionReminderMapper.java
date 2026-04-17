// inspectionReminderMapper.java

package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author Created by Samer on 2019/10/15.
 */
public interface InspectionReminderMapper {

    /**
     * 新增提醒信息
     * @param params
     */
    void addReminderInfo(@Param(value = "params") Map<String, Object> params);

    /**
     * 提醒信息系统同步，调用同步存储过程
     */
    void callReminderSyncProcedure(@Param(value = "params") Map<String, String> params);

}///:~
