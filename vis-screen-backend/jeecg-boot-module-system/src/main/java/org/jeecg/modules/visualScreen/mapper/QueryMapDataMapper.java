package org.jeecg.modules.visualScreen.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryMapDataMapper {

    List<Map<String, Object>> getIncome(@Param("params") PageData pd);
    List<Map<String, Object>> getIncomeRate(@Param("params") PageData pd);
    List<Map<String, Object>> getPayOut(@Param("params") PageData pd);
    List<Map<String, Object>> getPayOutRate(@Param("params") PageData pd);
    List<Map<String, Object>> getBudgetRevenue(@Param("params") PageData pd);
    List<Map<String, Object>>  getBudgetRevenueTitle(@Param("params") PageData pd);
    List<Map<String, Object>>  getBudgetRevenueRateTitle(@Param("params") PageData pd);
    List<Map<String, Object>> getBudgetRevenueRate(@Param("params") PageData pd);

    Map<String, Object> getBudgetRevenueMax(@Param("params") PageData pd);

    List<Map<String, Object>> getRevenueDisplay(@Param("params") PageData pd);

    Map<String, Object> getRevenueDisplayMax(@Param("params") PageData pd);

    List<Map<String, Object>> getEconomicZone(@Param("params") PageData pd);
    List<Map<String, Object>> getEconomicZonePay(@Param("params") PageData pd);
    List<Map<String, Object>> getEconomicZonePayRate(@Param("params") PageData pd);

    Map<String, Object> getEconomicZoneMax(@Param("params") PageData pd);

    List<Map<String, Object>> getEconomicTaxation(@Param("params") PageData pd);

    Map<String, Object> getEconomicTaxationMax(@Param("params") PageData pd);

    List<Map<String, Object>> getEconomicPay(@Param("params") PageData pd);

    List<Map<String, Object>> getEconomicPayTitle(@Param("params") PageData pd);

    List<Map<String, Object>> getEconomicPayRateTitle(@Param("params") PageData pd);

    Map<String, Object> getEconomicPayMax(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalance(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalanceRate(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalanceTitle(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalanceRateTitle(@Param("params") PageData pd);

    Map<String, Object> getInventoryBalanceMax(@Param("params") PageData pd);
    List<Map<String, Object>> getRevenueDisplayRate(@Param("params") PageData  pd);
    List<Map<String, Object>> getEconomicZoneRate(@Param("params") PageData  pd);
    List<Map<String, Object>> getEconomicTaxationRate(@Param("params") PageData  pd);
    List<Map<String, Object>> getEconomicPayRate(@Param("params") PageData  pd);

}
