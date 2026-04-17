package org.fixedReport.service.impl;

import org.fixedReport.dao.mapper.fixedReport.NewsFlashQuarterMapper;
import org.fixedReport.service.NewsFlashQuarterService;
import org.fixedReport.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Created by dang on 2020/05/12
 */
@Service
public class NewsFlashQuarterServiceImpl implements NewsFlashQuarterService {

    @Autowired
    private NewsFlashQuarterMapper newsFlashQuarterMapper;

    @Override
    public List<Map<String, Object>> getTableParams(PageData params) {
        return newsFlashQuarterMapper.getTableParams(params);
    }
    @Override
    public List<Map<String, Object>> getTableParams2(PageData params) {
        return newsFlashQuarterMapper.getTableParams2(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams3(PageData params) {
        return newsFlashQuarterMapper.getTableParams3(params);
    }

    @Override
    public List<Map<String, Object>> getTableParams4(PageData params) {
        return newsFlashQuarterMapper.getTableParams4(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams(PageData params) {
        return newsFlashQuarterMapper.getGraphParams(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams2(PageData params) {
        return newsFlashQuarterMapper.getGraphParams2(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams3(PageData params) {
        return newsFlashQuarterMapper.getGraphParams3(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams4(PageData params) {
        return newsFlashQuarterMapper.getGraphParams4(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams5(PageData params) {
        return newsFlashQuarterMapper.getGraphParams5(params);
    }

    @Override
    public List<Map<String, Object>> getGraphParams6(PageData params) {
        return newsFlashQuarterMapper.getGraphParams6(params);
    }

    @Override
    public List<Map<String, Object>> getTextAndParams(PageData params) {
        return newsFlashQuarterMapper.getTextAndParams(params);
    }
}
