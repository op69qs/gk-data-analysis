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
import org.jeecg.modules.visualScreen.service.QueryMapDataService;
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
@RequestMapping(value = "/queryMapData", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryMapDataController extends BaseController {

    @Autowired
    private QueryDataService queryDataService;

    @Autowired
    private QueryMapDataService queryMapDataService;

    @RequestMapping(value = "/getBudgetIncome", method = RequestMethod.POST)
    @ApiOperation("区域收支地图")
    public Map<String, Object> getBudgetIncome(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));

            pd.put("INDEX_NAME", "收入合计");
            pd.put("type", "amount");
            List<Map<String, Object>> data = queryMapDataService.getIncome(pd);
//            pd.put("INDEX_NAME", "收入合计金额");
            pd.put("type", "rate");
            List<Map<String, Object>> data1 = queryMapDataService.getIncomeRate(pd);
            pd.put("INDEX_NAME", "支出合计");
            pd.put("type", "amount");
            List<Map<String, Object>> data2 = queryMapDataService.getPayOut(pd);
//            pd.put("INDEX_NAME", "支出合计金额");
            pd.put("type", "amount");
            List<Map<String, Object>> data3 = queryMapDataService.getPayOutRate(pd);
            createBubbles(data, data1, data2, data3, result);
            result.put("data", data);
            result.put("type", "map");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getBudgetRevenue", method = RequestMethod.POST)
    @ApiOperation("重庆市预算收入分地区展示")
    public Map<String, Object> getBudgetRevenue(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "一般公共预算收入金额");
            pd.put("type", "amount");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));
            List<Map<String, Object>> data = queryMapDataService.getBudgetRevenue(pd);
            Map<String, Object> maxAndMin = queryMapDataService.getBudgetRevenueMax(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }
            List<Map<String, Object>>  TitleArea = queryMapDataService.getBudgetRevenueTitle(pd);
            pd.put("INDEX_NAME", "一般公共预算收入金额");
            pd.put("type", "rate");
            List<Map<String, Object>> data1 = queryMapDataService.getBudgetRevenueRate(pd);
            List<Map<String, Object>>  TitleArea1 = queryMapDataService.getBudgetRevenueRateTitle(pd);
            pd.put("INDEX_NAME", "税收收入金额");
            pd.put("type", "amount");
            List<Map<String, Object>> data2 = queryMapDataService.getBudgetRevenue(pd);
            List<Map<String, Object>>  TitleArea2 = queryMapDataService.getBudgetRevenueTitle(pd);
            pd.put("INDEX_NAME", "税收收入金额");
            pd.put("type", "rate");
            List<Map<String, Object>> data3 = queryMapDataService.getBudgetRevenueRate(pd);
            List<Map<String, Object>>  TitleArea3 = queryMapDataService.getBudgetRevenueRateTitle(pd);
            createBubbles(data, data1, data2, data3, result);
            result.put("data", data);
            List<Map<String, Object>> areaAll = new ArrayList<>();
            Map<String, Object> area1 = new HashMap<>();
            Map<String, Object> area2 = new HashMap<>();
            Map<String, Object> area3 = new HashMap<>();
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("AREA_DSCR").equals("重庆市")){
                    area1.put("areaDscr","重庆市".equals(TitleArea.get(i).get("AREA_DSCR"))?"市级":TitleArea.get(i).get("AREA_DSCR"));
                    area1.put("titleAreaYS",TitleArea.get(i).get("INDEX_VALUE"));
                    area1.put("titleAreaYSTB",TitleArea1.get(i).get("INDEX_VALUE"));
                    area1.put("titleAreaSS",TitleArea2.get(i).get("INDEX_VALUE"));
                    area1.put("titleAreaSSTB",TitleArea3.get(i).get("INDEX_VALUE"));
                    areaAll.add(area1);
                }
            }
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("AREA_DSCR").equals("两江新区")){
                    area2.put("areaDscr",TitleArea.get(i).get("AREA_DSCR"));
                    area2.put("titleAreaYS",TitleArea.get(i).get("INDEX_VALUE"));
                    area2.put("titleAreaYSTB",TitleArea1.get(i).get("INDEX_VALUE"));
                    area2.put("titleAreaSS",TitleArea2.get(i).get("INDEX_VALUE"));
                    area2.put("titleAreaSSTB",TitleArea3.get(i).get("INDEX_VALUE"));
                    areaAll.add(area2);
                }
            }
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("AREA_DSCR").equals("高新区")){
                    area3.put("areaDscr",TitleArea.get(i).get("AREA_DSCR"));
                    area3.put("titleAreaYS",TitleArea.get(i).get("INDEX_VALUE"));
                    area3.put("titleAreaYSTB",TitleArea1.get(i).get("INDEX_VALUE"));
                    area3.put("titleAreaSS",TitleArea2.get(i).get("INDEX_VALUE"));
                    area3.put("titleAreaSSTB",TitleArea3.get(i).get("INDEX_VALUE"));
                    areaAll.add(area3);
                }
            }
            result.put("titleArea", areaAll);
            result.put("msg", "查询成功");
            result.put("type", "map");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getRevenueDisplay", method = RequestMethod.POST)
    @ApiOperation("预算收入全国分地区展示")
    public Map<String, Object> getRevenueDisplay(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));


            pd.put("INDEX_NAME", "公共预算收入小计");
            pd.put("type", "amount");
            List<Map<String, Object>> data = queryMapDataService.getRevenueDisplay(pd);
            Map<String, Object> maxAndMin = queryMapDataService.getRevenueDisplayMax(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }

