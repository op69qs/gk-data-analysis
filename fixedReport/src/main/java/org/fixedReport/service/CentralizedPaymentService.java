package org.fixedReport.service;

import org.fixedReport.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Created by dang on 2020/05/12
 */
@Service
public interface CentralizedPaymentService {
    List<Map<String,Object>> getBudgetUnit(PageData params);

    List<Map<String,Object>> getAgentBankClass(PageData params);

    List<Map<String,Object>> getGuoKu(PageData params);

    List<Map<String,Object>> getKeMu(PageData params);
    List<Map<String,Object>> getKeMu2(PageData params);

}
