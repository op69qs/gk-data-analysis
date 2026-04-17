package org.seo.service.impl;

import org.seo.config.DataSourceContextHolder;
import org.seo.dao.mapper.ForSkipMapper;
import org.seo.service.ForSkipService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ForSkipServiceImpl implements ForSkipService {

    @Autowired
    private ForSkipMapper forSkipMapper;


    @Override
    public List<Map<String, Object>> getFileList(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        return forSkipMapper.getFileList(pd);
    }
}