//            pd.put("INDEX_NAME", "公共预算同比增速");
            pd.put("INDEX_NAME", "公共预算收入小计");
            pd.put("type", "rate");
            List<Map<String, Object>> data1 = queryMapDataService.getRevenueDisplayRate(pd);
            pd.put("INDEX_NAME", "税收收入");
            pd.put("type", "amount");
            List<Map<String, Object>> data2 = queryMapDataService.getRevenueDisplay(pd);
//            pd.put("INDEX_NAME", "税收收入同比增速");
            pd.put("INDEX_NAME", "税收收入");
            pd.put("type", "rate");
            List<Map<String, Object>> data3 = queryMapDataService.getRevenueDisplayRate(pd);
            createBubbles(data, data1, data2, data3, result);
            result.put("data", data);
            result.put("type", "map");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getEconomicZone", method = RequestMethod.POST)
    @ApiOperation("重庆经济区域公共预算收入展示")
    public Map<String, Object> getEconomicZone(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "一般公共预算收入");
            pd.put("type", "amount");
            List<Map<String, Object>> data = queryMapDataService.getEconomicZone(pd);
            Map<String, Object> maxAndMin = queryMapDataService.getEconomicZoneMax(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }
//            pd.put("INDEX_NAME", "一般公共预算收入同比增速");
            pd.put("INDEX_NAME", "一般公共预算收入");
            pd.put("type", "rate");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));

            List<Map<String, Object>> data1 = queryMapDataService.getEconomicZoneRate(pd);
            createBubblesTwo(data, data1, result);
            result.put("data", data);
            result.put("type", "map");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getEconomicTaxation", method = RequestMethod.POST)
    @ApiOperation("重庆经济区域税收收入展示")
    public Map<String, Object> getEconomicTaxation(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code").replace("guoku_id","a.guoku_id"));

            pd.put("INDEX_NAME", "税收收入");
            pd.put("type", "amount");
            List<Map<String, Object>> data = queryMapDataService.getEconomicTaxation(pd);
            Map<String, Object> maxAndMin = queryMapDataService.getEconomicTaxationMax(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }
//            pd.put("INDEX_NAME", "税收收入同比增速");
            pd.put("INDEX_NAME", "税收收入");
            pd.put("type", "rate");
            List<Map<String, Object>> data1 = queryMapDataService.getEconomicTaxationRate(pd);
            createBubblesTwo(data, data1, result);
            result.put("data", data);
            result.put("type", "map");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    @RequestMapping(value = "/getEconomicPay", method = RequestMethod.POST)
    @ApiOperation("重庆市支出分地区展示")
    public Map<String, Object> getEconomicPay(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            pd.put("INDEX_NAME", "支出合计金额");
            pd.put("type", "amount");
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));
            List<Map<String, Object>> data = queryMapDataService.getEconomicPay(pd);
            List<Map<String, Object>>  TitleArea = queryMapDataService.getEconomicPayTitle(pd);


            Map<String, Object> maxAndMin = queryMapDataService.getEconomicPayMax(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }
