package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.ReportMapper;
import org.fixedReport.service.ReportService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dang on 2020/05/12
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public List<Map<String, Object>> getTableParams(PageData params) {
        return reportMapper.getTableParams(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams2(PageData params) {
        return reportMapper.getTableParams2(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams3(PageData params) {
        return reportMapper.getTableParams3(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams(PageData params) {
        return reportMapper.getGraphParams(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams2(PageData params) {
        return reportMapper.getGraphParams2(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams3(PageData params) {
        return reportMapper.getGraphParams3(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams4(PageData params) {
        return reportMapper.getGraphParams4(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams5(PageData params) {
        return reportMapper.getGraphParams5(params);
    }

    @Override
    public List<Map<String, Object>> getAreaParams(PageData params) {
        return reportMapper.getAreaParams(params);
    }

    @Override
    public List<Map<String, Object>> getNumParams(PageData params) {
        return reportMapper.getNumParams(params);
    }


    @Override
    public void insertFact(Map params) {
        reportMapper.insertFact(params);
    }

    @Override
    public void updateEntityReport(Map params) {
        reportMapper.updateEntityReport(params);
    }

    @Override
    public List<Map<String, Object>> getAreaReport(PageData params) {
        return reportMapper.getAreaReport(params);
    }

    @Override
    public List<Map<String, Object>> getTextAndParams(PageData params) {
        return reportMapper.getTextAndParams(params);
    }

    @Override
    public Map<String, Object> getHtmlReport(PageData params) {
        return reportMapper.getHtmlReport(params);
    }

    @Override
    public Map<String, Object> getMonthlyReport(PageData params) {
        return reportMapper.getMonthlyReport(params);
    }
}
