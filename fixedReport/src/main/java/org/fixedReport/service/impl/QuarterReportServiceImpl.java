package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.QuarterReportMapper;
import org.fixedReport.service.QuarterReportService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class QuarterReportServiceImpl implements QuarterReportService {

    @Autowired
    private QuarterReportMapper quarterReportMapper;

    @Override
    public List<Map<String, Object>> getTableParams(PageData params) {
        return quarterReportMapper.getTableParams(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams2(PageData params) {
        return quarterReportMapper.getTableParams2(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams3(PageData params) {
        return quarterReportMapper.getTableParams3(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams4(PageData params) {
        return quarterReportMapper.getTableParams4(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams(PageData params) {
        return quarterReportMapper.getGraphParams(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams2(PageData params) {
        return quarterReportMapper.getGraphParams2(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams3(PageData params) {
        return quarterReportMapper.getGraphParams3(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams4(PageData params) {
        return quarterReportMapper.getGraphParams4(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams5(PageData params) {
        return quarterReportMapper.getGraphParams5(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams6(PageData params) {
        return quarterReportMapper.getGraphParams6(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams7(PageData params) {
        return quarterReportMapper.getGraphParams7(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams8(PageData params) {
        return quarterReportMapper.getGraphParams8(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams9(PageData params) {
        return quarterReportMapper.getGraphParams9(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams10(PageData params) {
        return quarterReportMapper.getGraphParams10(params);
    }

    @Override
    public void addEntityReport(PageData pd) {
        quarterReportMapper.addEntityReport(pd);
    }

    @Override
    public void delEntityReport(PageData pd) {
        quarterReportMapper.delEntityReport(pd);
    }

    @Override
    public void editEntityReport(PageData pd) {
        quarterReportMapper.editEntityReport(pd);
    }

    @Override
    public List<Map<String, Object>> getReportAll(PageData pd) {
        return quarterReportMapper.getReportAll(pd);
    }

    @Override
    public Integer countReportAll(PageData pd) {
        return quarterReportMapper.countReportAll(pd);
    }

    @Override
    public List<Map<String, Object>> getTextAndParams(PageData params) {
        return quarterReportMapper.getTextAndParams(params);
    }
}
