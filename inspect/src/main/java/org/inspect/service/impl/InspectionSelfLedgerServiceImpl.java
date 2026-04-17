// InspectionSelfLedgerServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionSelfLedgerMapper;
import org.inspect.service.InspectionSelfLedgerService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 自查问题台账
 *
 * @author Created by Samer on 2019/10/17.
 */
@Service
public class InspectionSelfLedgerServiceImpl implements InspectionSelfLedgerService {

    @Autowired
    private InspectionSelfLedgerMapper inspectionSelfLedgerMapper;

    @Override
    public Map<String, String> getTaskInfoById(PageData params) {
        return inspectionSelfLedgerMapper.getTaskInfoById(params);
    }

    @Override
    public Map<String, String> getGuokuInfo(PageData params) {
        return inspectionSelfLedgerMapper.getGuokuInfo(params);
    }

    @Override
    public List<Map<String, String>> getSelfLedgerByUserIdTaskID(PageData params) {
        return inspectionSelfLedgerMapper.getSelfLedgerByUserIdTaskID(params);
    }

    @Override
    public List<Map<String, String>> getLedgerAddUserByTaskId(PageData params) {
        return inspectionSelfLedgerMapper.getLedgerAddUserByTaskId(params);
    }

    @Override
    public List<Map<String, String>> getSelfLedgerByLedgerID(PageData params) {
        return inspectionSelfLedgerMapper.getSelfLedgerByLedgerID(params);
    }

    @Override
    public void addSelfLedger(PageData params) {
        inspectionSelfLedgerMapper.addSelfLedger(params);
    }

    @Override
    public void editSelfLedgerByLedgerID(PageData params) {
        inspectionSelfLedgerMapper.editSelfLedgerByLedgerID(params);
    }

    @Override
    public void delSelfLedgerByLedgerId(PageData params) {
        inspectionSelfLedgerMapper.delSelfLedgerByLedgerId(params);
    }

    @Override
    public void addQuestionLedgerRule(Map<String, Object> params) {
        inspectionSelfLedgerMapper.addQuestionLedgerRule(params);
    }

    @Override
    public void delQuestionLedgerRule(Map<String, Object> params) {
        inspectionSelfLedgerMapper.delQuestionLedgerRule(params);
    }


} ///:~
