// InspectionSelfSummaryServiceImpl.java

package org.inspect.service.impl;

import org.inspect.dao.mapper.inspect.InspectionSelfSummaryMapper;
import org.inspect.service.InspectionSelfSummaryService;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 自查汇总表
 *
 * @author Created by Samer on 2019/10/17.
 */
@Service
public class InspectionSelfSummaryServiceImpl implements InspectionSelfSummaryService {

    @Autowired
    private InspectionSelfSummaryMapper inspectionSelfSummaryMapper;

    @Override
    public List<Map<String, Object>> getCurSelfSumInfo(PageData params) {
        return inspectionSelfSummaryMapper.getCurSelfSumInfo(params);
    }

    @Override
    public void editSelfLedgerByLedgerID(Map<String, String> params) {
        inspectionSelfSummaryMapper.editSelfLedgerByLedgerID(params);
    }

    @Override
    public Map<String, String> getGuokuInfo(Map<String, String> params) {
        return inspectionSelfSummaryMapper.getGuokuInfo(params);
    }
} ///:~
