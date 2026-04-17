package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.CentralizedPaymentMapper;
import org.fixedReport.dao.mapper.fixedReport.ReportMapper;
import org.fixedReport.service.CentralizedPaymentService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dang on 2020/05/12
 */
@Service
public class CentralizedPaymentServiceImpl implements CentralizedPaymentService {
    @Autowired
    private CentralizedPaymentMapper centralizedPaymentMapper;
    @Override
    public List<Map<String, Object>> getBudgetUnit(PageData params) {
        return centralizedPaymentMapper.getBudgetUnit(params);
    }

    @Override
    public List<Map<String, Object>> getAgentBankClass(PageData params) {
        return centralizedPaymentMapper.getAgentBankClass(params);
    }

    @Override
    public List<Map<String, Object>> getGuoKu(PageData params) {
        return centralizedPaymentMapper.getGuoKu(params);
    }


    @Override
    public List<Map<String, Object>> getKeMu(PageData params) {
        return centralizedPaymentMapper.getKeMu(params);
    }

    @Override
    public List<Map<String, Object>> getKeMu2(PageData params) {
        return centralizedPaymentMapper.getKeMu2(params);
    }
}
