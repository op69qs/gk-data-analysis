// InspectionEvidenceNotificationServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionEvidenceNotificationMapper;
import org.inspect.dao.mapper.inspect.InspectionEvidenceRecordMapper;
import org.inspect.service.InspectionEvidenceNotificationService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 执法检查取证记录通知书
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionEvidenceNotificationServiceImpl implements InspectionEvidenceNotificationService {

    @Autowired
    private InspectionEvidenceNotificationMapper inspectionEvidenceNotificationMapper;

    /**
     * 获取子表记录
     * @param params
     */
    @Override
    public List<Map<String, Object>> getRecordSub(PageData params){
        return inspectionEvidenceNotificationMapper.getRecordSub(params);
    }

    /**
     * 获取记录
     * @param params
     */
    @Override
    public List<Map<String, Object>> getRecord(PageData params) {
        return inspectionEvidenceNotificationMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionEvidenceNotificationMapper.editRecord(params);
        if( ((List<Map<String, String>>)params.get("dataList")).size() > 0 ) {
            inspectionEvidenceNotificationMapper.delRecordSheet(params);
            inspectionEvidenceNotificationMapper.addRecordSheet(params);
        }
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionEvidenceNotificationMapper.addRecord(params);
        if( ((List<Map<String, String>>)params.get("dataList")).size() > 0 ) {
            inspectionEvidenceNotificationMapper.delRecordSheet(params);
            inspectionEvidenceNotificationMapper.addRecordSheet(params);
        }
    }
} ///:~
