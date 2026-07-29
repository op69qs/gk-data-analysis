package org.seo.service.impl;

import org.aspectj.lang.annotation.Before;
import org.seo.config.DataSourceContextHolder;
import org.seo.dao.mapper.DataAuxiliaryMapper;
import org.seo.service.DataAuxiliaryService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataAuxiliaryServiceImpl implements DataAuxiliaryService {

    @Autowired
    private DataAuxiliaryMapper dataAuxiliaryMapper;

    @Override
    public List<Map<String, Object>> getFirstClassifySelection() {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getFirstClassifySelection();
    }

    @Override
    public List<Map<String, Object>> getSecondClassifySelection(PageData pd) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getSecondClassifySelection(pd);
    }

    @Override
    public List<Map<String, Object>> getDataSourceSelection() {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getDataSourceSelection();
    }

    @Override
    public List<Map<String, Object>> getDataBaseSelection(PageData pageData) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getDataBaseSelection(pageData);
    }

    @Override
    public List<Map<String, Object>> getDataBaseInfo(PageData pageData) {
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getDataBaseInfo(pageData);
    }

    @Override
    public List<Map<String, Object>> getDataTableSelection(PageData pageData) {
        //动态切换数据源
        DataSourceContextHolder.setDBType(pageData.getString("SOURCE_ID"));
        List<Map<String, Object>> dataList = dataAuxiliaryMapper.getDataTableSelection(pageData);
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getDataTableComments(PageData pageData) {
        //动态切换数据源
        DataSourceContextHolder.setDBType(pageData.getString("SOURCE_ID"));
        List<Map<String, Object>> dataList = dataAuxiliaryMapper.getDataTableComments(pageData);
        return dataList;
    }

    @Override
    public List<Map<String, Object>> getDataSourceInfo(Map<String, Object> map) {
        DataSourceContextHolder.setDBType("default");
        return dataAuxiliaryMapper.getDataSourceInfo(map);
    }
}
