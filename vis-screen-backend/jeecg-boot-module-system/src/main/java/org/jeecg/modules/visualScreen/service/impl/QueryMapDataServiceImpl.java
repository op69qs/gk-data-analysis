package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.QueryMapDataMapper;
import org.jeecg.modules.visualScreen.service.QueryMapDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryMapDataServiceImpl implements QueryMapDataService {

    @Autowired
    private QueryMapDataMapper queryMapDataMapper;

    @Override
    public List<Map<String, Object>> getIncome(PageData pd) {
        return queryMapDataMapper.getIncome(pd);
    }

    @Override
    public List<Map<String, Object>> getIncomeRate(PageData pd) {
        return queryMapDataMapper.getIncomeRate(pd);
    }

    @Override
    public List<Map<String, Object>> getPayOut(PageData pd) {
        return queryMapDataMapper.getPayOut(pd);
    }

    @Override
    public List<Map<String, Object>> getPayOutRate(PageData pd) {
        return queryMapDataMapper.getPayOutRate(pd);
    }

    @Override
    public List<Map<String, Object>> getBudgetRevenue(PageData pd) {
        return queryMapDataMapper.getBudgetRevenue(pd);
    }

    @Override
    public List<Map<String, Object>>  getBudgetRevenueTitle(PageData pd) {
        return queryMapDataMapper.getBudgetRevenueTitle(pd);
    }

    @Override
    public List<Map<String, Object>> getBudgetRevenueRateTitle(PageData pd) {
        return queryMapDataMapper.getBudgetRevenueRateTitle(pd);
    }

    @Override
    public List<Map<String, Object>> getBudgetRevenueRate(PageData pd) {
        return queryMapDataMapper.getBudgetRevenueRate(pd);
    }

    @Override
    public Map<String, Object> getBudgetRevenueMax(PageData pd) {
        return queryMapDataMapper.getBudgetRevenueMax(pd);
    }

    @Override
    public List<Map<String, Object>> getRevenueDisplay(PageData pd) {
        return queryMapDataMapper.getRevenueDisplay(pd);
    }

    @Override
    public Map<String, Object> getRevenueDisplayMax(PageData pd) {
        return queryMapDataMapper.getRevenueDisplayMax(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicZone(PageData pd) {
        return queryMapDataMapper.getEconomicZone(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicZonePay(PageData pd) {
        return queryMapDataMapper.getEconomicZonePay(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicZonePayRate(PageData pd) {
        return queryMapDataMapper.getEconomicZonePayRate(pd);
    }

    @Override
    public Map<String, Object> getEconomicZoneMax(PageData pd) {
        return queryMapDataMapper.getEconomicZoneMax(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicTaxation(PageData pd) {
        return queryMapDataMapper.getEconomicTaxation(pd);
    }

    @Override
    public Map<String, Object> getEconomicTaxationMax(PageData pd) {
        return queryMapDataMapper.getEconomicTaxationMax(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicPay(PageData pd) {
        return queryMapDataMapper.getEconomicPay(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicPayTitle(PageData pd) {
        return queryMapDataMapper.getEconomicPayTitle(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicPayRateTitle(PageData pd) {
        return queryMapDataMapper.getEconomicPayRateTitle(pd);
    }

    @Override
    public Map<String, Object> getEconomicPayMax(PageData pd) {
        return queryMapDataMapper.getEconomicPayMax(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalance(PageData pd){
        return queryMapDataMapper.getInventoryBalance(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalanceRate(PageData pd) {
        return queryMapDataMapper.getInventoryBalanceRate(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalanceTitle(PageData pd) {
        return queryMapDataMapper.getInventoryBalanceTitle(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalanceRateTitle(PageData pd) {
        return queryMapDataMapper.getInventoryBalanceRateTitle(pd);
    }

    @Override
    public Map<String, Object> getInventoryBalanceMax(PageData pd){
        return queryMapDataMapper.getInventoryBalanceMax(pd);
    }

    @Override
    public List<Map<String, Object>> getRevenueDisplayRate(PageData pd) {
        return queryMapDataMapper.getRevenueDisplayRate(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicZoneRate(PageData pd) {
        return queryMapDataMapper.getEconomicZoneRate(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicTaxationRate(PageData pd) {
        return queryMapDataMapper.getEconomicTaxationRate(pd);
    }

    @Override
    public List<Map<String, Object>> getEconomicPayRate(PageData pd) {
        return queryMapDataMapper.getEconomicPayRate(pd);
    }
}
