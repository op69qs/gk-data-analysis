// InspectionReminderService.java

package org.inspect.service;

import java.sql.SQLException;
import java.util.Map;

/**
 * 提醒服务类
 * @author Created by Samer on 2019/10/15.
 */
public interface InspectionReminderService {

    /**
     * 新增提醒信息
     * @param params
     */
    void addReminderInfo(Map<String, Object> params) throws SQLException;

    /**
     * 提醒信息系统同步，调用同步存储过程
     */
    void callReminderSyncProcedure(Map<String, String> params) throws SQLException;


}///:~
