package org.jeecg.modules.visualScreen.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;

import java.util.List;
import java.util.Map;

public interface QueryTableDataService {

    List<Map<String, Object>> getGrowthBudget(PageData pd);

    List<Map<String, Object>> getGrowthBudgetRate(PageData pd);

    List<Map<String, Object>> getGrowthTax(PageData pd);

    List<Map<String, Object>> getGrowthTaxRate(PageData pd);

    List<Map<String, Object>> getAmountRegion(PageData pd);

    List<Map<String, Object>> getAmountRegionCQ(PageData pd);

    List<Map<String, Object>> getRankingGrowth(PageData pd);

    List<Map<String, Object>> getTaxRegional(PageData pd);

    List<Map<String, Object>> getTaxRegion(PageData pd);

    List<Map<String, Object>> getFiveProvinces(PageData pd);

    List<Map<String, Object>> getMunicipalities(PageData pd);

    List<Map<String, Object>> getRegionPay(PageData pd);

    List<Map<String, Object>> getRegionPayRate(PageData pd);

    List<Map<String, Object>> getInventoryBalance(PageData pd);

    List<Map<String, Object>> getInventoryBalanceRate(PageData pd);
}
