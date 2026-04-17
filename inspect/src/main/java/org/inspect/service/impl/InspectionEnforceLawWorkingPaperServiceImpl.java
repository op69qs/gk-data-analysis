// InspectionEnforceLawWorkingPaperServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionEnforceLawWorkingPaperMapper;
import org.inspect.service.InspectionEnforceLawWorkingPaperService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 执法检查工作底稿
 * @author Created by Samer on 2019/11/21.
 */
@Service
public class InspectionEnforceLawWorkingPaperServiceImpl implements InspectionEnforceLawWorkingPaperService {

    @Autowired
    private InspectionEnforceLawWorkingPaperMapper inspectionEnforceLawWorkingPaperMapper;

    /**
     * 获取记录
     * @param params
     */
    @Override
    public Map<String, Object> getRecord(PageData params) {
        return inspectionEnforceLawWorkingPaperMapper.getRecord(params);
    }

    /**
     * 修改记录
     * @param params
     */
    @Override
    public void editRecord(PageData params) {
        inspectionEnforceLawWorkingPaperMapper.editRecord(params);
    }

    /**
     * 新增记录
     * @param params
     */
    @Override
    public void addRecord(PageData params) {
        inspectionEnforceLawWorkingPaperMapper.addRecord(params);
    }
} ///:~
