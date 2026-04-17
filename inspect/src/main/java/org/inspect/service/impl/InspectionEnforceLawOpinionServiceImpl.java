// InspectionEnforceLawOpinionServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionEnforceLawOpinionMapper;
import org.inspect.service.InspectionEnforceLawOpinionService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 执法检查意见书
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionEnforceLawOpinionServiceImpl implements InspectionEnforceLawOpinionService {

    @Autowired
    private InspectionEnforceLawOpinionMapper inspectionEnforceLawOpinionMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionEnforceLawOpinionMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionEnforceLawOpinionMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionEnforceLawOpinionMapper.addRecord(params);
    }
} ///:~
