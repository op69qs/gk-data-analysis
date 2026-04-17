package org.seo.service.impl;

import org.seo.config.DataSourceContextHolder;
import org.seo.dao.mapper.DataSourceInfoMapper;
import org.seo.dao.mapper.DataSourceMapper;
import org.seo.model.DataSourceInfoEntity;
import org.seo.service.DataSourceInfoService;
import org.seo.service.DataSourceService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataSourceInfoServiceImpl implements DataSourceInfoService {


    @Autowired
    private DataSourceInfoMapper dataSourceInfoMapper;

    @Override
    public List<DataSourceInfoEntity> getList() {
        DataSourceContextHolder.setDBType("default");
        return dataSourceInfoMapper.getList();
    }


}
