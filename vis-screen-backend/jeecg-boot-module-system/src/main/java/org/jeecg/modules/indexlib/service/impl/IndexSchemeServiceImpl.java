package org.jeecg.modules.indexlib.service.impl;

import org.jeecg.modules.indexlib.mapper.IndexSchemeMapper;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
import org.jeecg.modules.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexSchemeServiceImpl implements IndexSchemeService {

    @Autowired
    private IndexSchemeMapper indexSchemeMapper;

    @Override
    public String getAllTrsInfo() {
        return indexSchemeMapper.getAllTrsInfo();
    }

    @Override
    public String getAllAreaInfo() {
        return indexSchemeMapper.getAllAreaInfo();
    }

    @Override
    public List<Map<String, Object>> execSchemeSql(PageData pageData) {
        return indexSchemeMapper.execSchemeSql(pageData);
    }

    @Override
    public Map<String, String> getSchemeInfoById(PageData pageData) {
        return indexSchemeMapper.getSchemeInfoById(pageData);
    }

    @Override
    public int getSchemeCount(PageData pageData) {
        return indexSchemeMapper.getSchemeCount(pageData);
    }

    @Override
    public List<Map<String, Object>> selectSchemeTable(PageData pageData) {
        return indexSchemeMapper.selectSchemeTable(pageData);
    }

    @Override
    public void deleteSchemeById(Map<String, Object> params) {
        indexSchemeMapper.deleteSchemeById(params);
    }

    @Override
    public List<Map<String, String>> getIndexNames(PageData pageData) {
        return indexSchemeMapper.getIndexNames(pageData);
    }
}
