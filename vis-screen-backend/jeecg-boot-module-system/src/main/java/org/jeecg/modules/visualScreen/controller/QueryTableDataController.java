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
import org.jeecg.modules.visualScreen.service.QueryTableDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "数据查询")
@RequestMapping(value = "/queryTableData", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryTableDataController extends BaseController {

    @Autowired
    private QueryDataService queryDataService;

    @Autowired
    private QueryTableDataService queryTableDataService;

    @RequestMapping(value = "/getGrowthBudget", method = RequestMethod.POST)
    @ApiOperation("一般公共预算收入金额排名TOP10")
    public Map<String, Object> getGrowthBudget(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));
            pd.put("INDEX_NAME","一般公共预算收入");
            List<Map<String, Object>> data = queryTableDataService.getGrowthBudget(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getGrowthBudgetRate", method = RequestMethod.POST)
    @ApiOperation("一般公共预算收入增速排名TOP10")
    public Map<String, Object> getGrowthBudgetRate(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            pd.put("INDEX_NAME","一般公共预算收入");
            List<Map<String, Object>> data = queryTableDataService.getGrowthBudgetRate(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getGrowthTax", method = RequestMethod.POST)
    @ApiOperation("税收收入金额排名TOP10")
    public Map<String, Object> getGrowthTax(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            pd.put("INDEX_NAME","税收收入金额");
            List<Map<String, Object>> data = queryTableDataService.getGrowthTax(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getGrowthTaxRate", method = RequestMethod.POST)
    @ApiOperation("税收收入增速排名TOP10")
    public Map<String, Object> getGrowthTaxRate(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME","税收收入金额");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> data = queryTableDataService.getGrowthTaxRate(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getAmountRegion", method = RequestMethod.POST)
    @ApiOperation("全国公共预算收入金额排名")
    public Map<String, Object> getAmountRegion(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryTableDataService.getAmountRegion(pd);
            List<Map<String, Object>> dataCQ = queryTableDataService.getAmountRegionCQ(pd);
            result.put("CQIndex", dataCQ.get(0).get("rownum"));
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("CQIndex", dataCQ.get(0).get("rownum"));

            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getRankingGrowth", method = RequestMethod.POST)
    @ApiOperation("全国公共预算收入增速排名")
    public Map<String, Object> getRankingGrowth(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryTableDataService.getRankingGrowth(pd);

            List<Map<String, Object>> dataCQ = queryTableDataService.getAmountRegionCQ(pd);
            result.put("CQIndex", dataCQ.get(0).get("rownum"));

            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getTaxRegional", method = RequestMethod.POST)
    @ApiOperation("全国税收收入金额排名")
    public Map<String, Object> getTaxRegional(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> data = queryTableDataService.getTaxRegional(pd);
            List<Map<String, Object>> dataCQ = queryTableDataService.getAmountRegionCQ(pd);
            result.put("CQIndex", dataCQ.get(0).get("rownum"));
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getTaxRegion", method = RequestMethod.POST)
    @ApiOperation("全国税收收入增速排名")
    public Map<String, Object> getTaxRegion(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryTableDataService.getTaxRegion(pd);
            List<Map<String, Object>> dataCQ = queryTableDataService.getAmountRegionCQ(pd);
            result.put("CQIndex", dataCQ.get(0).get("rownum"));
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getFiveProvinces", method = RequestMethod.POST)
    @ApiOperation("西南五省市预算收入及税收情况")
    public Map<String, Object> getFiveProvinces(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryTableDataService.getFiveProvinces(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getMunicipalities", method = RequestMethod.POST)
    @ApiOperation("全国直辖市预算收入及税收情况")
    public Map<String, Object> getMunicipalities(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
 result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryTableDataService.getMunicipalities(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getRegionPay", method = RequestMethod.POST)
    @ApiOperation("重庆市支出金额排名TOP10")
    public Map<String, Object> getRegionPay(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
             createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME","支出合计");
            List<Map<String, Object>> data = queryTableDataService.getRegionPay(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getRegionPayRate", method = RequestMethod.POST)
    @ApiOperation("重庆市支出同比排名TOP10")
    public Map<String, Object> getRegionPayRate(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME","支出合计");
            List<Map<String, Object>> data = queryTableDataService.getRegionPayRate(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }


    @RequestMapping(value = "/getInventoryBalance", method = RequestMethod.POST)
    @ApiOperation("重庆市库存余额排名TOP10")
    public Map<String, Object> getInventoryBalance(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));

            List<Map<String, Object>> data = queryTableDataService.getInventoryBalance(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getInventoryBalanceRate", method = RequestMethod.POST)
    @ApiOperation("重庆市库存同比排名TOP10")
    public Map<String, Object> getInventoryBalanceRate(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub, pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));

            List<Map<String, Object>> data = queryTableDataService.getInventoryBalanceRate(pd);
            if (null == data || data.size() == 0){
                result.put("nodata", true);
            }
            result.put("rows", data);
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
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

    private void createWhere(PageSub pageSub, PageData pd) {
        PageData queryPd = new PageData();
        queryPd.put("sub_id", pd.get("id"));
        List<PageWhere> pws = queryDataService.getPageWhere(queryPd);
        if (pageSub != null) {
            Map<String, Object> greateWhereMap = WhereUtil.greateWhere(pageSub, pws);
            pd.put("whereInfo", greateWhereMap.get("whereSql"));
            pd.put("dateInfo", greateWhereMap.get("dateInfo"));
        }
    }
//    private void createWhereKCT(PageSub pageSub, PageData pd) {
//        PageData queryPd = new PageData();
//        queryPd.put("sub_id", pd.get("id"));
//        List<PageWhere> pws = queryDataService.getPageWhere(queryPd);
//        if (pageSub != null) {
//            pd.put("whereInfo", WhereUtil.greateWhereKCT(pageSub, pws));
//        }
//    }
}
