package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionGroupMapper;
import org.inspect.dao.mapper.inspect.InspectionTaskMapper;
import org.inspect.service.InspectionGroupService;
import org.inspect.service.InspectionTaskService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionGroupServiceImpl implements InspectionGroupService {

    @Autowired
    private InspectionGroupMapper inspectionGroupMapper;


    @Override
    public List<Map<String, Object>> getInspectionGroupPage(PageData pd) {
        return inspectionGroupMapper.getInspectionGroupPage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionGroupData(PageData pd) {
        return inspectionGroupMapper.getInspectionGroupData(pd);
    }

    @Override
    public Integer getInspectionGroupCount(PageData pd) {
        return inspectionGroupMapper.getInspectionGroupCount(pd);
    }

    @Override
    public void addInspectionGroup(PageData pd) {
        inspectionGroupMapper.addInspectionGroup(pd);
    }

    @Override
    public void editInspectionGroup(PageData pd) {
        inspectionGroupMapper.editInspectionGroup(pd);
    }

    @Override
    public void delInspectionGroup(PageData pd) {
        inspectionGroupMapper.delInspectionGroup(pd);
    }

    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionGroupMapper.checkRepeat(pd);
    }
}
