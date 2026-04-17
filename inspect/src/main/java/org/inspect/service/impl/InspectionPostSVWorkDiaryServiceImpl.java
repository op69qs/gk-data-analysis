// InspectionPostSVWorkDiaryServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVWorkDiaryMapper;
import org.inspect.service.InspectionPostSVWorkDiaryService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 事后监督工作日志
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionPostSVWorkDiaryServiceImpl implements InspectionPostSVWorkDiaryService {

    @Autowired
    private InspectionPostSVWorkDiaryMapper inspectionPostSVWorkDiaryMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionPostSVWorkDiaryMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionPostSVWorkDiaryMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionPostSVWorkDiaryMapper.addRecord(params);
    }
} ///:~
