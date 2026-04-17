// InspectionFindingsOfFactServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionFindingsOfFactMapper;
import org.inspect.service.InspectionFindingsOfFactService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 执法检查事实认定书
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionFindingsOfFactServiceImpl implements InspectionFindingsOfFactService {

    @Autowired
    private InspectionFindingsOfFactMapper inspectionFindingsOfFactMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionFindingsOfFactMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionFindingsOfFactMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionFindingsOfFactMapper.addRecord(params);
    }
} ///:~
