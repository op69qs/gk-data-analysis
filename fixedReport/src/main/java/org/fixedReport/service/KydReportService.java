package org.fixedReport.service;

import org.fixedReport.util.PageData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public interface KydReportService {
    /*获取分行业列表*/
    List<Map<String, Object>> getIndustryReportAll(PageData pd);
    Integer countIndustryReportAll(PageData pd);

    /*获取分企业列表*/
    List<Map<String, Object>> getEnterpriseReportAll(PageData pd);
    Integer countEnterpriseReportAll(PageData pd);

    /*获取企业排名列表*/
    List<Map<String, Object>> getEnterpriseRankingReportAll(PageData pd);
    Integer countEnterpriseRankingReportAll(PageData pd);

    /**
     * 获取行业下拉
     * @return
     */
    List<Map<String, Object>> getIndustryDrop();
}