//            pd.put("INDEX_NAME", "支出合计金额同比");
            pd.put("INDEX_NAME", "支出合计金额");
            pd.put("type", "rate");

            List<Map<String, Object>> data1 = queryMapDataService.getEconomicPayRate(pd);
            List<Map<String, Object>>  TitleArea1 = queryMapDataService.getEconomicPayRateTitle(pd);
            List<Map<String, Object>> areaAll = new ArrayList<>();
//            for(int i=0;i< TitleArea.size();i++){
//                Map<String, Object> area1 = new HashMap<>();
//                area1.put("areaDscr","重庆市".equals(TitleArea.get(i).get("name"))?"市级":TitleArea.get(i).get("name"));
//                area1.put("titleAreaPayOut",TitleArea.get(i).get("value"));
//                area1.put("titleAreaPayOutTB",TitleArea1.get(i).get("value"));
//                areaAll.add(area1);
//            }
            Map<String, Object> area1 = new HashMap<>();
            Map<String, Object> area2 = new HashMap<>();
            Map<String, Object> area3 = new HashMap<>();
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("重庆市")){
                    area1.put("areaDscr","重庆市".equals(TitleArea.get(i).get("name"))?"市级":TitleArea.get(i).get("name"));
                    area1.put("titleAreaPayOut",TitleArea.get(i).get("value"));
                    area1.put("titleAreaPayOutTB",TitleArea1.get(i).get("value"));
                    areaAll.add(area1);
                }
            }
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("两江新区")){
                    area2.put("areaDscr",TitleArea.get(i).get("name"));
                    area2.put("titleAreaPayOut",TitleArea.get(i).get("value"));
                    area2.put("titleAreaPayOutTB",TitleArea1.get(i).get("value"));
                    areaAll.add(area2);
                }
            }

            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("高新区")){
                    area3.put("areaDscr",TitleArea.get(2).get("name"));
                    area3.put("titleAreaPayOut",TitleArea.get(2).get("value"));
                    area3.put("titleAreaPayOutTB",TitleArea1.get(2).get("value"));
                    areaAll.add(area3);
                }
            }

            result.put("titleArea", areaAll);
            createBubblesTwo(data, data1, result);
            result.put("data", data);
            result.put("type", "map");
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
    @ApiOperation("重庆市库存分地区展示：库存余额+同比")
    public Map<String, Object> getInventoryBalance(@RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            PageSub pageSub = queryDataService.getPageSub(pd);
            getPeriod(pageSub, pd);
            getUnit(pageSub, pd);
            createWhere(pageSub,pd);
            result.put("dateInfo", pd.get("dateInfo"));
            List<Map<String, Object>> data = queryMapDataService.getInventoryBalance(pd);
            Map<String, Object> maxAndMin = queryMapDataService.getInventoryBalanceMax(pd);
            List<Map<String, Object>>  TitleArea = queryMapDataService.getInventoryBalanceTitle(pd);
            if (null != maxAndMin && !maxAndMin.isEmpty()) {
                result.put("max", Double.parseDouble(maxAndMin.get("max_value") + ""));
                result.put("min", Double.parseDouble(maxAndMin.get("min_value") + ""));
            }
            pd.put("whereInfo",pd.getString("whereInfo").replace("dacct","a.dacct").replace("area_code","a.area_code"));

            List<Map<String, Object>> data1 = queryMapDataService.getInventoryBalanceRate(pd);
            List<Map<String, Object>>  TitleArea1 = queryMapDataService.getInventoryBalanceRateTitle(pd);
            List<Map<String, Object>> areaAll = new ArrayList<>();
//            for(int i=0;i< TitleArea.size();i++){
//                Map<String, Object> area1 = new HashMap<>();
//                area1.put("areaDscr","重庆市".equals(TitleArea.get(i).get("name"))?"市级":TitleArea.get(i).get("name"));
//                area1.put("titleAreaStock",TitleArea.get(i).get("value"));
//                area1.put("titleAreaStockTB",TitleArea1.get(i).get("value"));
//                areaAll.add(area1);
//            }
            Map<String, Object> area1 = new HashMap<>();
            Map<String, Object> area2 = new HashMap<>();
            Map<String, Object> area3 = new HashMap<>();
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("重庆市")){
                    area1.put("areaDscr","重庆市".equals(TitleArea.get(i).get("name"))?"市级":TitleArea.get(i).get("name"));
                    area1.put("titleAreaStock",TitleArea.get(i).get("value"));
                    area1.put("titleAreaStockTB",TitleArea1.get(i).get("value"));
                    areaAll.add(area1);
                }
            }
            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("两江新区")){
                    area2.put("areaDscr",TitleArea.get(i).get("name"));
                    area2.put("titleAreaStock",TitleArea.get(i).get("value"));
                    area2.put("titleAreaStockTB",TitleArea1.get(i).get("value"));
                    areaAll.add(area2);
                }
            }

            for(int i=0;i<3;i++){
                if(TitleArea.get(i).get("name").equals("高新区")){
                    area3.put("areaDscr",TitleArea.get(2).get("name"));
                    area3.put("titleAreaStock",TitleArea.get(2).get("value"));
                    area3.put("titleAreaStockTB",TitleArea1.get(2).get("value"));
                    areaAll.add(area3);
                }
            }
            result.put("titleArea", areaAll);
            createBubblesTwo(data, data1, result);
            result.put("data", data);
            result.put("type", "map");
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "success");
        }
        return result;
    }

    private void createBubbles(List<Map<String, Object>> data,
                               List<Map<String, Object>> data1,
                               List<Map<String, Object>> data2,
                               List<Map<String, Object>> data3,
                               Map<String, Object> result) {
        if (null != data && data.size() > 0) {
            List<Object[]> bubbles = new ArrayList<>();
            for (Map<String, Object> d : data) {
                Object[] bubble = new Object[5];
                bubble[0] = d.get("name");
                bubble[1] = d.get("value");
                bubble[2] = 0;
                if (null != data1 && data1.size() > 0) {
                    for (Map<String, Object> e : data1) {
                        if (d.get("name").equals(e.get("name"))) {
                            bubble[2] = e.get("value");
                            break;
                        }
                    }
                }
                bubble[3] = 0;
                if (null != data2 && data2.size() > 0) {
                    for (Map<String, Object> e : data2) {
                        if (d.get("name").equals(e.get("name"))) {
                            bubble[3] = e.get("value");
                            break;
                        }
                    }
                }
                bubble[4] = 0;
                if (null != data3 && data3.size() > 0) {
                    for (Map<String, Object> e : data3) {
                        if (d.get("name").equals(e.get("name"))) {
                            bubble[4] = e.get("value");
                            break;
                        }
                    }
                }
                bubbles.add(bubble);
            }
            result.put("bubbles", bubbles);
        }else{
            result.put("nodata", true);
        }
    }

    private void createBubblesTwo(List<Map<String, Object>> data,
                                  List<Map<String, Object>> data1,
                                  Map<String, Object> result) {
        if (null != data && data.size() > 0) {
            List<Object[]> bubbles = new ArrayList<>();
            for (Map<String, Object> d : data) {
                Object[] bubble = new Object[3];
                bubble[0] = d.get("name");
                bubble[1] = d.get("value");
                bubble[2] = 0;
                if (null != data1 && data1.size() > 0) {
                    for (Map<String, Object> e : data1) {
                        if (d.get("name").equals(e.get("name"))) {
                            bubble[2] = e.get("value");
                            break;
                        }
                    }
                }
                bubbles.add(bubble);
            }
            result.put("bubbles", bubbles);
        }else{
            result.put("nodata", true);
        }
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
//            pd.put("whereInfo", WhereUtil.greateWhere(pageSub, pws));
            Map<String, Object> greateWhereMap = WhereUtil.greateWhere(pageSub, pws);
            pd.put("whereInfo", greateWhereMap.get("whereSql"));
            pd.put("dateInfo", greateWhereMap.get("dateInfo"));
        }
    }
    private void createWhereKC(PageSub pageSub, PageData pd) {
        PageData queryPd = new PageData();
        queryPd.put("sub_id", pd.get("id"));
        List<PageWhere> pws = queryDataService.getPageWhere(queryPd);
        if (pageSub != null) {
//            pd.put("whereInfo", WhereUtil.greateWhereKC(pageSub, pws));
            Map<String, Object> greateWhereMap = WhereUtil.greateWhere(pageSub, pws);
            pd.put("whereInfo", greateWhereMap.get("whereSql"));
            pd.put("dateInfo", greateWhereMap.get("dateInfo"));
        }
    }
}
