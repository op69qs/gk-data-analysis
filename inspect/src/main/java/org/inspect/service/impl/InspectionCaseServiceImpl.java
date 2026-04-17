package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionCaseMapper;
import org.inspect.service.InspectionCaseService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionCaseServiceImpl implements InspectionCaseService {

    @Autowired
    private InspectionCaseMapper inspectionCaseMapper;

    @Override
    public List<Map<String, Object>> getInspectionCasePage(PageData pd) {
        return inspectionCaseMapper.getInspectionCasePage(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCaseData(PageData pd) {
        return inspectionCaseMapper.getInspectionCaseData(pd);
    }

    @Override
    public Integer countCase(PageData pd) {
        return inspectionCaseMapper.countCase(pd);
    }

    @Override
    public void addInspectionCase(PageData pd) {
        inspectionCaseMapper.addInspectionCase(pd);
    }

    @Override
    public void editInspectionCase(PageData pd) {
        inspectionCaseMapper.editInspectionCase(pd);
    }

    @Override
    public void delInspectionCase(PageData pd) {
        inspectionCaseMapper.delInspectionCase(pd);
    }
}
