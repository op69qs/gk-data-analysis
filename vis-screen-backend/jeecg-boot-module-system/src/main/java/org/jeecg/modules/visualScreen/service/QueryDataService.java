package org.jeecg.modules.visualScreen.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;

import java.util.List;
import java.util.Map;

public interface QueryDataService {

    PageSub getPageSub(PageData pd);

    List<PageWhere> getPageWhere(PageData pd);

    List<Map<String, Object>> getStructure(PageData pd);

    List<Map<String, Object>> getTransferIncome(PageData pd);

    List<Map<String, Object>> getTransferIncomeRate(PageData pd);

    List<Map<String, Object>> getLocalFinancial(PageData pd);

    List<Map<String, Object>> getLocalFinancialRate(PageData pd);

    List<Map<String, Object>> getLandTransfer(PageData pd);

    List<Map<String, Object>> getLandTransferRate(PageData pd);

    List<Map<String, Object>> getPublicBudget(PageData pd);

    List<Map<String, Object>> getPublicBudgetRate(PageData pd);

    List<Map<String, Object>> getThreeBudget(PageData pd);

    List<Map<String, Object>> getThreeBudgetRate(PageData pd);

    List<Map<String, Object>> getTaxRevenue(PageData pd);

    List<Map<String, Object>> getTaxRevenueRate(PageData pd);

    List<Map<String, Object>> getIncomePayoutGap(PageData pd);

    List<Map<String, Object>> getIndustryTax(PageData pd);

    List<Map<String, Object>> getMunicipalitiesDirectly(PageData pd);

    List<Map<String, Object>> getCustomsRevenue(PageData pd);

    List<Map<String, Object>> getCustomsRevenueRate(PageData pd);

    List<Map<String, Object>> getCustomsNonTax(PageData pd);

    List<Map<String, Object>> getCustomsNonTaxRate(PageData pd);

    List<Map<String, Object>> getCustomsImportDuties(PageData pd);

    List<Map<String, Object>> getCustomsImportDutiesRate(PageData pd);

    List<Map<String, Object>> getCustomsImportVat(PageData pd);

    List<Map<String, Object>> getCustomsImportVatRate(PageData pd);

    List<Map<String, Object>> getImportDutyArticles(PageData pd);

    List<Map<String, Object>> getImportDutyArticlesRate(PageData pd);

    List<Map<String, Object>> getBudgetIncomeTop5(PageData pd);

    List<Map<String, Object>> getIndustryType(PageData pd);

    List<Map<String, Object>> getIndustryName(PageData pd);

    List<Map<String, Object>> getIndustryTop10(PageData pd);

    List<Map<String, Object>> getIndustryMain(PageData pd);

    List<Map<String, Object>> getTreasuryIndex(PageData pd);

    List<Map<String, Object>> getSubjectPay(PageData pd);

    List<Map<String, Object>> getSubjectPaySub(PageData pd);

    List<Map<String, Object>> getPurposePay(PageData pd);

    List<Map<String, Object>> getBudgetaryIncome(PageData pd);

    List<Map<String, Object>> getTypeBudget(PageData pd);

    List<Map<String, Object>> getInventoryBalance(PageData pd);

    List<Map<String, Object>> getInventoryForm(PageData pd);

    List<Map<String, Object>> getCustomsIncomeSituation(PageData pd);

    List<Map<String, Object>> getCustomsIncomeSituationTb(PageData pd);

    List<Map<String, Object>> getAccountToGuoku(PageData pd);

    List<Map<String, Object>> getGuokuToAccount(PageData pd);

    List<Map<String, Object>> getGuokuToOrg(PageData pd);

    List<Map<String, Object>> getOrgToGuoku(PageData pd);

}
