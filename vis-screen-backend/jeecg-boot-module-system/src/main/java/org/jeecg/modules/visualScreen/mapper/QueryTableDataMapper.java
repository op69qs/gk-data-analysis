package org.jeecg.modules.visualScreen.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryTableDataMapper {

    List<Map<String, Object>> getGrowthBudget(@Param("params") PageData pd);

    List<Map<String, Object>> getGrowthBudgetRate(@Param("params") PageData pd);

    List<Map<String, Object>> getGrowthTax(@Param("params") PageData pd);

    List<Map<String, Object>> getGrowthTaxRate(@Param("params") PageData pd);

    List<Map<String, Object>> getAmountRegion(@Param("params") PageData pd);

    List<Map<String, Object>> getAmountRegionCQ(@Param("params") PageData pd);

    List<Map<String, Object>> getRankingGrowth(@Param("params") PageData pd);

    List<Map<String, Object>> getTaxRegional(@Param("params") PageData pd);

    List<Map<String, Object>> getTaxRegion(@Param("params") PageData pd);

    List<Map<String, Object>> getFiveProvinces(@Param("params") PageData pd);

    List<Map<String, Object>> getMunicipalities(@Param("params") PageData pd);

    List<Map<String, Object>> getRegionPay(@Param("params") PageData pd);

    List<Map<String, Object>> getRegionPayRate(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalance(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalanceRate(@Param("params") PageData pd);

}
