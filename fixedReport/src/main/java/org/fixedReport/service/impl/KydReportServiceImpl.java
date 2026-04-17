package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.KydReportMapper;
import org.fixedReport.service.KydReportService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class KydReportServiceImpl implements KydReportService {

    @Autowired
    private KydReportMapper kydReportMapper;

    @Override
    public List<Map<String, Object>> getIndustryReportAll(PageData pd) {
        return kydReportMapper.getIndustryReportAll(pd);
    }

    @Override
    public Integer countIndustryReportAll(PageData pd) {
        return kydReportMapper.countIndustryReportAll(pd);
    }

    @Override
    public List<Map<String, Object>> getEnterpriseReportAll(PageData pd) {
        return kydReportMapper.getEnterpriseReportAll(pd);
    }

    @Override
    public Integer countEnterpriseReportAll(PageData pd) {
        return kydReportMapper.countEnterpriseReportAll(pd);
    }

    @Override
    public List<Map<String, Object>> getEnterpriseRankingReportAll(PageData pd) {
        return kydReportMapper.getEnterpriseRankingReportAll(pd);
    }

    @Override
    public Integer countEnterpriseRankingReportAll(PageData pd) {
        return kydReportMapper.countEnterpriseRankingReportAll(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryDrop() {
        return kydReportMapper.getIndustryDrop();
    }
}
