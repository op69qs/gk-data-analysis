package org.seo.service.impl;

import org.seo.config.DataSourceContextHolder;
import org.seo.dao.mapper.ComprehensiveQueryMapper;
import org.seo.service.ComprehensiveQueryService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ComprehensiveQueryServiceImpl implements ComprehensiveQueryService {

    @Autowired
    private ComprehensiveQueryMapper comprehensiveQueryMapper;
    @Override
    public List<Map<String, Object>> getTableName(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getTableName(pd);
    }

    @Override
    public List<Map<String, Object>> getColumn(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getColumn(pd);
    }

    @Override
    public Map<String,String> getType(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getType(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeMain(pd);
    }

    @Override
    public List<Map<String, Object>> checkScheme(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.checkScheme(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeMainPage(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeMainPage(pd);
    }

    @Override
    public void addSchemeMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeMain(pd);
    }

    @Override
    public void editSchemeMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.editSchemeMain(pd);
    }

    @Override
    public void delSchemeMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeMain(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeTable(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeTable(pd);
    }

    @Override
    public void addSchemeTable(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeTable(pd);
    }

    @Override
    public void delSchemeTable(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeTable(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeColumn(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeColumn(pd);
    }

    @Override
    public void addSchemeColumn(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeColumn(pd);
    }

    @Override
    public void delSchemeColumn(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeColumn(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeWhere(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeWhere(pd);
    }

    @Override
    public void addSchemeWhere(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeWhere(pd);
    }

    @Override
    public void delSchemeWhere(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeWhere(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeOrder(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeOrder(pd);
    }

    @Override
    public void addSchemeOrder(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeOrder(pd);
    }

    @Override
    public void delSchemeOrder(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeOrder(pd);
    }

    @Override
    public Integer countMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.countMain(pd);
    }

    @Override
    public List<Map<String, Object>> getRelationMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getRelationMain(pd);
    }

    @Override
    public void addRelationMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addRelationMain(pd);
    }

    @Override
    public void delRelationMain(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delRelationMain(pd);
    }

    @Override
    public List<Map<String, Object>> getRelationSub(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getRelationSub(pd);
    }

    @Override
    public void addRelationSub(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addRelationSub(pd);
    }

    @Override
    public void delRelationSub(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delRelationSub(pd);
    }

    @Override
    public List<Map<String, Object>> getSchemeIndication(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        return comprehensiveQueryMapper.getSchemeIndication(pd);
    }

    @Override
    public void addSchemeIndication(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.addSchemeIndication(pd);
    }

    @Override
    public void delSchemeIndication(PageData pd) {
        DataSourceContextHolder.setDBType("default");
        comprehensiveQueryMapper.delSchemeIndication(pd);
    }

    @Override
    public List<Map<String, Object>> executeSql(PageData pd,String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        return comprehensiveQueryMapper.executeSql(pd);
    }

    @Override
    public Integer countSql(PageData pd,String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        return comprehensiveQueryMapper.countSql(pd);
    }
}
