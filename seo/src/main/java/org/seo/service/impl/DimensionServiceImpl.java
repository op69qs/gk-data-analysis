package org.seo.service.impl;

import org.seo.config.DataSourceContextHolder;
import org.seo.dao.mapper.DimensionMapper;
import org.seo.service.DimensionService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;

    @Override
    public List<Map<String, Object>> getMainPage(PageData pd) {

        return dimensionMapper.getMainPage(pd);
    }

    @Override
    public List<Map<String, Object>> getMainAll(PageData pd) {

        return dimensionMapper.getMainAll(pd);
    }

    @Override
    public List<Map<String, Object>> checkMain(PageData pd) {
        return dimensionMapper.checkMain(pd);
    }

    @Override
    public void addMain(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.addMain(pd);
    }

    @Override
    public void editMain(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.editMain(pd);
    }

    @Override
    public void delMain(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.delMain(pd);
    }

    @Override
    public Integer countMain(PageData pd) {
        return dimensionMapper.countMain(pd);
    }

    @Override
    public List<Map<String, Object>> getSubPage(PageData pd) {
        return dimensionMapper.getSubPage(pd);
    }

    @Override
    public List<Map<String, Object>> getSubAll(PageData pd) {
        return dimensionMapper.getSubAll(pd);
    }

    @Override
    public List<Map<String, Object>> checkSub(PageData pd) {
        return dimensionMapper.checkSub(pd);
    }

    @Override
    public void addSub(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.addSub(pd);
    }

    @Override
    public void editSub(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.editSub(pd);
    }

    @Override
    public void delSub(PageData pd, String dataSource_id) {
        DataSourceContextHolder.setDBType(dataSource_id);
        dimensionMapper.delSub(pd);
    }

    @Override
    public Integer countSub(PageData pd) {
        return dimensionMapper.countSub(pd);
    }
}
