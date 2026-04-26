package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.QueryDataMapper;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.jeecg.modules.visualScreen.service.QueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QueryDataServiceimpl implements QueryDataService {

    @Autowired
    private QueryDataMapper queryDataMapper;

    @Override
    public PageSub getPageSub(PageData pd) {
        return queryDataMapper.getPageSub(pd);
    }

    @Override
    public List<PageWhere> getPageWhere(PageData pd) {
        return queryDataMapper.getPageWhere(pd);
    }

    @Override
    public List<Map<String, Object>> getStructure(PageData pd) {
        return queryDataMapper.getStructure(pd);
    }

    @Override
    public List<Map<String, Object>> getTransferIncome(PageData pd) {
        return queryDataMapper.getTransferIncome(pd);
    }

    @Override
    public List<Map<String, Object>> getTransferIncomeRate(PageData pd) {
        return queryDataMapper.getTransferIncomeRate(pd);
    }

    @Override
    public List<Map<String, Object>> getLocalFinancial(PageData pd) {
        return queryDataMapper.getLocalFinancial(pd);
    }

    @Override
    public List<Map<String, Object>> getLocalFinancialRate(PageData pd) {
        return queryDataMapper.getLocalFinancialRate(pd);
    }

    @Override
    public List<Map<String, Object>> getLandTransfer(PageData pd) {
        return queryDataMapper.getLandTransfer(pd);
    }

    @Override
    public List<Map<String, Object>> getLandTransferRate(PageData pd) {
        return queryDataMapper.getLandTransferRate(pd);
    }

    @Override
    public List<Map<String, Object>> getPublicBudget(PageData pd) {
        return queryDataMapper.getPublicBudget(pd);
    }

    @Override
    public List<Map<String, Object>> getPublicBudgetRate(PageData pd) {
        return queryDataMapper.getPublicBudgetRate(pd);
    }

    @Override
    public List<Map<String, Object>> getThreeBudget(PageData pd) {
        return queryDataMapper.getThreeBudget(pd);
    }

    @Override
    public List<Map<String, Object>> getThreeBudgetRate(PageData pd) {
        return queryDataMapper.getThreeBudgetRate(pd);
    }

    @Override
    public List<Map<String, Object>> getTaxRevenue(PageData pd) {
        return queryDataMapper.getTaxRevenue(pd);
    }

    @Override
    public List<Map<String, Object>> getTaxRevenueRate(PageData pd) {
        return queryDataMapper.getTaxRevenueRate(pd);
    }

    @Override
    public List<Map<String, Object>> getIncomePayoutGap(PageData pd) {
        return queryDataMapper.getIncomePayoutGap(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryTax(PageData pd) {
        return queryDataMapper.getIndustryTax(pd);
    }

    @Override
    public List<Map<String, Object>> getMunicipalitiesDirectly(PageData pd) {
        return queryDataMapper.getMunicipalitiesDirectly(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsRevenue(PageData pd) {
        return queryDataMapper.getCustomsRevenue(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsRevenueRate(PageData pd) {
        return queryDataMapper.getCustomsRevenueRate(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsNonTax(PageData pd) {
        return queryDataMapper.getCustomsNonTax(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsNonTaxRate(PageData pd) {
        return queryDataMapper.getCustomsNonTaxRate(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsImportDuties(PageData pd) {
        return queryDataMapper.getCustomsImportDuties(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsImportDutiesRate(PageData pd) {
        return queryDataMapper.getCustomsImportDutiesRate(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsImportVat(PageData pd) {
        return queryDataMapper.getCustomsImportVat(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsImportVatRate(PageData pd) {
        return queryDataMapper.getCustomsImportVatRate(pd);
    }

    @Override
    public List<Map<String, Object>> getImportDutyArticles(PageData pd) {
        return queryDataMapper.getImportDutyArticles(pd);
    }

    @Override
    public List<Map<String, Object>> getImportDutyArticlesRate(PageData pd) {
        return queryDataMapper.getImportDutyArticlesRate(pd);
    }

    @Override
    public List<Map<String, Object>> getBudgetIncomeTop5(PageData pd) {
        return queryDataMapper.getBudgetIncomeTop5(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryType(PageData pd) {
        return queryDataMapper.getIndustryType(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryName(PageData pd) {
        return queryDataMapper.getIndustryName(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryTop10(PageData pd) {
        return queryDataMapper.getIndustryTop10(pd);
    }

    @Override
    public List<Map<String, Object>> getIndustryMain(PageData pd) {
        return queryDataMapper.getIndustryMain(pd);
    }

    @Override
    public List<Map<String, Object>> getTreasuryIndex(PageData pd) {
        return queryDataMapper.getTreasuryIndex(pd);
    }

    @Override
    public List<Map<String, Object>> getSubjectPay(PageData pd) {
        return queryDataMapper.getSubjectPay(pd);
    }

    @Override
    public List<Map<String, Object>> getSubjectPaySub(PageData pd) {
        return queryDataMapper.getSubjectPaySub(pd);
    }

    @Override
    public List<Map<String, Object>> getPurposePay(PageData pd) {
        return queryDataMapper.getPurposePay(pd);
    }

    @Override
    public List<Map<String, Object>> getBudgetaryIncome(PageData pd) {
        return queryDataMapper.getBudgetaryIncome(pd);
    }

    @Override
    public List<Map<String, Object>> getTypeBudget(PageData pd) {
        return queryDataMapper.getTypeBudget(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryBalance(PageData pd) {
        return queryDataMapper.getInventoryBalance(pd);
    }

    @Override
    public List<Map<String, Object>> getInventoryForm(PageData pd) {
        return queryDataMapper.getInventoryForm(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsIncomeSituation(PageData pd) {
        return queryDataMapper.getCustomsIncomeSituation(pd);
    }

    @Override
    public List<Map<String, Object>> getCustomsIncomeSituationTb(PageData pd) {
        return queryDataMapper.getCustomsIncomeSituationTb(pd);
    }

    @Override
    public List<Map<String, Object>> getAccountToGuoku(PageData pd) {
        return queryDataMapper.getAccountToGuoku(pd);
    }

    @Override
    public List<Map<String, Object>> getGuokuToAccount(PageData pd) {
        return queryDataMapper.getGuokuToAccount(pd);
    }

    @Override
    public List<Map<String, Object>> getGuokuToOrg(PageData pd) {
        return queryDataMapper.getGuokuToOrg(pd);
    }

    @Override
    public List<Map<String, Object>> getOrgToGuoku(PageData pd) {
        return queryDataMapper.getOrgToGuoku(pd);
    }
}
