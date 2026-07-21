package org.jeecg.modules.indexlib.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.BaseController;
import org.jeecg.modules.indexlib.service.IndexRelationService;
import org.jeecg.modules.indexlib.service.IndexSchemeService;
import org.jeecg.modules.util.DateUtil_old;
import org.jeecg.modules.util.IndexChartsHelper;
import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.service.GalleryService;
import org.jeecg.modules.visualScreen.service.PageWhereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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
@Api(tags = "指标转饼图图")
@RequestMapping(value = "/IndexPie", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexPieController extends BaseController {
    @Autowired private IndexRelationService indexRelationService;
    @Autowired private IndexSchemeService indexSchemeService;
    @Autowired private GalleryService galleryService;
    @Autowired private PageWhereService pageWhereService;

    @PostMapping("/saveIndexPie")
    @ApiOperation("保存指标图至图库")
    public Map<String, Object> saveIndexPie(
            @RequestBody(required = false) JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData request = getPageData(jsonObject);
            JSONObject condition = JSONObject.parseObject(request.getString("condition"));
            PageData saved = getPageData(condition);
            saved.put("condition", condition.toJSONString());
            Map<String, String> scheme = indexSchemeService.getSchemeInfoById(saved);
            saved.put("scheme_name", scheme.get("SCHEME_DESCR"));
            saved.put("id", get32UUID());
            saved.put("state", "0");
            saved.put("business_id", "1010");
            saved.put("content", request.getString("content"));
            saved.put("add_time",
                    DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY_MM_DD_HH_MM));
            galleryService.add(saved);
            result.put("msg", "添加成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "添加失败");
            result.put("result", "failed");
        }
        return result;
    }

    @RequestMapping(value = "/getIndexPieData", method = RequestMethod.POST)
    @ApiOperation("获取指标图数据")
    public Map<String, Object> getIndexPieData(
            @RequestBody(required = false) JSONObject jsonObject) {
        String endDate = "";
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = getPageData(jsonObject);
            if (StringUtils.isNotEmpty(pageData.getString("gallery_id"))
                    && StringUtils.isNotEmpty(pageData.getString("sub_id"))) {
                PageData lookup = new PageData();
                lookup.put("id", pageData.getString("gallery_id"));
                PageData condition = getPageData(JSONObject.parseObject(String.valueOf(
                        galleryService.getAll(lookup).get(0).get("condition"))));
                lookup.clear();
                lookup.put("gallery_id", pageData.getString("gallery_id"));
                lookup.put("sub_id", pageData.getString("sub_id"));
                String whereValue = String.valueOf(
                        pageWhereService.getAll(lookup).get(0).get("where_value"));
                if (StringUtils.isNotEmpty(whereValue) && whereValue.indexOf("Q") != -1) {
                    whereValue = whereValue.replace("-", "");
                }
                String[] dates = whereValue.split(",");
                if (dates.length == 2) {
                    condition.put("endDate", "至今".equals(dates[1])
                            && "d".equals(condition.getString("time_type"))
                            ? DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY_MM_DD)
                            : dates[1]);
                    condition.put("startDate", dates[0]);
                }
                if (dates.length == 1) { condition.put("startDate", dates[0]); }
                pageData = condition;
                pageData.put("price", condition.getString("unit"));
                pageData.put("periodFlag", condition.getString("periodFlag"));
            }
            if (StringUtils.isEmpty(pageData.getString("direction"))
                    || !("X".equals(pageData.getString("direction"))
                    || "Y".equals(pageData.getString("direction")))) {
                result.put("msg", "统计方向未选择，查询失败");
                result.put("result", "failed");
                return result;
            }
            if ("1".equals(String.valueOf(pageData.get("periodFlag")))) {
                endDate = DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY_MM_DD);
            } else if ("2".equals(String.valueOf(pageData.get("periodFlag")))) {
                endDate = DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY_MM);
            } else if ("3".equals(String.valueOf(pageData.get("periodFlag")))) {
                endDate = getQuarterNow(
                        DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY_MM));
            } else if ("4".equals(String.valueOf(pageData.get("periodFlag")))) {
                endDate = DateUtil_old.getCurrentDateStr(DateUtil_old.Pattern.YYYY);
            }
            if ("1".equals(String.valueOf(pageData.get("timeType")))) {
                pageData.put("endDate", endDate);
            } else if ("3".equals(String.valueOf(pageData.get("timeType")))) {
                pageData.put("endDate", endDate);
                pageData.put("startDate", endDate);
            }
            if ("true".equals(pageData.getString("isRate"))) {
                pageData.put("startDate", pageData.getString("endDate"));
            }

            Map<String, String> scheme = indexSchemeService.getSchemeInfoById(pageData);
            String indexColumns = scheme.get("SCHEME_COLUMS");
            if (StringUtils.isEmpty(indexColumns)) { return null; }
            String[] columns = indexColumns.split(",");
            List<Map<String, String>> indexInfoList = new ArrayList<>();
            PageData detailRequest = new PageData();
            for (String column : columns) {
                detailRequest.clear();
                detailRequest.put("INDEX_ID", column);
                Map<String, Object> detail = indexRelationService.getIndexDetails(detailRequest);
                Map<String, String> info = new HashMap<>();
                info.put(column, detail.get("INDEX_NAME").toString());
                info.put("INDEX_TYPE", detail.get("INDEX_TYPE").toString());
                info.put("INDEX_CORRE_TABLE", detail.get("INDEX_CORRE_TABLE").toString());
                indexInfoList.add(info);
            }
            result.put("indexInfoList", indexInfoList);
            String baseSql = IndexChartsHelper.createIndexPieSQL(indexInfoList, columns, pageData);
            StringBuilder sql = new StringBuilder(
                    " SELECT V.name, CAST(ROUND(COALESCE(V.value, 0)/")
                    .append(pageData.getString("price"))
                    .append(", 2) AS char) as value FROM ( ");
            if ("X".equals(pageData.getString("direction"))) {
                sql.append(" SELECT MIN(aa.CODE) AS CODE, aa.COLID AS COLID, ")
                        .append("MIN(r.index_name) AS name, MIN(aa.GK) AS GK, ")
                        .append("IFNULL(SUM(aa.value),0) AS value FROM (")
                        .append(baseSql).append(") aa LEFT JOIN ")
                        .append("`indicators_lib`.`lib_index_relation` r ")
                        .append("ON aa.COLID = r.`INDEX_ID` WHERE aa.CODE ='")
                        .append(pageData.getString("GK"))
                        .append("' GROUP BY aa.COLID ) V ORDER BY V.value ASC ");
            } else {
                sql.append(" SELECT aa.CODE AS CODE, MIN(aa.COLID) AS COLID, ")
                        .append("MIN(aa.GK) AS name, ")
                        .append("IFNULL(SUM(aa.value),0) AS value FROM (")
                        .append(baseSql).append(") aa WHERE aa.COLID ='")
                        .append(pageData.getString("indexName"))
                        .append("' GROUP BY aa.CODE ORDER BY aa.CODE ASC ) V ");
            }
            PageData query = new PageData();
            query.put("schemeSql", sql.toString());
            List<Map<String, Object>> rows = indexSchemeService.execSchemeSql(query);
            String[] legend = new String[rows.size()];
            if (CollectionUtils.isNotEmpty(rows)) {
                for (int i = 0; i < rows.size(); i++) {
                    legend[i] = String.valueOf(rows.get(i).get("name"));
                }
            }
            result.put("legend", legend);
            result.put("data", rows);
            result.put("msg", "查询成功");
            result.put("type", pageData.getString("type"));
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "操作失败");
            result.put("result", "failed");
        }
        return result;
    }

    private String getQuarterNow(String yearMonth) {
        String year = yearMonth.substring(0, 4);
        int month = Integer.parseInt(yearMonth.substring(5, 7));
        return year + "Q" + (month % 3 == 0 ? month / 3 : month / 3 + 1);
    }
}
