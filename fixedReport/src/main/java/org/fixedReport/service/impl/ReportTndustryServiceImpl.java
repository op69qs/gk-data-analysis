package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.ReportTndustryMapper;
import org.fixedReport.service.ReportTndustryService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dang on 2020/05/12
 */
@Service
public class ReportTndustryServiceImpl implements ReportTndustryService {

    @Autowired
    private ReportTndustryMapper reportTndustryMapper;


    @Override
    public List<Map<String, Object>> getTndustryTaxData1All(PageData pd) {
        return reportTndustryMapper.getTndustryTaxData1All(pd);
    }

    @Override
    public List<Map<String, Object>> getTndustryTaxData1(PageData pd) {
        return reportTndustryMapper.getTndustryTaxData1(pd);
    }

    @Override
    public List<Map<String, Object>> getTndustryTaxData(PageData pd) {
        return reportTndustryMapper.getTndustryTaxData(pd);
    }

    @Override
    public List<Map<String, Object>> getTndustryTaxDataAll(PageData pd) {
        return reportTndustryMapper.getTndustryTaxDataAll(pd);
    }

    @Override
    public List<Map<String, Object>> getAllSubject() {
        return reportTndustryMapper.getAllSubject();
    }

    @Override
    public Integer countTndustryTaxData(PageData pd) {
        return reportTndustryMapper.countTndustryTaxData(pd);
    }

    @Override
    public Integer countTndustryTaxData1(PageData pd) {
        return reportTndustryMapper.countTndustryTaxData1(pd);
    }

    @Override
    public List<Map<String, Object>> getTndustryTaxAll(PageData pd) {
        return reportTndustryMapper.getTndustryTaxAll(pd);
    }

    @Override
    public List<Map<String, Object>> getColALL(PageData pd) {
        return reportTndustryMapper.getColALL(pd);
    }

    @Override
    public List<Map<String, Object>> getColHeB(PageData pd) {
        return reportTndustryMapper.getColHeB(pd);
    }
    @Override
    public List<Map<String, Object>> getColHeBF(PageData pd) {
        return reportTndustryMapper.getColHeBF(pd);
    }
}
