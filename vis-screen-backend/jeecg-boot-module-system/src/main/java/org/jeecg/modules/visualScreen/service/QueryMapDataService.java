package org.jeecg.modules.visualScreen.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import java.util.List;
import java.util.Map;

public interface QueryMapDataService {

    List<Map<String, Object>> getIncome(PageData pd);
    List<Map<String, Object>> getIncomeRate(PageData pd);
    List<Map<String, Object>> getPayOut(PageData pd);
    List<Map<String, Object>> getPayOutRate(PageData pd);
    List<Map<String, Object>> getBudgetRevenue(PageData pd);

    List<Map<String, Object>>  getBudgetRevenueTitle(PageData pd);

    List<Map<String, Object>>  getBudgetRevenueRateTitle(PageData pd);

    List<Map<String, Object>> getBudgetRevenueRate(PageData pd);

    Map<String, Object> getBudgetRevenueMax(PageData pd);

    List<Map<String, Object>> getRevenueDisplay(PageData pd);

    Map<String, Object> getRevenueDisplayMax(PageData pd);

    List<Map<String, Object>> getEconomicZone(PageData pd);
    List<Map<String, Object>> getEconomicZonePay(PageData pd);
    List<Map<String, Object>> getEconomicZonePayRate(PageData pd);


    Map<String, Object> getEconomicZoneMax(PageData pd);

    List<Map<String, Object>> getEconomicTaxation(PageData pd);

    Map<String, Object> getEconomicTaxationMax(PageData pd);

    List<Map<String, Object>> getEconomicPay(PageData pd);

    List<Map<String, Object>> getEconomicPayTitle(PageData pd);

    List<Map<String, Object>> getEconomicPayRateTitle(PageData pd);

    Map<String, Object> getEconomicPayMax(PageData pd);

    List<Map<String, Object>> getInventoryBalance(PageData pd);
    List<Map<String, Object>> getInventoryBalanceRate(PageData pd);

    List<Map<String, Object>> getInventoryBalanceTitle(PageData pd);
    List<Map<String, Object>> getInventoryBalanceRateTitle(PageData pd);


    Map<String, Object> getInventoryBalanceMax(PageData pd);
    List<Map<String, Object>> getRevenueDisplayRate(PageData pd);
    List<Map<String, Object>> getEconomicZoneRate(PageData pd);
    List<Map<String, Object>> getEconomicTaxationRate(PageData pd);
    List<Map<String, Object>> getEconomicPayRate(PageData pd);

}
