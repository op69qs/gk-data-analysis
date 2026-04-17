// InspectionTypeClassServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionTypeClassMapper;
import org.inspect.service.InspectionTypeClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/8.
 */
@Service
public class InspectionTypeClassServiceImpl implements InspectionTypeClassService {

    @Autowired
    private InspectionTypeClassMapper inspectionTypeClassMapper;

    @Override
    public List<Map<String, Object>> getInspectionTypeClass() {
        return inspectionTypeClassMapper.getInspectionTypeClass();
    }
} ///:~
