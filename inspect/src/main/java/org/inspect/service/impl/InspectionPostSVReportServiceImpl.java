// InspectionPostSVReportService.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVReportMapper;
import org.inspect.service.InspectionPostSVReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 事后监督监督季度报告
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionPostSVReportServiceImpl implements InspectionPostSVReportService {

    @Autowired
    private InspectionPostSVReportMapper inspectionPostSVReportMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public List<Map<String, Object>> getRecord(Map<String, Object> params) {
        return inspectionPostSVReportMapper.getRecord(params);
    }

    /**
     * 获取记录条数
     * @param params
     */
    public Integer getRecordCount(Map<String, Object> params){
        return inspectionPostSVReportMapper.getRecordCount(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(Map<String, Object> params) {
        inspectionPostSVReportMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(Map<String, Object> params) {
        inspectionPostSVReportMapper.addRecord(params);
    }
} ///:~
