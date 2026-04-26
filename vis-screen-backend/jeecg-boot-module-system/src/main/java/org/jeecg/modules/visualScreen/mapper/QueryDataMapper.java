package org.jeecg.modules.visualScreen.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QueryDataMapper {

    PageSub getPageSub(@Param("params") PageData pd);

    List<PageWhere> getPageWhere(@Param("params") PageData pd);

    List<Map<String, Object>> getStructure(@Param("params") PageData pd);

    List<Map<String, Object>> getTransferIncome(@Param("params") PageData pd);

    List<Map<String, Object>> getTransferIncomeRate(@Param("params") PageData pd);

    List<Map<String, Object>> getLocalFinancial(@Param("params") PageData pd);

    List<Map<String, Object>> getLocalFinancialRate(@Param("params") PageData pd);

    List<Map<String, Object>> getLandTransfer(@Param("params") PageData pd);

    List<Map<String, Object>> getLandTransferRate(@Param("params") PageData pd);

    List<Map<String, Object>> getPublicBudget(@Param("params") PageData pd);

    List<Map<String, Object>> getPublicBudgetRate(@Param("params") PageData pd);

    List<Map<String, Object>> getThreeBudget(@Param("params") PageData pd);

    List<Map<String, Object>> getThreeBudgetRate(@Param("params") PageData pd);

    List<Map<String, Object>> getTaxRevenue(@Param("params") PageData pd);

    List<Map<String, Object>> getTaxRevenueRate(@Param("params") PageData pd);

    List<Map<String, Object>> getIncomePayoutGap(@Param("params") PageData pd);

    List<Map<String, Object>> getIndustryTax(@Param("params") PageData pd);

    List<Map<String, Object>> getMunicipalitiesDirectly(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsRevenue(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsRevenueRate(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsNonTax(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsNonTaxRate(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsImportDuties(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsImportDutiesRate(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsImportVat(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsImportVatRate(@Param("params") PageData pd);

    List<Map<String, Object>> getImportDutyArticles(@Param("params") PageData pd);

    List<Map<String, Object>> getImportDutyArticlesRate(@Param("params") PageData pd);

    List<Map<String, Object>> getBudgetIncomeTop5(@Param("params") PageData pd);

    List<Map<String, Object>> getIndustryType(@Param("params") PageData pd);

    List<Map<String, Object>> getIndustryName(@Param("params") PageData pd);

    List<Map<String, Object>> getIndustryTop10(@Param("params") PageData pd);

    List<Map<String, Object>> getIndustryMain(@Param("params") PageData pd);

    List<Map<String, Object>> getTreasuryIndex(@Param("params") PageData pd);

    List<Map<String, Object>> getSubjectPay(@Param("params") PageData pd);

    List<Map<String, Object>> getSubjectPaySub(@Param("params") PageData pd);

    List<Map<String, Object>> getPurposePay(@Param("params") PageData pd);

    List<Map<String, Object>> getBudgetaryIncome(@Param("params") PageData pd);

    List<Map<String, Object>> getTypeBudget(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryBalance(@Param("params") PageData pd);

    List<Map<String, Object>> getInventoryForm(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsIncomeSituation(@Param("params") PageData pd);

    List<Map<String, Object>> getCustomsIncomeSituationTb(@Param("params") PageData pd);

    List<Map<String, Object>> getAccountToGuoku(@Param("params") PageData  pd);

    List<Map<String, Object>> getGuokuToAccount(@Param("params") PageData  pd);

    List<Map<String, Object>> getGuokuToOrg(@Param("params") PageData  pd);

    List<Map<String, Object>> getOrgToGuoku(@Param("params") PageData  pd);

}
