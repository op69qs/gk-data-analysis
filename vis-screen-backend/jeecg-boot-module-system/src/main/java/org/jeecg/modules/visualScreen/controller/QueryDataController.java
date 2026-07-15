package org.jeecg.modules.visualScreen.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.util.WhereUtil;
import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;
import org.jeecg.modules.visualScreen.service.QueryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "数据查询")
@RequestMapping(value = "/queryData", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryDataController extends BaseController {

    @Autowired
    private QueryDataService queryDataService;

    @RequestMapping(value = "/getStructure", method = RequestMethod.POST)
    @ApiOperation("财力结构")
    public Map<String, Object> getStructure(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> benqiMaps = queryDataService.getStructure(pd);
            greateTQWhere(pageSub, pd);
            List<Map<String, Object>> tongqiMaps = queryDataService.getStructure(pd);

            List<PageData> benqi = new ArrayList<>();
            List<PageData> tongqi = new ArrayList<>();
            Double benqiSum = 0.0;
            if (null != benqiMaps && benqiMaps.size() > 0) {
                for (Map<String, Object> data : benqiMaps) {
                    PageData b = new PageData();
                    b.put("value", Double.parseDouble(data.get("INDEX_VALUE") + ""));
                    benqiSum += Double.parseDouble(data.get("INDEX_VALUE") + "");
                    b.put("name", data.get("INDEX_NAME"));
                    benqi.add(b);
                }
                String[] memo = new String[benqiMaps.size()];
                memo[0]=benqiMaps.get(0).get("INDEX_NAME") + "";
                memo[1]=benqiMaps.get(3).get("INDEX_NAME") + "";
                memo[2]=benqiMaps.get(1).get("INDEX_NAME") + "";
                memo[3]=benqiMaps.get(2).get("INDEX_NAME") + "";
                memo[4]=benqiMaps.get(4).get("INDEX_NAME") + "";
//                for (int i = 0; i < benqiMaps.size(); i++) {
//                    memo[i] = benqiMaps.get(i).get("INDEX_NAME") + "";
//                }
                result.put("本期标题", "地方财力结构");
                result.put("同期标题", "去年同期");
                result.put("memo", memo);
            } else {
                result.put("nodata", true);
            }
            Double tongqiSum = 0.0;
            if (null != tongqiMaps && tongqiMaps.size() > 0) {
                for (Map<String, Object> data : tongqiMaps) {
                    PageData t = new PageData();
                    t.put("value", Double.parseDouble(data.get("INDEX_VALUE") + ""));
                    tongqiSum += Double.parseDouble(data.get("INDEX_VALUE") + "");
                    t.put("name", data.get("INDEX_NAME"));
                    tongqi.add(t);
                }
            }
            String[] sum = new String[2];
//            if (benqiSum != 0.0) {
//                if (null != pageSub.getUnit()) {
//                    benqiSum = benqiSum / Integer.parseInt(pageSub.getUnit());
//                }
//            }
//            if (tongqiSum != 0.0) {
//                if (null != pageSub.getUnit()) {
////                    tongqiSum = tongqiSum / Integer.parseInt(pageSub.getUnit());
//                    tongqiSum = tongqiSum ;
//                }
//            }
            sum[0] = "合计" + String.format("%.2f", benqiSum) + unit;
            sum[1] = "合计" + String.format("%.2f", tongqiSum) + unit;
            result.put("benqi", benqi);
            result.put("tongqi", tongqi);
            result.put("sum", sum);
            result.put("type", "two_pie");
            result.put("pageSub", pageSub);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getTransferIncome", method = RequestMethod.POST)
    @ApiOperation("转移性收入金额及同比")
    public Map<String, Object> getTransferIncome(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> amount = queryDataService.getTransferIncome(pd);
            List<Map<String, Object>> rate = queryDataService.getTransferIncomeRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getLocalFinancial", method = RequestMethod.POST)
    @ApiOperation("地方债收入金额及同比")
    public Map<String, Object> getLocalFinancial(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> amount = queryDataService.getLocalFinancial(pd);
            List<Map<String, Object>> rate = queryDataService.getLocalFinancialRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getLandTransfer", method = RequestMethod.POST)
    @ApiOperation("土地出让收入金额及同比")
    public Map<String, Object> getLandTransfer(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> amount = queryDataService.getLandTransfer(pd);
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getLandTransferRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getPublicBudget", method = RequestMethod.POST)
    @ApiOperation("一般公共预算收入金额及同比")
    public Map<String, Object> getPublicBudget(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);

            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> amount = queryDataService.getPublicBudget(pd);
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getPublicBudgetRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getThreeBudget", method = RequestMethod.POST)
    @ApiOperation("三本预算收入金额及同比")
    public Map<String, Object> getThreeBudget(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));

            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            pd.put("INDEX_NAME", "一般公共预算收入");
            List<Map<String, Object>> index1 = queryDataService.getThreeBudget(pd);
//            pd.put("INDEX_NAME", "地方级基金预算收入全辖当期值");
            pd.put("INDEX_NAME", "基金预算收入");
            List<Map<String, Object>> index2 = queryDataService.getThreeBudget(pd);
//            pd.put("INDEX_NAME", "地方级国有资本经营预算收入全辖当期值");
            pd.put("INDEX_NAME", "国有资本经营预算收入");
            List<Map<String, Object>> index3 = queryDataService.getThreeBudget(pd);
//            pd.put("INDEX_NAME", "一般公共预算收入增速");
            pd.put("INDEX_NAME", "一般公共预算收入");
            List<Map<String, Object>> rate1 = queryDataService.getThreeBudgetRate(pd);
//            pd.put("INDEX_NAME", "地方级基金预算收入全辖同比");
            pd.put("INDEX_NAME", "基金预算收入");
            List<Map<String, Object>> rate2 = queryDataService.getThreeBudgetRate(pd);
//            pd.put("INDEX_NAME", "地方级国有资本经营预算收入全辖同比");
            pd.put("INDEX_NAME", "国有资本经营预算收入");
            List<Map<String, Object>> rate3 = queryDataService.getThreeBudgetRate(pd);
            getThreeValue(index1, index2, index3, result, "x", "amount");
            getThreeValue(rate1, rate2, rate3, result, "z", "rate");
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getTaxRevenue", method = RequestMethod.POST)
    @ApiOperation("税收金额及同比")
    public Map<String, Object> getTaxRevenue(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            pd.put("INDEX_NAME", "税收收入当期值");
            List<Map<String, Object>> index = queryDataService.getTaxRevenue(pd);
//            pd.put("INDEX_NAME", "税收收入同比");
            pd.put("INDEX_NAME", "税收收入当期值");

             List<Map<String, Object>> rate = queryDataService.getTaxRevenueRate(pd);

            getOneValue(index, result, "x", "amount");
            getOneValue(rate, result, "z", "rate");
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getNotTaxRevenue", method = RequestMethod.POST)
    @ApiOperation("非税金额及同比")
    public Map<String, Object> getNotTaxRevenue(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "非税收收入当期值");
            List<Map<String, Object>> index = queryDataService.getTaxRevenue(pd);
            pd.put("INDEX_NAME", "非税收收入当期值");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getTaxRevenueRate(pd);

            getOneValue(index, result, "x", "amount");
            getOneValue(rate, result, "z", "rate");
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIncomePayoutGap", method = RequestMethod.POST)
    @ApiOperation("收支缺口")
    public Map<String, Object> getIncomePayoutGap(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));

            pd.put("INDEX_NAME", "公共预算支出");
            List<Map<String, Object>> index1 = queryDataService.getIncomePayoutGap(pd);
            pd.put("INDEX_NAME", "公共预算收入");
            List<Map<String, Object>> index2 = queryDataService.getIncomePayoutGap(pd);

            pd.put("INDEX_NAME", "支出缺口");
            List<Map<String, Object>> index3 = queryDataService.getIncomePayoutGap(pd);

            getThreeValue(index1, index2, index3, result, "x", "amount");
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "bar");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIndustryTax", method = RequestMethod.POST)
    @ApiOperation("行业税收收入")
    public Map<String, Object> getIndustryTax(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));

            pd.put("INDEX_NAME", "公共预算支出");
            List<Map<String, Object>> index1 = queryDataService.getIndustryTax(pd);
            pd.put("INDEX_NAME", "公共预算收入");
            List<Map<String, Object>> index2 = queryDataService.getIndustryTax(pd);

            pd.put("INDEX_NAME", "支出缺口");
            List<Map<String, Object>> index3 = queryDataService.getIndustryTax(pd);

            getThreeValue(index1, index2, index3, result, "x", "amount");
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getMunicipalitiesDirectly", method = RequestMethod.POST)
    @ApiOperation("直辖市预算收入排名")
    public Map<String, Object> getMunicipalitiesDirectly(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));

            pd.put("INDEX_NAME", "公共预算收入小计");
            List<Map<String, Object>> data = queryDataService.getMunicipalitiesDirectly(pd);
            if (null == data || data.size() == 0) {
                result.put("nodata", true);
            }
            result.put("data", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "pie");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getCustomsRevenue", method = RequestMethod.POST)
    @ApiOperation("海关征收税收收入情况")
    public Map<String, Object> getCustomsRevenue(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "税收收入");
            List<Map<String, Object>> amount = queryDataService.getCustomsRevenue(pd);
//            pd.put("INDEX_NAME", "税收收入增速");
            pd.put("INDEX_NAME", "税收收入");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getCustomsRevenueRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getCustomsNonTax", method = RequestMethod.POST)
    @ApiOperation("海关征收非税收入情况")
    public Map<String, Object> getCustomsNonTax(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "非税收收入");
            List<Map<String, Object>> amount = queryDataService.getCustomsNonTax(pd);
//            pd.put("INDEX_NAME", "非税收收入增速");
            pd.put("INDEX_NAME", "非税收收入");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getCustomsNonTaxRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getCustomsImportDuties", method = RequestMethod.POST)
    @ApiOperation("海关征收进口关税情况")
    public Map<String, Object> getCustomsImportDuties(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "进口关税");
            List<Map<String, Object>> amount = queryDataService.getCustomsImportDuties(pd);
//            pd.put("INDEX_NAME", "进口关税增速");
            pd.put("INDEX_NAME", "进口关税");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getCustomsImportDutiesRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getCustomsImportVat", method = RequestMethod.POST)
    @ApiOperation("海关征收进口增值税情况")
    public Map<String, Object> getCustomsImportVat(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "进口增值税");
            List<Map<String, Object>> amount = queryDataService.getCustomsImportVat(pd);
//            pd.put("INDEX_NAME", "进口增值税增速");
            pd.put("INDEX_NAME", "进口增值税");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getCustomsImportVatRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getImportDutyArticles", method = RequestMethod.POST)
    @ApiOperation("海关征收进境物品进口税(行邮税)情况")
    public Map<String, Object> getImportDutyArticles(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "进境物品进口税");
            List<Map<String, Object>> amount = queryDataService.getImportDutyArticles(pd);
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            pd.put("INDEX_NAME", "进境物品进口税");
//            pd.put("INDEX_NAME", "进境物品进口税增速");
            List<Map<String, Object>> rate = queryDataService.getImportDutyArticlesRate(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getBudgetIncomeTop5", method = RequestMethod.POST)
    @ApiOperation("西南五省市预算收入排名")
    public Map<String, Object> getBudgetIncomeTop5(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> benqiMaps = queryDataService.getBudgetIncomeTop5(pd);
            List<PageData> benqi = new ArrayList<>();
            if (null != benqiMaps && benqiMaps.size() > 0) {
                for (Map<String, Object> data : benqiMaps) {
                    PageData b = new PageData();
                    b.put("value", Double.parseDouble(data.get("INDEX_VALUE") + ""));
                    b.put("name", data.get("INDEX_NAME"));
                    benqi.add(b);
                }
            } else {
                result.put("nodata", true);
            }
            result.put("benqi", benqi);
            result.put("type", "funnel");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIndustryType", method = RequestMethod.POST)
    @ApiOperation("行业税收分析")
    public Map<String, Object> getIndustryType(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryDataService.getIndustryType(pd);
            List<Map<String, Object>> data1 = queryDataService.getIndustryName(pd);
            if (null == data || data.size() == 0) {
                result.put("nodata", true);
            }
            result.put("data", data);
            result.put("data1", data1);
            result.put("type", "pie");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIndustryTop10", method = RequestMethod.POST)
    @ApiOperation("行业税收排名TOP10")
    public Map<String, Object> getIndustryTop10(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
             result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> maps = queryDataService.getIndustryTop10(pd);
            List<String> y = new ArrayList<>();
            List<Double> data = new ArrayList<>();
            if (null != maps && maps.size() > 0) {
                for (Map<String, Object> map : maps) {
                    y.add(map.get("name") + "");
                    data.add(Double.parseDouble(map.get("value") + ""));
                }
            } else {
                result.put("nodata", true);
            }
            result.put("y", y);
            result.put("data", data);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIndustryMain", method = RequestMethod.POST)
    @ApiOperation("支柱行业税收变化趋势分析")
    public Map<String, Object> getIndustryMain(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            String[] legend = new String[3];
            pd.put("industrial_name", "房地产业");
            legend[0] = "房地产业";
            List<Map<String, Object>> maps1 = queryDataService.getIndustryMain(pd);
            pd.put("industrial_name", "信息技术服务");
            legend[1] = "信息技术服务";
            List<Map<String, Object>> maps2 = queryDataService.getIndustryMain(pd);
            pd.put("industrial_name", "制造业");
            legend[2] = "制造业";
            List<Map<String, Object>> maps3 = queryDataService.getIndustryMain(pd);
            getThreeValue(maps1, maps2, maps3, result, "x", "data");
            result.put("legend", legend);
            result.put("type", "line");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getTreasuryIndex", method = RequestMethod.POST)
    @ApiOperation("国库指数与GDP相关分析图")
    public Map<String, Object> getTreasuryIndex(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            String[] legend = new String[2];
            pd.put("INDEX_NAME", "国库指数");
            legend[0] = "国库指数";
            List<Map<String, Object>> maps1 = queryDataService.getTreasuryIndex(pd);
            List<String[]> dataFact = new ArrayList<>();
            List<String[]> dataFact2 = new ArrayList<>();

            for(int i=0;i<maps1.size();i++){
                String  datas[]=new String[2];
                datas[0]=maps1.get(i).get("DACCT").toString();
                datas[1]=""+ Double.parseDouble(maps1.get(i).get("INDEX_VALUE")+"");
                dataFact.add(datas);
            }

            pd.put("INDEX_NAME", "GDP累计增速");
            legend[1] = "gdp";
            List<Map<String, Object>> maps2 = queryDataService.getTreasuryIndex(pd);

            for(int i=0;i<maps2.size();i++){
                String  datas2[]=new String[2];
                datas2[0]=maps2.get(i).get("DACCT").toString();
                datas2[1]=""+ Double.parseDouble(maps2.get(i).get("INDEX_VALUE")+"");
                dataFact2.add(datas2);
            }

            getTwoValue(maps1, maps2, result, "x", "data");
            result.put("legend", legend);
            result.put("dataFact", dataFact);
            result.put("dataFact2", dataFact2);
            result.put("type", "line");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getGuokuToOrg", method = RequestMethod.POST)
    @ApiOperation("国库流向商业银行")
    public Map<String, Object> getGuokuToOrg(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> datas = queryDataService.getGuokuToOrg(pd);
            List<PageData> dataFact = new ArrayList<>();
            if (null != datas && datas.size() > 0) {
                for (Map<String, Object> data : datas) {
                    PageData t = new PageData();
                    t.put("value", Double.parseDouble(data.get("value") + ""));
                    t.put("name", data.get("name"));
                    dataFact.add(t);
                }
            }
            result.put("data", dataFact);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }
    @RequestMapping(value = "/getOrgToGuoku", method = RequestMethod.POST)
    @ApiOperation("商业银行流向国库")
    public Map<String, Object> getOrgToGuoku(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> datas = queryDataService.getOrgToGuoku(pd);
            List<PageData> dataFact = new ArrayList<>();
            if (null != datas && datas.size() > 0) {
                for (Map<String, Object> data : datas) {
                    PageData t = new PageData();
                    t.put("value", Double.parseDouble(data.get("value") + ""));
                    t.put("name", data.get("name"));
                    dataFact.add(t);
                }
            }
            result.put("data", dataFact);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }
    @RequestMapping(value = "/getGuokuToAccount", method = RequestMethod.POST)
    @ApiOperation("国库流向财政专户")
    public Map<String, Object> getGuokuToAccount(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> datas = queryDataService.getGuokuToAccount(pd);
            List<PageData> dataFact = new ArrayList<>();
            String X[]=new String[7];
            if (null != datas && datas.size() > 0) {
                int i=0;
                for (Map<String, Object> data : datas) {
                    PageData t = new PageData();
                    t.put("value", Double.parseDouble(data.get("value") + ""));
                    t.put("name", data.get("name"));
                    X[i]=data.get("name").toString();
                    i++;
                    dataFact.add(t);
                }
            }
            result.put("data", dataFact);
            result.put("x", X);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }
    @RequestMapping(value = "/getAccountToGuoku", method = RequestMethod.POST)
    @ApiOperation("财政专户流向国库")
    public Map<String, Object> getAccountToGuoku(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> datas = queryDataService.getAccountToGuoku(pd);
            List<PageData> dataFact = new ArrayList<>();
            String X[]=new String[7];
            if (null != datas && datas.size() > 0) {
                int i=0;
                for (Map<String, Object> data : datas) {
                    PageData t = new PageData();
                    t.put("value", Double.parseDouble(data.get("value") + ""));
                    t.put("name", data.get("name"));
                    dataFact.add(t);
                    X[i]=data.get("name").toString();
                    i++;
                }
            }
            result.put("data", dataFact);
            result.put("x", X);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }
    @RequestMapping(value = "/getSubjectPay", method = RequestMethod.POST)
    @ApiOperation("支出按科目展示")
    public Map<String, Object> getSubjectPay(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryDataService.getSubjectPay(pd);
            if (null == data || data.size() == 0) {
                result.put("nodata", true);
            }
            result.put("data", data);
            result.put("type", "pie");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getSubjectPaySub", method = RequestMethod.POST)
    @ApiOperation("支出子项目占比分析")
    public Map<String, Object> getSubjectPaySub(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryDataService.getSubjectPaySub(pd);
            if (null == data || data.size() == 0) {
                result.put("nodata", true);
            }
            result.put("data", data);
            result.put("type", "pie");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getPurposePay", method = RequestMethod.POST)
    @ApiOperation("支出按用途展示")
    public Map<String, Object> getPurposePay(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryDataService.getPurposePay(pd);
            if (null == data || data.size() == 0) {
                result.put("nodata", true);
            }
            result.put("data", data);
            result.put("type", "pie");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getBudgetaryIncome", method = RequestMethod.POST)
    @ApiOperation("预算收支总体执行情况")
    public Map<String, Object> getBudgetaryIncome(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "预算收入");
            List<Map<String, Object>> incomeMaps = queryDataService.getBudgetaryIncome(pd);
            pd.put("INDEX_NAME", "预算支出");
            List<Map<String, Object>> payMaps = queryDataService.getBudgetaryIncome(pd);

            List<PageData> inCome = new ArrayList<>();
            List<PageData> payOut = new ArrayList<>();
            String[] memo = new String[2];
            if (null != incomeMaps && incomeMaps.size() > 0) {
                for (Map<String, Object> data : incomeMaps) {
                    PageData b = new PageData();
                    b.put("value", Double.parseDouble(data.get("BUDGET_COMPLETE") + ""));
                    b.put("name", "预算收入完成");
                    PageData b1 = new PageData();
                    b1.put("value", Double.parseDouble(data.get("BUDGET_NOT_COMPLETE") + ""));
                    b1.put("name", "预算收入未完成");
                    inCome.add(b);
                    inCome.add(b1);
                    memo[0] = "预算收入完成\n" + Double.parseDouble(data.get("BUDGET_RATE") + "") + "%";
                }
            } else {
                result.put("nodata", true);
            }
            if (null != payMaps && payMaps.size() > 0) {
                for (Map<String, Object> data : payMaps) {
                    PageData b = new PageData();
                    b.put("value", Double.parseDouble(data.get("BUDGET_COMPLETE") + ""));
                    b.put("name", "预算支出完成");
                    PageData b1 = new PageData();
                    b1.put("value", Double.parseDouble(data.get("BUDGET_NOT_COMPLETE") + ""));
                    b1.put("name", "预算支出未完成");
                    payOut.add(b);
                    payOut.add(b1);
                    memo[1] = "预算支出完成\n" + Double.parseDouble(data.get("BUDGET_RATE") + "") + "%";
                }
            }
            result.put("inCome", inCome);
            result.put("payOut", payOut);
            result.put("memo", memo);
            result.put("type", "two_pie");
            result.put("pageSub", pageSub);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getTypeBudget", method = RequestMethod.POST)
    @ApiOperation("预算收支总体执行情况")
    public Map<String, Object> getTypeBudget(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));

            List<Map<String, Object>> maps = queryDataService.getTypeBudget(pd);
            List<PageData> inCome = new ArrayList<>();
            String memo = "";
            if (null != maps && maps.size() > 0) {
                for (Map<String, Object> data : maps) {
                    PageData b = new PageData();
                    b.put("value", Double.parseDouble(data.get("INDEX_VALUE_COMPLETE") + ""));
                    b.put("name", "完成");
                    PageData b1 = new PageData();
                    b1.put("value", Double.parseDouble(data.get("INDEX_VALUE_NOT_COMPLETE") + ""));
                    b1.put("name", "未完成");
                    inCome.add(b);
                    inCome.add(b1);
                    memo = data.get("SUBJECT_DSCR") + "完成\n" + Double.parseDouble(data.get("RATE") + "") + "%";
                }
            } else {
                result.put("nodata", true);
            }
            result.put("inCome", inCome);
            result.put("memo", memo);
            result.put("type", "pie");
            result.put("pageSub", pageSub);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getInventoryBalance", method = RequestMethod.POST)
    @ApiOperation("库存变化趋势分析")
    public Map<String, Object> getInventoryBalance(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            String[] legend = new String[2];
            legend[0] = "库存余额";
            legend[1] = "库存同比";
            List<Map<String, Object>> maps = queryDataService.getInventoryBalance(pd);
            if (null != maps && maps.size() > 0) {
                List<String> x = new ArrayList<>();
                List<Double> y1 = new ArrayList<>();
                List<Double> y2 = new ArrayList<>();
                List<List<Double>> y = new ArrayList<>();
                for (Map<String, Object> a : maps) {
                    x.add(a.get("DACCT") + "");
                    y1.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
                    y2.add(Double.parseDouble(a.get("GROWTH_INDEX_VALUE") + ""));
                }
                y.add(y1);
                y.add(y2);
                result.put("x", x);
                result.put("amount", y);
            } else {
                result.put("nodata", true);
            }
            result.put("legend", legend);
            result.put("type", "line");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getInventoryForm", method = RequestMethod.POST)
    @ApiOperation("库存构成分析")
    public Map<String, Object> getInventoryForm(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "省级库存余额");
            List<Map<String, Object>> data = queryDataService.getInventoryForm(pd);


            pd.put("INDEX_NAME", "区县级库存余额");
            List<Map<String, Object>> data1 = queryDataService.getInventoryForm(pd);
            getTwoValue(data, data1, result, "x", "amount");
//            double[] datas=new double[data.size()];
//            double[] data2=new double[data.size()];
//            String DACCT[]=new String[data.size()];
//            for(int i=0;i<data.size();i++){
//                datas[i]=Double.parseDouble(data.get(i).get("INDEX_VALUE").toString());
//                data2[i]=Double.parseDouble(data.get(i).get("INDEX_VALUE").toString());
//                DACCT[i]=data.get(i).get("DACCT").toString();
//
//            }
//            if (null == data || data.size() == 0) {
//                result.put("nodata", true);
//            }
//            result.put("data", datas);
//            result.put("data1", data2);
//            result.put("DACCT", DACCT);
            result.put("type", "bar");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getCustomsIncomeSituation", method = RequestMethod.POST)
    @ApiOperation("海关收入总体情况")
    public Map<String, Object> getCustomsIncomeSituation(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            String unit = getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "海关收入");
            pd.put("type", "amount");
            List<Map<String, Object>> amount = queryDataService.getCustomsIncomeSituation(pd);
            pd.put("INDEX_NAME", "海关收入");
            pd.put("type", "rate");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> rate = queryDataService.getCustomsIncomeSituationTb(pd);
            getAmountAndRate(amount, rate, result);
            result.put("msg", "查询成功");
            result.put("result", "success");
            result.put("type", "barAndLine");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "failed");
        }
        return result;
    }


    private void getPeriod(PageSub pageSub, PageData pd) {
        if (null != pageSub && pageSub.getTime_type().equals("d")) {
            pd.put("PERIOD_FLAG", "1");
        }
        if (null != pageSub && pageSub.getTime_type().equals("m")) {
            pd.put("PERIOD_FLAG", "2");
        }
        if (null != pageSub && pageSub.getTime_type().equals("q")) {
            pd.put("PERIOD_FLAG", "3");
        }
        if (null != pageSub && pageSub.getTime_type().equals("y")) {
            pd.put("PERIOD_FLAG", "4");
        }
    }

    private String getUnit(PageSub pageSub, PageData pd) {
        if (pageSub == null || !isValidUnit(pageSub.getUnit())) {
            pd.put("unit", "100000000");
            return "亿元";
        }
        if (null != pageSub.getUnit()) {
            pd.put("unit", pageSub.getUnit());
            if (pageSub.getUnit().equals("1")) {
                return "元";
            } else if (pageSub.getUnit().equals("10000")) {
                return "万元";
            } else if (pageSub.getUnit().equals("100000000")) {
                return "亿元";
            }
        }
        return "";
    }

    private boolean isValidUnit(String unit) {
        return "1".equals(unit) || "10000".equals(unit) || "100000000".equals(unit);
    }

    private void ensureUnit(PageData pd) {
        String unit = pd.getString("unit");
        if (!isValidUnit(unit)) {
            pd.put("unit", "100000000");
        }
    }

    private void createWhere(PageSub pageSub, PageData pd) {
        PageData queryPd = new PageData();
        queryPd.put("sub_id", pd.get("id"));
        List<PageWhere> pws = queryDataService.getPageWhere(queryPd);
        ensureUnit(pd);
        if (pageSub != null) {
//            pd.put("whereInfo", WhereUtil.greateWhere(pageSub, pws));
            Map<String, Object> greateWhereMap = WhereUtil.greateWhere(pageSub, pws);
            pd.put("whereInfo", greateWhereMap.get("whereSql"));
            pd.put("dateInfo", greateWhereMap.get("dateInfo"));
            // 由于有同比等情况存在，当查询年的数据如果选择当年时，数据可能存在差异，因此改为查询年数据时，按照月维度进行查询，cuijsh
            if("4".equals(pd.getString("PERIOD_FLAG"))){
                pd.put("PERIOD_TYPE","2");
            }

        }
    }

    private void greateTQWhere(PageSub pageSub, PageData pd) {
        PageData queryPd = new PageData();
        queryPd.put("sub_id", pd.get("id"));
        List<PageWhere> pws = queryDataService.getPageWhere(queryPd);
        if (pageSub != null) {
            pd.put("whereInfo", WhereUtil.greateTQWhere(pageSub, pws));
        }
    }

    private void getThreeValue(List<Map<String, Object>> index1, List<Map<String, Object>> index2, List<Map<String, Object>> index3, Map<String, Object> result
            , String key1, String key2) {
        if (null != index1 && index1.size() > 0) {
            List<String> x = new ArrayList<>();
            List<Double> y1 = new ArrayList<>();
            List<Double> y2 = new ArrayList<>();
            List<Double> y3 = new ArrayList<>();
            List<List<Double>> y = new ArrayList<>();
            for (Map<String, Object> a : index1) {
                x.add(a.get("DACCT") + "");
                y1.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y1);
            for (Map<String, Object> a : index2) {
                y2.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y2);
            for (Map<String, Object> a : index3) {
                y3.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y3);
            result.put(key1, x);
            result.put(key2, y);
        } else {
            result.put("nodata", true);
        }
    }

    private void getTwoValue(List<Map<String, Object>> index1, List<Map<String, Object>> index2, Map<String, Object> result
            , String key1, String key2) {
        if (null != index1 && index1.size() > 0) {
            List<String> x = new ArrayList<>();
            List<Double> y1 = new ArrayList<>();
            List<Double> y2 = new ArrayList<>();
            List<List<Double>> y = new ArrayList<>();
            for (Map<String, Object> a : index1) {
                x.add(a.get("DACCT") + "");
                y1.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y1);
            for (Map<String, Object> a : index2) {
                y2.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y2);
            result.put(key1, x);
            result.put(key2, y);
        } else {
            result.put("nodata", true);
        }
    }

    private void getOneValue(List<Map<String, Object>> index1, Map<String, Object> result
            , String key1, String key2) {
        if (null != index1 && index1.size() > 0) {
            List<String> x = new ArrayList<>();
            List<Double> y1 = new ArrayList<>();
            List<List<Double>> y = new ArrayList<>();
            for (Map<String, Object> a : index1) {
                x.add(a.get("DACCT") + "");
                y1.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            y.add(y1);
            result.put(key1, x);
            result.put(key2, y);
        } else {
            result.put("nodata", true);
        }
    }

    private void getAmountAndRate(List<Map<String, Object>> amount, List<Map<String, Object>> rate, Map<String, Object> result) {
        if (null != amount && amount.size() > 0) {
            List<String> x = new ArrayList<>();
            List<Double> y = new ArrayList<>();
            for (Map<String, Object> a : amount) {
                x.add(a.get("DACCT") + "");
                y.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            result.put("x", x);
            result.put("amount", y);
        } else {
            result.put("nodata", true);
        }
        if (null != rate && rate.size() > 0) {
            List<Double> y = new ArrayList<>();
            for (Map<String, Object> a : rate) {
                y.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
            }
            result.put("rate", y);
        }
    }

}
