// InspectionEnforceLawReportServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionEnforceLawReportMapper;
import org.inspect.service.InspectionEnforceLawReportService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 执法检查报告
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionEnforceLawReportServiceImpl implements InspectionEnforceLawReportService {

    @Autowired
    private InspectionEnforceLawReportMapper inspectionEnforceLawReportMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionEnforceLawReportMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionEnforceLawReportMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionEnforceLawReportMapper.addRecord(params);
    }
} ///:~
