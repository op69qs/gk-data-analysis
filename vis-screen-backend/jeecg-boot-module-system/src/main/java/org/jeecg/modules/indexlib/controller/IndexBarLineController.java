package org.jeecg.modules.indexlib.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "指标转柱状或者折线图")
@RequestMapping(value = "/IndexBarLine", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexBarLineController extends BaseController {
    @Autowired private IndexRelationService indexRelationService;
    @Autowired private IndexSchemeService indexSchemeService;
    @Autowired private GalleryService galleryService;
    @Autowired private PageWhereService pageWhereService;

    @PostMapping("/saveIndexBarLine")
    @ApiOperation("保存指标图至图库")
    public Map<String, Object> saveIndexBarLine(
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

    @RequestMapping(value = "/getIndexBarLineData", method = RequestMethod.POST)
    @ApiOperation("获取指标图数据")
    public Map<String, Object> getIndexBarLineData(
            @RequestBody(required = false) JSONObject jsonObject) {
        String endDate = "";
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = getPageData(jsonObject);
            if (StringUtils.isNotEmpty(pageData.getString("gallery_id"))
                    && StringUtils.isNotEmpty(pageData.getString("sub_id"))) {
                PageData lookup = new PageData();
                lookup.put("id", pageData.getString("gallery_id"));
                List<Map<String, Object>> gallery = galleryService.getAll(lookup);
                PageData condition = getPageData(JSONObject.parseObject(
                        String.valueOf(gallery.get(0).get("condition"))));
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

            Map<String, String> scheme = indexSchemeService.getSchemeInfoById(pageData);
            String indexColumns = scheme.get("SCHEME_COLUMS");
            if (StringUtils.isEmpty(indexColumns)) { return null; }
            List schemeColumns = JSONObject.parseArray(pageData.getString("schemecolumns"));
            String checked = "";
            if (!CollectionUtils.isEmpty(schemeColumns)) {
                StringBuilder selected = new StringBuilder();
                for (Object item : schemeColumns) {
                    if (selected.length() > 0) { selected.append(","); }
                    selected.append(((Map) item).get("chartId"));
                }
                checked = selected.toString();
                indexColumns = checked;
            }

            String[] columns = indexColumns.split(",");
            List<Map<String, String>> indexInfoList = new ArrayList<>();
            List<String> types = new ArrayList<>();
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
                String type = String.valueOf(detail.get("INDEX_TYPE"));
                types.add(type == null || type.isEmpty() || "null".equals("type") ? "0" : type);
            }
            result.put("indexInfoList", indexInfoList);
            String unionSql =
                    IndexChartsHelper.createIndexUnionSQL(indexInfoList, columns, pageData, checked);
            StringBuilder sql = new StringBuilder(" SELECT ");
            for (int i = 0; i < columns.length; i++) {
                sql.append(" ROUND(COALESCE(tt.`").append(columns[i]).append("`, 0)");
                sql.append("0".equals(types.get(i)) ? "/" + pageData.getString("price")
                        : " * 100");
                sql.append(", 2) AS `").append(columns[i]).append("`,");
            }
            sql.append(" tt.ACCOUNT_DATE, tt.CODE, tt.GK FROM ( SELECT ");
            for (String column : columns) {
                sql.append(" COALESCE(SUM(bb.`").append(column).append("`), 0) AS `")
                        .append(column).append("`,");
            }
            boolean dateAxis = "0".equals(pageData.getString("xTurn"));
            if (dateAxis) {
                sql.append("bb.ACCOUNT_DATE, MIN(bb.CODE) AS CODE, MIN(bb.GK) AS GK ");
            } else {
                sql.append("MIN(bb.ACCOUNT_DATE) AS ACCOUNT_DATE, bb.CODE, "
                        + "MIN(bb.GK) AS GK ");
            }
            sql.append("FROM (").append(unionSql)
                    .append(") bb WHERE 1=1 AND bb.ACCOUNT_DATE >= '")
                    .append(pageData.getString("startDate"))
                    .append("' AND bb.ACCOUNT_DATE <= '")
                    .append(pageData.getString("endDate")).append("'");
            List<String> scale;
            if (dateAxis) {
                if (StringUtils.isNotEmpty(pageData.getString("direction"))) {
                    sql.append(" AND bb.CODE = '").append(pageData.getString("direction")).append("'");
                }
                sql.append(" GROUP BY bb.ACCOUNT_DATE ) tt "
                        + "ORDER BY tt.`ACCOUNT_DATE` ASC ");
                scale = IndexChartsHelper.setEChartsScale(pageData);
            } else {
                sql.append(" GROUP BY bb.CODE ) tt "
                        + "ORDER BY tt.`CODE` ASC ");
                String scaleString = "";
                if ("1".equals(pageData.getString("dimensionFlag"))) {
                    scaleString = indexSchemeService.getAllTrsInfo();
                }
                if ("2".equals(pageData.getString("dimensionFlag"))) {
                    scaleString = indexSchemeService.getAllAreaInfo();
                }
                if (StringUtils.isEmpty(scaleString)) { return null; }
                scale = Arrays.asList(scaleString.split(","));
            }
            result.put("x", scale);
            PageData query = new PageData();
            query.put("schemeSql", sql.toString());
            List<Map<String, Object>> rows = indexSchemeService.execSchemeSql(query);
            List<List<String>> data = new ArrayList<>();
            for (String column : columns) {
                List<String> values = new ArrayList<>();
                for (String node : scale) {
                    String value = "";
                    for (Map<String, Object> row : rows) {
                        if (node.equals(row.get("ACCOUNT_DATE")) || node.equals(row.get("CODE"))
                                || node.equals(row.get("GK"))) {
                            value = new BigDecimal(Double.valueOf(String.valueOf(row.get(column))))
                                    .setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        }
                    }
                    values.add(value);
                }
                data.add(values);
            }
            result.put("data", data);
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
