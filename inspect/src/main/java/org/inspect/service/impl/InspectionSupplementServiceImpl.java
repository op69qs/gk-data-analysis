// InspectionSupplementServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionSupplementMapper;
import org.inspect.service.InspectionSupplementService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2020/3/4.
 */
@Service
public class InspectionSupplementServiceImpl implements InspectionSupplementService {

    @Autowired
    private InspectionSupplementMapper inspectionSupplementMapper;

    @Override
    public List<Map<String, Object>> getSupplementLedgerInfo(PageData params) {
        return inspectionSupplementMapper.getSupplementLedgerInfo(params);
    }

    @Override
    public void editSupplementTask(PageData params) {
        inspectionSupplementMapper.editSupplementTask(params);
    }

    @Override
    public List<Map<String, Object>> getSupplementTask(PageData params) {
        return inspectionSupplementMapper.getSupplementTask(params);
    }

    @Override
    public int getSupplementTaskCount(PageData params) {
        return inspectionSupplementMapper.getSupplementTaskCount(params);
    }

    @Override
    public void addSupplementTask(PageData params) {
        inspectionSupplementMapper.addSupplementTask(params);
    }

    @Override
    public void delSupplementTask(PageData params) {
        inspectionSupplementMapper.delSupplementTask(params);
    }

    @Override
    public void addSupplementLedger(PageData params) {
        inspectionSupplementMapper.addSupplementLedger(params);
    }

    @Override
    public void addSupplementRule(PageData params) {
        inspectionSupplementMapper.addSupplementRule(params);
    }

    @Override
    public void editSupplementLedgerById(PageData params) {
        inspectionSupplementMapper.editSupplementLedgerById(params);
    }

    @Override
    public void delSupplementLedgerById(PageData params) {
        inspectionSupplementMapper.delSupplementLedgerById(params);
    }

    @Override
    public void delSupplementRule(PageData params) {
        inspectionSupplementMapper.delSupplementRule(params);
    }
} ///:~
