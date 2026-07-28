package org.seo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.seo.BaseController;
import org.seo.config.DataSourceContextHolder;
import org.seo.service.DataAuxiliaryService;
import org.seo.service.DataTableService;
import org.seo.util.DateUtil;
import org.seo.util.IndexTreeNode;
import org.seo.util.PageData;
import org.seo.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@Api(tags = "数据表维护")
@RequestMapping(value = "/dataTableController", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataTableController extends BaseController {

    @Autowired
    private DataTableService dataTableService;

    @Autowired
    private DataAuxiliaryService dataAuxiliaryService;

    /**
     * 查询数据表Tree
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询数据表Tree")
    @PostMapping(value = "/getDataSourceTree")
    public Map<String, Object> getDataSourceTree(@ApiParam(value =
            "id: Tree节点ID\n" +
                    "parentId: Tree父节点ID\n" +
                    "tableName: 数据表\n") @RequestBody JSONObject jsonObject) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            if (oConvertUtils.isNotEmpty(pageData.get("tableName"))) { //若数据表关键字查询不为空
                pageData.put("tableName", pageData.getString("tableName").split(",")); //按逗号截取字符串
            }
            List<IndexTreeNode> treeNodeList = dataTableService.getDataSourceTree(pageData);
            result.put("result", "success");
            result.put("msg", "查询数据表Tree成功");
            result.put("rows", treeNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询数据表Tree失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 查询数据源关系表Tree
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询数据源关系表Tree")
    @PostMapping(value = "/getRelationTree")
    public Map<String, Object> getRelationTree(@ApiParam(value =
            "id: Tree节点ID\n" +
                    "parentId: Tree节点父ID\n" +
                    "tableName: 数据表\n") @RequestBody JSONObject jsonObject) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            if (oConvertUtils.isNotEmpty(pageData.get("tableName"))) { //若数据表关键字查询不为空
                pageData.put("tableName", pageData.getString("tableName").split(",")); //按逗号截取字符串
            }
            List<Map<String, Object>> treeNodeList = dataTableService.getRelationTree(pageData);
            result.put("result", "success");
            result.put("msg", "查询数据源关系表Tree成功");
            result.put("rows", treeNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询数据源关系表Tree失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 新增数据表
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "新增数据表")
    @PostMapping(value = "/addDataTable")
    public Map<String, Object> addDataTable(@RequestBody JSONObject jsonObject) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            if (dataTableService.getTableCount(pageData) > 0) { //先校验数据表是否已维护
                result.put("result", "false");
                result.put("msg", "数据表已维护,请重新选择");
            } else {
                String tableId = this.get32UUID();
                dataTableService.addDataTable(getTableList(tableId, pageData));
                result.put("result", "success");
                result.put("msg", "新增数据表成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "新增数据表失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 编辑数据表
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "编辑数据表")
    @PostMapping(value = "/editDataTable")
    public Map<String, Object> editDataTable(@RequestBody JSONObject jsonObject) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            String tableId = pageData.getString("TABLE_ID");
            pageData.put("date", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            dataTableService.editDataTable(tableId, getTableList(tableId, pageData));
            result.put("result", "success");
            result.put("msg", "编辑数据表成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "编辑数据表失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 获取数据表信息
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "获取数据表信息")
    @PostMapping(value = "/getDataTableData")
    public Map<String, Object> getDataTableData(@RequestBody JSONObject jsonObject) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            List<Map<String, Object>> dataList = dataTableService.getRelationData(pageData);
            List<Map<String, Object>> columnList = getColumnList(dataList, pageData);//字段描述集合
            result.put("result", "success");
            result.put("msg", "获取数据表信息成功");
            result.put("rows", dataList);
            result.put("columns", columnList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "获取数据表信息失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 将页面的参数处理为list类型
     *
     * @param tableId
     * @param pageData
     * @return
     */
    private List<Map<String, Object>> getTableList(String tableId, PageData pageData) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        String date = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        List<Map<String, Object>> list = new ArrayList<>();
        String[] columns = pageData.get("COLUMNS").toString().replaceAll("},", "}▲")
                .replace("[", "").replace("]", "").split("▲");
        pageData.remove("COLUMNS");
        for (String column : columns) {
            Map<String, Object> columnMap = (Map<String, Object>) JSON.parse(column);
            columnMap.put("FIELD_SIGN", columnMap.get("columnName")); //列代码
            columnMap.put("FIELD_NAME", columnMap.get("columnComment")); //列名称
            columnMap.put("INPUT_TYPE", columnMap.get("DBTIT")); //输入类型
            columnMap.remove("columnName");
            columnMap.remove("columnComment");
            columnMap.remove("DBTIT");
            columnMap.put("TABLE_ID", tableId);
            columnMap.put("date", date);
            for (Object key : pageData.keySet()) {
                columnMap.put(key.toString(), pageData.get(key));
            }
            list.add(columnMap);
        }
        return list;
    }

    /**
     * 原始数据表字段有变更时可以确保查出最新字段描述
     *
     * @param dataList
     * @param pageData
     */
    private List<Map<String, Object>> getColumnList(List<Map<String, Object>> dataList, PageData pageData) {

        if (null == dataList || dataList.size()<1 ){
            return null;
        }
        Map<String, Object> tableDataMap = dataList.get(0); //存放表数据集合

        //查询数据源信息
        Map<String, Object> sourceMap = dataAuxiliaryService.getDataSourceInfo(tableDataMap).get(0);
        pageData.put("BASE_TYPE", sourceMap.get("TYPE").toString());
        pageData.put("DATABASE", sourceMap.get("DBNAME").toString());
        pageData.put("SCHEMA_NAME", sourceMap.get("NAMESPACE").toString());
        pageData.put("TABLE_SIGN", tableDataMap.get("TABLE_SIGN").toString());
        pageData.put("SOURCE_ID", tableDataMap.get("DATABASE_ID").toString());

        //查询数据表字段的接口查询表字段注释接口，并原始数据表中的字段描述放入newColumnMap
        Map<String, Object> newColumnMap = new LinkedHashMap<>();
        List<Map<String, Object>> commentsList = dataAuxiliaryService.getDataTableComments(pageData);
        for (int i = 0; i < commentsList.size(); i++) {
            Map<String, Object> comments = commentsList.get(i);
            if (!pageData.getString("TABLE_SIGN").equals(comments.get("columnName").toString())) {
                newColumnMap.put(comments.get("columnName").toString(), (comments.get("columnComment") == null ? "" : comments.get("columnComment")));
            }
        }

        //创建新的字段描述Map，将字段描述放入columnList
        Map<String, Object> oldColumnMap = new HashMap<>(); //存放已保存的字段描述
        Map<String, Object> columnFlagMap = new HashMap<>(); //存放已保存的字段标识
        for (Map<String, Object> map : dataList) {
            //原始数据表是否还存在已保存的字段,若存在则保存循环的字段描述
            if (newColumnMap.containsKey(map.get("FIELD_SIGN").toString())) {
                oldColumnMap.put(map.get("FIELD_SIGN").toString(), map.get("FIELD_NAME"));
                columnFlagMap.put(map.get("FIELD_SIGN").toString(), map.get("INPUT_TYPE"));
            }
        }

        //遍历原始数据表字段描述，有新增则添加，无新增则保留原有字段描述，最后放入columnList
        List<Map<String, Object>> columnList = new ArrayList<>(); //字段描述list
        for (String key : newColumnMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            if (oldColumnMap.containsKey(key)) {
                map.put("columnName", key);
                map.put("columnComment", oldColumnMap.get(key));
                map.put("DBTIT", columnFlagMap.get(key));
                columnList.add(map);
            } else {
                map.put("columnName", key);
                map.put("columnComment", newColumnMap.get(key));
                map.put("DBTIT", "");
                columnList.add(map);
            }
        }

        //先清空原因的数据List，再放入新的数据
        dataList.clear();
        tableDataMap.remove("FIELD_SIGN"); //字段名
        tableDataMap.remove("FIELD_NAME"); //字段注释
        tableDataMap.remove("INPUT_TYPE"); //字段标识
        dataList.add(tableDataMap);

        return columnList;
    }

}
