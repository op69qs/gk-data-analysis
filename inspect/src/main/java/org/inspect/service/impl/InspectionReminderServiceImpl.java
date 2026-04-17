// InspectionReminderServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionReminderMapper;
import org.inspect.service.InspectionReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

/**
 * 提醒服务实现类
 * @author Created by Samer on 2019/10/15.
 */
@Service
public class InspectionReminderServiceImpl implements InspectionReminderService {

    @Autowired
    private InspectionReminderMapper inspectionReminderMapper;

    /**
     * 新增提醒信息
     * @param params 立即启动提醒信息
     * @throws SQLException
     */
    @Override
    public void addReminderInfo(Map<String, Object> params) throws SQLException{
        inspectionReminderMapper.addReminderInfo(params);
    }

    /**
     * 提醒信息系统同步，调用同步存储过程
     * @throws SQLException
     */
    @Override
    public void callReminderSyncProcedure(Map<String, String> params) throws SQLException {
        inspectionReminderMapper.callReminderSyncProcedure(params);
    }


} ///:~
