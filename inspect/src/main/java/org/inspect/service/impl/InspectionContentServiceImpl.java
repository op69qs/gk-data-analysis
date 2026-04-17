package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionContentMapper;
import org.inspect.dao.mapper.inspect.InspectionProcMapper;
import org.inspect.dao.mapper.inspect.InspectionProcSubMapper;
import org.inspect.service.InspectionContentService;
import org.inspect.service.InspectionProcSubService;
import org.inspect.util.DateUtil;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionContentServiceImpl implements InspectionContentService {

    @Autowired
    private InspectionContentMapper inspectionContentMapper;


    @Override
    public List<Map<String, Object>> getContentData(PageData pd) {
        return inspectionContentMapper.getContentData(pd);
    }

    @Override
    public void addContent(PageData pd) {
        inspectionContentMapper.addContent(pd);
    }

    @Override
    public void editContent(PageData pd) {
        inspectionContentMapper.editContent(pd);
    }

    @Override
    public void delContent(PageData pd) {
        inspectionContentMapper.delContent(pd);
    }
}
