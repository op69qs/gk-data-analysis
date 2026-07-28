package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.seo.BaseController;
import org.seo.config.DataSourceContextHolder;
import org.seo.service.DataAuxiliaryService;
import org.seo.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "数据源相关下拉选")
@RequestMapping(value = "/dataAuxiliaryController", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataAuxiliaryController extends BaseController {

    @Autowired
    private DataAuxiliaryService dataAuxiliaryService;

    /**
     * 获取数据表一级分类下拉选
     *
     * @return
     */
    @ApiOperation(value = "获取数据表一级分类下拉选")
    @PostMapping(value = "/getFirstClassifySelection")
    public Map<String, Object> getFirstClassifySelection() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> dataList = dataAuxiliaryService.getFirstClassifySelection();
            result.put("result", "success");
            result.put("msg", "获取数据表一级分类下拉选成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "获取数据表一级分类下拉选失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 获取数据表二级分类下拉选
     *
     * @return
     */
    @ApiOperation(value = "获取数据表二级分类下拉选")
    @PostMapping(value = "/getSecondClassifySelection")
    public Map<String, Object> getSecondClassifySelection() {
        Map<String, Object> result = new HashMap<>();
        PageData pd =this.getPageData();
        try {
            List<Map<String, Object>> dataList = dataAuxiliaryService.getSecondClassifySelection(pd);
            result.put("result", "success");
            result.put("msg", "获取数据表二级分类下拉选成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "获取数据表二级分类下拉选失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 获取数据配置的数据源下拉选
     *
     * @return
     */
    @ApiOperation(value = "获取数据配置的数据源下拉选")
    @PostMapping(value = "/getDataSourceSelection")
    public Map<String, Object> getDataSourceSelection() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, Object>> dataList = dataAuxiliaryService.getDataSourceSelection();
            result.put("result", "success");
            result.put("msg", "获取数据配置的数据源下拉选成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "获取数据配置的数据源下拉选失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 根据数据源查询数据源下的数据库下拉选
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "根据数据源查询数据源下的数据库下拉选")
    @PostMapping(value = "/getDataBaseSelection")
    public Map<String, Object> getDataBaseSelection(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            List<Map<String, Object>> dataList = dataAuxiliaryService.getDataBaseSelection(pageData);
            result.put("result", "success");
            result.put("msg", "根据数据源查询数据源下的数据库下拉选成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "根据数据源查询数据源下的数据库下拉选失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 根据数据源和数据库查询数据库下的数据表下拉选
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "根据数据源和数据库查询数据库下的数据表下拉选")
    @PostMapping(value = "/getDataTableSelection")
    public Map<String, Object> getDataTableSelection(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        normalizeVastbaseSchema(pageData);
        try {
            List<Map<String, Object>> newDataList = new ArrayList<>();
            List<Map<String, Object>> dataList = dataAuxiliaryService.getDataTableSelection(pageData);
            //由于数据源不同，所以需要将大写字段转为小写
            for (Map<String, Object> map : dataList) {
                Map<String, Object> newMap = new HashMap<>();
                for (String key : map.keySet()) {
                    newMap.put(key.toLowerCase(), map.get(key));
                }
                newDataList.add(newMap);
            }
            result.put("result", "success");
            result.put("msg", "根据数据源和数据库查询数据库下的数据表下拉选成功");
            result.put("rows", newDataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "根据数据源和数据库查询数据库下的数据表下拉选失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 根据选择的数据表据查询该数据表/字段注释
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "根据选择的数据表据查询该数据表注释")
    @PostMapping(value = "/getDataTableComments")
    public Map<String, Object> getDataTableComments(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        normalizeVastbaseSchema(pageData);
        try {
            String tableName = ""; //表描述
            Map<String, Object> removeMap = new HashMap<>();
            List<Map<String, Object>> newDataList = new ArrayList<>();
            List<Map<String, Object>> dataList = dataAuxiliaryService.getDataTableComments(pageData);
            //由于数据源不同，所以需要将大写字段转为小写
            for (Map<String, Object> map : dataList) {
                Map<String, Object> newMap = new HashMap<>();
                for (String key : map.keySet()) {
                    newMap.put(key.toLowerCase().replace("name", "Name").replace("comment", "Comment"), map.get(key));
                    if (pageData.getString("TABLE_SIGN").equals(newMap.get("columnName"))) {
                        removeMap.putAll(newMap);
                    }
                    newMap.put("DBTIT","O");
                }
                newDataList.add(newMap);
            }
            newDataList.remove(removeMap); //删除表名对应的list元素
            result.put("result", "success");
            result.put("msg", "根据选择的数据表据查询该数据表成功");
            result.put("TABLE_NAME", removeMap.get("columnComment")); //表描述
            result.put("rows", newDataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "根据选择的数据表据查询该数据表失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 兼容旧页面仍把第二级选择值放在DATABASE中的请求格式。
     */
    private void normalizeVastbaseSchema(PageData pageData) {
        if (!"Vastbase".equals(pageData.getString("BASE_TYPE"))) {
            return;
        }
        String schemaName = pageData.getString("SCHEMA_NAME");
        if (schemaName == null || schemaName.trim().isEmpty()) {
            pageData.put("SCHEMA_NAME", pageData.getString("DATABASE"));
        }
    }

}
