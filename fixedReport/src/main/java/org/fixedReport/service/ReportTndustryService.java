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
public interface ReportTndustryService {
    List<Map<String, Object>> getTndustryTaxData1All(PageData pd);
    List<Map<String, Object>> getTndustryTaxData1(PageData pd);
    List<Map<String, Object>> getTndustryTaxData(PageData pd);
    List<Map<String, Object>> getTndustryTaxDataAll(PageData pd);
    List<Map<String, Object>> getAllSubject();
    Integer countTndustryTaxData(PageData pd);
    Integer countTndustryTaxData1(PageData pd);
    List<Map<String, Object>> getTndustryTaxAll(PageData pd);
    List<Map<String, Object>> getColALL(PageData pd);
    List<Map<String, Object>> getColHeB(PageData pd);
    List<Map<String, Object>> getColHeBF(PageData pd);

}
