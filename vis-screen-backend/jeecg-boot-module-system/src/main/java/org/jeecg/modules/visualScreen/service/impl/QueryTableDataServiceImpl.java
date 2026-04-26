package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.QueryTableDataMapper;
import org.jeecg.modules.visualScreen.service.QueryTableDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryTableDataServiceImpl implements QueryTableDataService {

    @Autowired
    private QueryTableDataMapper queryTableDataMapper;

    @Override
    public List<Map<String, Object>> getGrowthBudget(PageData pd) {
        return queryTableDataMapper.getGrowthBudget(pd);
    }

    @Override
    public List<Map<String, Object>> getGrowthBudgetRate(PageData pd) {
        return queryTableDataMapper.getGrowthBudgetRate(pd);
    }

    @Override
    public List<Map<String, Object>> getGrowthTax(PageData pd) {
        return queryTableDataMapper.getGrowthTax(pd);
    }

    @Override
    public List<Map<String, Object>> getGrowthTaxRate(PageData pd) {
        return queryTableDataMapper.getGrowthTaxRate(pd);
    }

    @Override
    public List<Map<String, Object>> getAmountRegion(PageData pd) {
        return queryTableDataMapper.getAmountRegion(pd);
    }

    @Override
    public List<Map<String, Object>> getAmountRegionCQ(PageData pd) {
        return queryTableDataMapper.getAmountRegionCQ(pd);
    }

    @Override
    public List<Map<String, Object>> getRankingGrowth(PageData pd) {
        return queryTableDataMapper.getRankingGrowth(pd);
    }

    @Override
    public List<Map<String, Object>> getTaxRegional(PageData pd) {
        return queryTableDataMapper.getTaxRegional(pd);
    }

    @Override
    public List<Map<String, Object>> getTaxRegion(PageData pd) {
        return queryTableDataMapper.getTaxRegion(pd);
    }

    @Override
    public List<Map<String, Object>> getFiveProvinces(PageData pd) {
        return queryTableDataMapper.getFiveProvinces(pd);
    }

    @Override
    public List<Map<String, Object>> getMunicipalities(PageData pd) {
        return queryTableDataMapper.getMunicipalities(pd);
    }

    @Override
    public List<Map<String, Object>> getRegionPay(PageData pd) {
        return queryTableDataMapper.getRegionPay(pd);
    }

    @Override
    public List<Map<String, Object>> getRegionPayRate(PageData pd) {
        return queryTableDataMapper.getRegionPayRate(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalance(PageData pd) {
        return queryTableDataMapper.getInventoryBalance(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalanceRate(PageData pd) {
        return queryTableDataMapper.getInventoryBalanceRate(pd);
    }
}
