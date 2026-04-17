package org.seo.service.impl;

import org.seo.dao.mapper.DataSourceMapper;
import org.seo.service.DataSourceService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;


    @Override
    public List<Map<String, Object>> getDataSourcePage(PageData pd) {
        return dataSourceMapper.getDataSourcePage(pd);
    }

    @Override
    public List<Map<String, Object>> getDataSource(PageData pd) {
        return dataSourceMapper.getDataSource(pd);
    }

    @Override
    public void addDataSource(PageData pd) {
        dataSourceMapper.addDataSource(pd);
    }

    @Override
    public void delDataSource(PageData pd) {
        dataSourceMapper.delDataSource(pd);
    }

    @Override
    public void editDataSource(PageData pd) {
        dataSourceMapper.editDataSource(pd);
    }

    @Override
    public Integer countDataSource(PageData pd) {
        return dataSourceMapper.countDataSource(pd);
    }

    @Override
    public List<Map<String, Object>> getDataSourceEnum(PageData pd) {
        return dataSourceMapper.getDataSourceEnum(pd);
    }

    @Override
    public List<Map<String, Object>> getDataSourceEnumSelect() {
        return dataSourceMapper.getDataSourceEnumSelect();
    }

    @Override
    public List<Map<String, Object>> getDataBase(PageData pd) {
        return dataSourceMapper.getDataBase(pd);
    }

    @Override
    public void addDataBase(PageData pd) {
        dataSourceMapper.addDataBase(pd);
    }

    @Override
    public void delDataBase(PageData pd) {
        dataSourceMapper.delDataBase(pd);
    }

    @Override
    public List<Map<String, Object>> getDataSourceName(PageData pd) {
        return dataSourceMapper.getDataSourceName(pd);
    }
}
