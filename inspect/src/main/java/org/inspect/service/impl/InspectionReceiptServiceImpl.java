package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionDataCheckMapper;
import org.inspect.dao.mapper.inspect.InspectionReceiptMapper;
import org.inspect.service.InspectionDataCheckService;
import org.inspect.service.InspectionReceiptService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dj on 2019/11/15.
 */
@Service
public class InspectionReceiptServiceImpl implements InspectionReceiptService {

    @Autowired
    private InspectionReceiptMapper inspectionReceiptMapper;


    @Override
    public void updateReceiptById(PageData pd) {
        inspectionReceiptMapper.updateReceiptById(pd);
    }

    @Override
    public void addReceiptList(PageData pd) {
        inspectionReceiptMapper.addReceiptList(pd);
    }

    @Override
    public List<Map<String, Object>> getInspectionCheck(PageData pd) {
        return inspectionReceiptMapper.getInspectionCheck(pd);
    }


    @Override
    public List<Map<String, Object>> checkRepeat(PageData pd) {
        return inspectionReceiptMapper.checkRepeat(pd);
    }


    @Override
    public void delInspectionCheck(PageData pd) {
        inspectionReceiptMapper.delInspectionCheck(pd);
    }

    @Override
    public void skipInspection(PageData pd) {
        inspectionReceiptMapper.skipInspection(pd);
    }

}
