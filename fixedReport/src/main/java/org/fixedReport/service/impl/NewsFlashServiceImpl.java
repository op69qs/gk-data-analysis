package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.NewsFlashMapper;
import org.fixedReport.service.NewsFlashService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dang on 2020/05/12
 */
@Service
public class NewsFlashServiceImpl implements NewsFlashService {

    @Autowired
    private NewsFlashMapper newsFlashMapper;

    @Override
    public List<Map<String, Object>> getTableParams(PageData params) {
        return newsFlashMapper.getTableParams(params);
    }
    @Override
    public void insertFact(Map params) {
        newsFlashMapper.insertFact(params);
    }
    @Override
    public List<Map<String, Object>> getTableParams2(PageData params) {
        return newsFlashMapper.getTableParams2(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams3(PageData params) {
        return newsFlashMapper.getTableParams3(params);
    }


    @Override
    public void addEntityReport(PageData pd) {
        newsFlashMapper.addEntityReport(pd);
    }

    @Override
    public boolean getNameIsExist(PageData pd) {
        int  nameIsExist=newsFlashMapper.getNameIsExist(pd);
        return  nameIsExist>0?false:true;
    }

    @Override
    public void delEntityReport(PageData pd) {
        newsFlashMapper.delEntityReport(pd);
    }

    @Override
    public void delEntityReportFact(PageData pd) {
        newsFlashMapper.delEntityReportFact(pd);
    }

    @Override
    public void editEntityReport(PageData pd) {
        newsFlashMapper.editEntityReport(pd);
    }

    @Override
    public List<Map<String, Object>> getReportAll(PageData pd) {
        return newsFlashMapper.getReportAll(pd);
    }

    @Override
    public Integer countReportAll(PageData pd) {
        return newsFlashMapper.countReportAll(pd);
    }

    @Override
    public List<Map<String, Object>> getTextAndParams(PageData params) {
        return newsFlashMapper.getTextAndParams(params);
    }
}
