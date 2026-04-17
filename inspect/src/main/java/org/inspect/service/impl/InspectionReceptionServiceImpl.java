package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionReceptionMapper;
import org.inspect.service.InspectionReceptionService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InspectionReceptionServiceImpl implements InspectionReceptionService {

    @Autowired
    private InspectionReceptionMapper inspectionReceptionMapper;

    @Override
    public Map<String, Object> getInspectionReceptionData(PageData pd) {
        return inspectionReceptionMapper.getInspectionReceptionData(pd);
    }

    @Override
    public void addInspectionReception(PageData pd) {
        inspectionReceptionMapper.addInspectionReception(pd);
    }

    @Override
    public void editInspectionReception(PageData pd) {
        inspectionReceptionMapper.editInspectionReception(pd);
    }

    @Override
    public void delInspectionReception(PageData pd) {
        inspectionReceptionMapper.delInspectionReception(pd);
    }
}
