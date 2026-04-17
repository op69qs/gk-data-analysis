package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVProcSubMapper;
import org.inspect.service.InspectionPostSVProcSubService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionPostSVProcSubServiceImpl implements InspectionPostSVProcSubService {

    @Autowired
    private InspectionPostSVProcSubMapper inspectionPostSVProcSubMapper;

    @Override
    public List<Map<String, Object>> getInspectionProcSubData(PageData pd) {
        return inspectionPostSVProcSubMapper.getInspectionProcSubData(pd);
    }

}
