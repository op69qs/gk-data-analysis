// InspectionPostSVNoticeServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVNoticeMapper;
import org.inspect.service.InspectionPostSVNoticeService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 事后监督监督通知
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionPostSVNoticeServiceImpl implements InspectionPostSVNoticeService {

    @Autowired
    private InspectionPostSVNoticeMapper inspectionPostSVNoticeMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionPostSVNoticeMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionPostSVNoticeMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionPostSVNoticeMapper.addRecord(params);
    }
} ///:~
