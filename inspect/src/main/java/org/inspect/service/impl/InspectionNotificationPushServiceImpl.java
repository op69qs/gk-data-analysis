// InspectionNotificationPushServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionNotificationPushMapper;
import org.inspect.service.InspectionNotificationPushService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 检查任务通知推送
 * @author Created by Samer on 2019/11/11.
 */
@Service
public class InspectionNotificationPushServiceImpl implements InspectionNotificationPushService {

    @Autowired
    private InspectionNotificationPushMapper inspectionNotificationPushMapper;

    /**
     * 获取国库名称（编码）
     * @param params
     * @return
     */
    @Override
    public String getReceiveTreDscr(PageData params) {
        return inspectionNotificationPushMapper.getReceiveTreDscr(params);
    }

    /**
     * 新增发布信息
     * @param params
     */
    @Override
    public void addInfoPub(PageData params) {
        inspectionNotificationPushMapper.addInfoPub(params);
    }

    /**
     * 新增附件信息
     * @param params
     */
    @Override
    public void addInfoAtta(PageData params) {
        inspectionNotificationPushMapper.addInfoAtta(params);
    }

    /**
     * 新增接受机构信息
     * @param params
     */
    @Override
    public void addReceiveOrg(PageData params) {
        inspectionNotificationPushMapper.addReceiveOrg(params);
    }


} ///:~
