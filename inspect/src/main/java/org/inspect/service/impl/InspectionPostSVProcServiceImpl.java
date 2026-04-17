package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionPostSVProcMapper;
import org.inspect.service.InspectionPostSVProcService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InspectionPostSVProcServiceImpl implements InspectionPostSVProcService {

    @Autowired
    private InspectionPostSVProcMapper inspectionPostSVProcMapper;

    @Override
    public List<Map<String, Object>> getInspectionProcData(PageData pd) {
        return inspectionPostSVProcMapper.getInspectionProcData(pd);
    }

}
