package org.seo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.seo.BaseController;
import org.seo.config.DataSourceContextHolder;
import org.seo.model.QueTreeNode;
import org.seo.service.ComprehensiveQueryService;
import org.seo.service.DimensionService;
import org.seo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Slf4j
@RestController
@Api(tags = "综合查询")
@RequestMapping(value = "/seoController", produces = MediaType.APPLICATION_JSON_VALUE)
public class ComprehensiveQueryController extends BaseController {

    @Autowired
    private ComprehensiveQueryService comprehensiveQueryService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @RequestMapping(value = {"/getSchemeMainPage"}, method = RequestMethod.POST)
    @ApiOperation("获取方案(分页)")
    public Object getSchemeMainPage() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            List<Map<String, Object>> result = comprehensiveQueryService.getSchemeMainPage(pd);
            Integer count = comprehensiveQueryService.countMain(pd);
            jsonMap.put("total", count);//total键 存放总记录数，必须的
            jsonMap.put("rows", result);//rows键 存放每页记录 list
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeMain"}, method = RequestMethod.POST)
    @ApiOperation("获取方案(不分页)")
    public Object getSchemeMain() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeMain(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeInfo"}, method = RequestMethod.POST)
    @ApiOperation("获取方案详情")
    public Object getSchemeInfo() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("table", comprehensiveQueryService.getSchemeTable(pd));
            jsonMap.put("column", comprehensiveQueryService.getSchemeColumn(pd));
            jsonMap.put("where", comprehensiveQueryService.getSchemeWhere(pd));
            jsonMap.put("order", comprehensiveQueryService.getSchemeOrder(pd));
            jsonMap.put("index", comprehensiveQueryService.getSchemeIndication(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/checkScheme"}, method = RequestMethod.POST)
    @ApiOperation("重名校验")
    public Object checkScheme() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.checkScheme(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getTableName"}, method = RequestMethod.POST)
    @ApiOperation("获取表名")
    public Object getTableName() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = new PageData();
            List<QueTreeNode> queTreeNode = new ArrayList<>();
            List<Map<String, Object>> list = comprehensiveQueryService.getTableName(pd);
            getTreeList(queTreeNode, list, null);
            jsonMap.put("rows", queTreeNode);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getColumn"}, method = RequestMethod.POST)
    @ApiOperation("获取列名")
    public Object getColumn() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            String TABLE_SIGN = pd.getString("TABLE_SIGN");
            if (null != TABLE_SIGN && !TABLE_SIGN.equals("")) {
                String[] TABLE_SIGNS = TABLE_SIGN.split("▲");
                pd.put("DATABASE_ID", TABLE_SIGNS[0]);
                pd.put("TABLE_SIGN", TABLE_SIGNS[1]);
            }
            List<Map<String, Object>> rows = comprehensiveQueryService.getColumn(pd);
            jsonMap.put("rows", rows);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }


    @RequestMapping(value = {"/editSchemeMain"}, method = RequestMethod.POST)
    @ApiOperation("修改方案")
    public Map<String, Object> editSchemeMain(@RequestParam(value = "ID", required = false) String ID,
                                              @RequestParam(value = "SCHEME_NAME", required = false) String SCHEME_NAME,
                                              @RequestParam(value = "table", required = false) String table,
                                              @RequestParam(value = "column", required = false) String column,
                                              @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "WHERE_LEFT", required = false) String WHERE_LEFT,
                                              @RequestParam(value = "WHERE_MIDDLE", required = false) String WHERE_MIDDLE,
                                              @RequestParam(value = "WHERE_RIGHT", required = false) String WHERE_RIGHT,
                                              @RequestParam(value = "WHERE_TYPE", required = false) String WHERE_TYPE,
                                              @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                              @RequestParam(value = "UPDATE_USER", required = false) String UPDATE_USER,
                                              @RequestParam(value = "SCHEME_MEMO", required = false) String SCHEME_MEMO,
                                              @RequestParam(value = "COUNT_TYPE", required = false) String COUNT_TYPE,
                                              @RequestParam(value = "COUNT_COLUMN", required = false) String COUNT_COLUMN,
                                              @RequestParam(value = "INDEX_NAME", required = false) String INDEX_NAME,
                                              @RequestParam(value = "INDEX_VALUE", required = false) String INDEX_VALUE,
                                              @RequestParam(value = "COLUMN_CN", required = false) String COLUMN_CN,
                                              @RequestParam(value = "DATA_TYPE", required = false) String dataType,
                                              @RequestParam(value = "IS_COUNT", required = false) String isCount,
                                              @RequestParam(value = "TIME_COLUMN", required = false) String timeColumn,
                                              @RequestParam(value = "TABLE_DSCR", required = false) String TABLE_DSCR,
                                              @RequestParam(value = "TABLE_USE", required = false) String TABLE_USE,
                                              @RequestParam(value = "SCHEME_COUNT", required = false) String SCHEME_COUNT,
                                              @RequestParam(value = "DIMENSION_COLUMN", required = false) String DIMENSION_COLUMN,
                                              @RequestParam(value = "DIMENSION_ID", required = false) String DIMENSION_ID) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData();
        result.put("msg", "修改成功！");
        result.put("result", "success");
        try {
            String[] tables = table.split("▲");
            String tt = table;
            pd.put("ID", tables[0]);
            Map<String, String> type = getType(pd);
            table = type.get("DBNAME") + "." + tables[1];

            pd.put("ID", ID);
            pd.put("SCHEME_NAME", SCHEME_NAME);
            pd.put("UPDATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.put("UPDATE_USER", UPDATE_USER);
            pd.put("SCHEME_MEMO", SCHEME_MEMO);
            pd.put("TABLE_ID", tables[0]);
            pd.put("TABLE_NAME", tables[1]);
            pd.put("IS_COUNT", isCount);
            pd.put("INDEX_VALUE", INDEX_VALUE);
            pd.put("TIME_COLUMN", timeColumn);

            String[] whereLeft = WHERE_LEFT.split(",");
            String[] whereMiddle = WHERE_MIDDLE.split(",");
            String[] whereRight = WHERE_RIGHT.split(",");
            String[] whereType = WHERE_TYPE.split(",");
            if (null == pageNo || pageNo == 0) {
                pageNo = 1;
                pageSize = 100;
            }
            String sql = createSelectSql(table, column, order, whereLeft, whereMiddle, whereRight, whereType, pageNo, pageSize, type.get("TYPE"), tables[0], DIMENSION_COLUMN, DIMENSION_ID, "");
            pd.put("SCHEME_SQL", sql);
            pd.put("SCHEME_COUNT", SCHEME_COUNT);
            comprehensiveQueryService.editSchemeMain(pd);

            PageData delPd = new PageData();
            delPd.put("SCHEME_ID", ID);
            comprehensiveQueryService.delSchemeTable(delPd);
            comprehensiveQueryService.delSchemeColumn(delPd);
            comprehensiveQueryService.delSchemeWhere(delPd);
            comprehensiveQueryService.delSchemeOrder(delPd);
            comprehensiveQueryService.delSchemeIndication(delPd);

            String tableId = addSchemeTable(pd.getString("ID"), tt, TABLE_DSCR, TABLE_USE);
            addSchemeColumn(pd.getString("ID"), tableId, column);
            addSchemeWhere(pd.getString("ID"), WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, "", WHERE_TYPE, dataType);
            addSchemeOrder(pd.getString("ID"), order, "");
            addSchemeIndication(pd.getString("ID"), COUNT_TYPE, COUNT_COLUMN, COLUMN_CN, INDEX_NAME);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    private Map<String, String> getType(PageData pd) {
        return comprehensiveQueryService.getType(pd);
    }

    @RequestMapping(value = {"/addSchemeMain"}, method = RequestMethod.POST)
    @ApiOperation("新增方案")
    public Map<String, Object> addSchemeMain(@RequestParam(value = "SCHEME_NAME", required = false) String SCHEME_NAME,
                                             @RequestParam(value = "table", required = false) String table,
                                             @RequestParam(value = "column", required = false) String column,
                                             @RequestParam(value = "order", required = false) String order,
                                             @RequestParam(value = "WHERE_LEFT", required = false) String WHERE_LEFT,
                                             @RequestParam(value = "WHERE_MIDDLE", required = false) String WHERE_MIDDLE,
                                             @RequestParam(value = "WHERE_RIGHT", required = false) String WHERE_RIGHT,
                                             @RequestParam(value = "WHERE_TYPE", required = false) String WHERE_TYPE,
                                             @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                             @RequestParam(value = "CREATE_USER", required = false) String CREATE_USER,
                                             @RequestParam(value = "SCHEME_MEMO", required = false) String SCHEME_MEMO,
                                             @RequestParam(value = "COUNT_TYPE", required = false) String COUNT_TYPE,
                                             @RequestParam(value = "COUNT_COLUMN", required = false) String COUNT_COLUMN,
                                             @RequestParam(value = "INDEX_NAME", required = false) String INDEX_NAME,
                                             @RequestParam(value = "INDEX_VALUE", required = false) String INDEX_VALUE,
                                             @RequestParam(value = "COLUMN_CN", required = false) String COLUMN_CN,
                                             @RequestParam(value = "DATA_TYPE", required = false) String dataType,
                                             @RequestParam(value = "IS_COUNT", required = false) String isCount,
                                             @RequestParam(value = "TIME_COLUMN", required = false) String timeColumn,
                                             @RequestParam(value = "TABLE_DSCR", required = false) String TABLE_DSCR,
                                             @RequestParam(value = "TABLE_USE", required = false) String TABLE_USE,
                                             @RequestParam(value = "SCHEME_COUNT", required = false) String SCHEME_COUNT,
                                             @RequestParam(value = "DIMENSION_COLUMN", required = false) String DIMENSION_COLUMN,
                                             @RequestParam(value = "DIMENSION_ID", required = false) String DIMENSION_ID) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData();
        result.put("msg", "新增成功！");
        result.put("result", "success");
        try {
            String[] tables = table.split("▲");
            String tt = table;
            pd.put("ID", tables[0]);
            Map<String, String> type = comprehensiveQueryService.getType(pd);
            table = type.get("DBNAME") + "." + tables[1];

            pd.put("ID", get32UUID());
            pd.put("CREATE_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.put("SCHEME_NAME", SCHEME_NAME);
            pd.put("CREATE_USER", CREATE_USER);
            pd.put("SCHEME_MEMO", SCHEME_MEMO);
            pd.put("TABLE_ID", tables[0]);
            pd.put("TABLE_NAME", tables[1]);
            pd.put("IS_COUNT", isCount);
            pd.put("INDEX_VALUE", INDEX_VALUE);
            pd.put("TIME_COLUMN", timeColumn);

            String[] whereLeft = new String[0];
            String[] whereMiddle = new String[0];
            String[] whereRight = new String[0];
            String[] whereType = new String[0];
            if (null != WHERE_LEFT && !WHERE_LEFT.equals("")) {
                whereLeft = WHERE_LEFT.split(",");
                whereMiddle = WHERE_MIDDLE.split(",");
                whereRight = WHERE_RIGHT.split(",");
                whereType = WHERE_TYPE.split(",");
            }
            if (null == pageNo || pageNo == 0) {
                pageNo = 1;
                pageSize = 100;
            }
            String sql = createSelectSql(table, column, order, whereLeft, whereMiddle, whereRight, whereType, pageNo, pageSize, type.get("TYPE"), tables[0], DIMENSION_COLUMN, DIMENSION_ID, "");
            pd.put("SCHEME_SQL", sql);
            pd.put("SCHEME_COUNT", SCHEME_COUNT);
            comprehensiveQueryService.addSchemeMain(pd);

            String tableId = addSchemeTable(pd.getString("ID"), tt, TABLE_DSCR, TABLE_USE);
            addSchemeColumn(pd.getString("ID"), tableId, column);
            addSchemeWhere(pd.getString("ID"), WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, "", WHERE_TYPE, dataType);
            addSchemeOrder(pd.getString("ID"), order, "");
            addSchemeIndication(pd.getString("ID"), COUNT_TYPE, COUNT_COLUMN, COLUMN_CN, INDEX_NAME);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    @RequestMapping(value = {"/delSchemeMain"}, method = RequestMethod.POST)
    @ApiOperation("删除方案")
    public Map<String, Object> delSchemeMain() {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData();
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            pd.put("SCHEME_ID", pd.get("ID"));
            comprehensiveQueryService.delSchemeTable(pd);
            comprehensiveQueryService.delSchemeColumn(pd);
            comprehensiveQueryService.delSchemeWhere(pd);
            comprehensiveQueryService.delSchemeOrder(pd);
            comprehensiveQueryService.delSchemeIndication(pd);
            comprehensiveQueryService.delSchemeMain(pd);
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    //    //list添加子节点
    private void getTreeList(List<QueTreeNode> treeList, List<Map<String, Object>> metaList, QueTreeNode temp) {
        Map<String, Object> scopedSlotsMap = new HashMap<>();
        scopedSlotsMap.put("title", "title");
        for (int i = 0; i < metaList.size(); i++) {
            String tempPid = metaList.get(i).get("pid").toString();
            QueTreeNode tree = new QueTreeNode();
            tree.setValue(metaList.get(i).get("id").toString());
            tree.setKey(metaList.get(i).get("id").toString());
            tree.setTitle(metaList.get(i).get("name").toString());
            tree.setLabel(metaList.get(i).get("name").toString());
            tree.setDisabled("0".equals(metaList.get(i).get("disabled").toString()) ? false : true);
            tree.setIsleaf(metaList.get(i).get("isleaf").toString());
            tree.setScopedSlots(scopedSlotsMap);
            tree.setDscr(metaList.get(i).get("dscr").toString());
            tree.setTable_en(metaList.get(i).get("table_en").toString());
            if (temp == null) {
                if (oConvertUtils.isEmpty(tempPid)) {
                    treeList.add(tree);
                    if (tree.getIsleaf().equals("0")) {
                        getTreeList(treeList, metaList, tree);
                    }
                }
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getValue())) {
                temp.getChildren().add(tree);
                if (tree.getIsleaf().equals("0")) {
                    getTreeList(treeList, metaList, tree);
                }
            }
        }
    }

    @RequestMapping(value = {"/executeSql"}, method = RequestMethod.POST)
    @ApiOperation("执行查询")
    public Object executeSql(@RequestParam(value = "table", required = false) String table,
                             @RequestParam(value = "column", required = false) String column,
                             @RequestParam(value = "order", required = false) String order,
                             @RequestParam(value = "WHERE_LEFT", required = false) String[] WHERE_LEFT,
                             @RequestParam(value = "WHERE_MIDDLE", required = false) String[] WHERE_MIDDLE,
                             @RequestParam(value = "WHERE_RIGHT", required = false) String[] WHERE_RIGHT,
                             @RequestParam(value = "WHERE_TYPE", required = false) String[] WHERE_TYPE,
                             @RequestParam(value = "pageNo", required = false) Integer pageNo,
                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                             @RequestParam(value = "userId", required = false) String userId,
                             @RequestParam(value = "IS_COUNT", required = false) String isCount,
                             @RequestParam(value = "TIME_COLUMN", required = false) String timeColumn,
                             @RequestParam(value = "DATA_TYPE", required = false) String[] dataType,
                             @RequestParam(value = "DIMENSION_COLUMN", required = false) String DIMENSION_COLUMN,
                             @RequestParam(value = "DIMENSION_ID", required = false) String DIMENSION_ID,
                             @RequestParam(value = "guokuId", required = false) String guokuId) {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = new PageData();
            if (null == table || table.equals("")) {
                jsonMap.put("result", "false");
                jsonMap.put("msg", "表名不能为空");
                return jsonMap;
            }
            String[] tables = table.split("▲");
            pd.put("ID", tables[0]);
            Map<String, String> type = getType(pd);
            table = type.get("DBNAME") + "." + tables[1];
            String sql = createSelectSql(table, column, order, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, pageNo, pageSize, type.get("TYPE"), tables[0], DIMENSION_COLUMN, DIMENSION_ID, guokuId);
            String countSql = createCountSql(table, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type.get("TYPE"));
            log.info("##############################" + sql + "##############################");
            pd.put("sql", sql);
            pd.put("countSql", countSql);
            List<Map<String, Object>> temp = comprehensiveQueryService.executeSql(pd, tables[0]);
            jsonMap.put("rows", temp);
            jsonMap.put("total", comprehensiveQueryService.countSql(pd, tables[0]));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/executeSqlFromFont"}, method = RequestMethod.POST)
    @ApiOperation("执行页面传sql的查询")
    public Object executeSqlFromFont() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            Map<String, Object> indicatorScheme = comprehensiveQueryService.getIndicatorScheme(pd.getString("ID"));
            if (indicatorScheme != null && !indicatorScheme.isEmpty()) {
                String schemeCondition = String.valueOf(indicatorScheme.get("SCHEME_CONDITON"));
                Map<String, Object> condition = JSONObject.parseObject(schemeCondition, Map.class);
                String scopedSql = IndicatorDataScopeSql.apply(
                        String.valueOf(indicatorScheme.get("SCHEME_SQL")),
                        String.valueOf(condition.get("dimensionFlag")),
                        getRequest().getHeader("X-Analysis-Subject-Code"),
                        getRequest().getHeader("X-Analysis-Guoku-Id"));
                pd.put("sql", scopedSql);
                pd.put("countSql", "SELECT COUNT(1) FROM (" + scopedSql + ") indicator_scope_count");
                jsonMap.put("total", comprehensiveQueryService.countSql(pd, pd.getString("TABLE_ID")));
            } else {
                jsonMap.put("total", pd.getString("SCHEME_COUNT"));
            }
            List<Map<String, Object>> temp = comprehensiveQueryService.executeSql(pd, pd.getString("TABLE_ID"));
            jsonMap.put("rows", temp);
            jsonMap.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/delRedis"}, method = RequestMethod.POST)
    @ApiOperation("删除内存")
    public Map<String, Object> delRedis() {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData();
        result.put("msg", "删除成功！");
        result.put("result", "success");
        try {
            String key = "benQi_" + pd.getString("userId");
            redisUtil.del(key, "shangQi_" + pd.getString("userId"), "tongQi_" + pd.getString("userId"));
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "false");
        }
        return result;
    }

    private String createSelectSql(String table,
                                   String column,
                                   String order,
                                   String[] WHERE_LEFT,
                                   String[] WHERE_MIDDLE,
                                   String[] WHERE_RIGHT,
                                   String[] WHERE_TYPE,
                                   Integer pageNo,
                                   Integer pageSize,
                                   String type,
                                   String DATABASE_ID,
                                   String DIMENSION_COLUMN,
                                   String DIMENSION_ID,
                                   String guokuId) {
        PageData pd = new PageData();
        String sql = "select ";

        String[] tables = table.split(",");
        String guoku = "";
        for (String t : tables) {
            PageData queryPd = new PageData();
            queryPd.put("TABLE_SIGN", t.substring(t.indexOf(".") + 1));
            queryPd.put("DATABASE_ID", DATABASE_ID);
            List<Map<String, Object>> columns = comprehensiveQueryService.getColumn(queryPd);
            for (Map<String, Object> c : columns) {
                String INPUT_TYPE = c.get("FIELD_SIGN").toString().substring(c.get("FIELD_SIGN").toString().indexOf("▲") + 1);
                if (null != INPUT_TYPE && INPUT_TYPE.equals("T")) {
                    guoku = c.get("FIELD_SIGN").toString().substring(0, c.get("FIELD_SIGN").toString().indexOf("▲"));
                }
            }
        }

        if (null != column && !column.equals("")) {
            String[] columns = column.split(",");
            String r = "";
            for (String c : columns) {
                r += c + " as " + "\"" + c + "\",";
            }
            r = r.substring(0, r.length() - 1);
            sql += r + " ";
        } else {
            String newColumn = "";
            for (String t : tables) {
                pd.put("TABLE_SIGN", t.substring(t.indexOf(".") + 1));
                pd.put("DATABASE_ID", DATABASE_ID);
                List<Map<String, Object>> columns = comprehensiveQueryService.getColumn(pd);
                for (Map<String, Object> c : columns) {
                    String FIELD_SIGN = c.get("FIELD_SIGN").toString().substring(0, c.get("FIELD_SIGN").toString().indexOf("▲"));
                    newColumn += FIELD_SIGN + " as " + "\"" + FIELD_SIGN + "\",";
                }
            }
            sql += newColumn.substring(0, newColumn.length() - 1);
        }
        sql += " from " + table;
        if (null != DIMENSION_COLUMN && !DIMENSION_COLUMN.equals("")) {
            sql += " inner join (select code from ods.seo_dimension_sub where main_id = '" + DIMENSION_ID + "')b on " + pd.get("TABLE_SIGN") + "." + DIMENSION_COLUMN + " = b.code";
        }
        if (!guoku.equals("")) {
            sql += " inner join (select CID from seo.cm_guoku_dimnsn where PID = '" + guokuId + "')c on " + guoku + " = c.CID";
        }
        sql += " where 1=1 ";

        sql = addWhereSql(sql, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type);
        if (null != order && !order.equals("")) {
            sql += " order by " + order.replace("▲", " ");
        }
        if (null != pageNo && null != pageSize) {
            if (type.equals("Mysql") || type.equals("Clickhouse") || type.equals("Vastbase") || type.equals("PostgreSQL")) {
                pageNo = (pageNo - 1) * pageSize;
                sql += " LIMIT " + pageSize + " OFFSET " + pageNo;
            }
            if (type.equals("DB2")) {
                pageNo = (pageNo - 1) * pageSize;
                sql = "SELECT * FROM (SELECT B.*, ROWNUMBER() OVER() AS RN FROM ( " + sql + " ) AS B " +
                        " )AS A WHERE A.RN BETWEEN " + pageNo + " AND " + (pageNo + pageSize);
            }
        }
        return sql;
    }

    private String createCountSql(String table,
                                  String[] WHERE_LEFT,
                                  String[] WHERE_MIDDLE,
                                  String[] WHERE_RIGHT,
                                  String[] WHERE_TYPE,
                                  String type) {
        PageData pd = new PageData();
        String countSql = "select count(1) ";
        countSql += " from " + table + " where 1=1 ";
        String sql = addWhereSql(countSql, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type);
        return sql;
    }

    private String addSchemeTable(String SCHEME_ID, String TABLE_NAME, String TABLE_DESC, String TABLE_USE) {
        PageData tablePd = new PageData();
        String id = get32UUID();
        tablePd.put("ID", id);
        tablePd.put("SCHEME_ID", SCHEME_ID);
        tablePd.put("TABLE_NAME", TABLE_NAME);
        tablePd.put("TABLE_DESC", TABLE_DESC);
        tablePd.put("TABLE_USE", TABLE_USE);
        comprehensiveQueryService.addSchemeTable(tablePd);
        return id;
    }

    private void addSchemeColumn(String SCHEME_ID, String TABLE_ID, String column) {
        PageData columnPd = new PageData();
        if (null != column && !column.equals("")) {
            columnPd.put("ID", get32UUID());
            columnPd.put("SCHEME_ID", SCHEME_ID);
            columnPd.put("TABLE_ID", TABLE_ID);
            columnPd.put("COLUMN_NAME", column);
            columnPd.put("COLUMN_DESC", "");
            comprehensiveQueryService.addSchemeColumn(columnPd);
        }
    }

    private void addSchemeWhere(String SCHEME_ID, String WHERE_LEFT, String WHERE_MIDDLE, String WHERE_RIGHT, String WHERE_MEMO, String WHERE_TYPE, String dataType) {
        if (null != WHERE_LEFT) {
            PageData wherePd = new PageData();
            wherePd.put("ID", get32UUID());
            wherePd.put("SCHEME_ID", SCHEME_ID);
            wherePd.put("WHERE_LEFT", WHERE_LEFT);
            wherePd.put("WHERE_MIDDLE", WHERE_MIDDLE);
            wherePd.put("WHERE_RIGHT", WHERE_RIGHT);
            wherePd.put("WHERE_MEMO", WHERE_MEMO);
            wherePd.put("DATA_TYPE", dataType);
            wherePd.put("WHERE_TYPE", WHERE_TYPE);
            comprehensiveQueryService.addSchemeWhere(wherePd);
        }
    }

    private void addSchemeOrder(String SCHEME_ID, String order, String ORDER_MEMO) {
        if (null != order && !order.equals("")) {
            PageData orderPd = new PageData();
            orderPd.put("ID", get32UUID());
            orderPd.put("SCHEME_ID", SCHEME_ID);
            orderPd.put("ORDER_DSCR", order);
            comprehensiveQueryService.addSchemeOrder(orderPd);
        }
    }

    private void addSchemeIndication(String SCHEME_ID, String COUNT_TYPE, String COUNT_COLUMN, String COLUMN_CN, String INDEX_NAME) {
        if (null != INDEX_NAME && !INDEX_NAME.equals("")) {
            PageData pd = new PageData();
            pd.put("ID", get32UUID());
            pd.put("SCHEME_ID", SCHEME_ID);
            pd.put("INDICATION_NAME", INDEX_NAME);
            pd.put("COUNT_TYPE", COUNT_TYPE);
            pd.put("COUNT_COLUMN", COUNT_COLUMN);
            pd.put("COLUMN_CN", COLUMN_CN);
            comprehensiveQueryService.addSchemeIndication(pd);
        }
    }

    private String addWhereSql(String sql, String[] WHERE_LEFT, String[] WHERE_MIDDLE, String[] WHERE_RIGHT, String[] WHERE_TYPE, String type) {
        if (null != WHERE_LEFT && WHERE_LEFT.length > 0) {
            for (int i = 0; i < WHERE_LEFT.length; i++) {
                if (WHERE_MIDDLE[i].startsWith("L:")) {
                    /**
                     * @Note: 根据字段长度查询，cuijiesheng
                     * @Date: 2020-11-24
                     */
                    if (WHERE_MIDDLE[i].startsWith("L:")) {
                        sql += " and LENGTH(" + WHERE_LEFT[i] + ") " + WHERE_MIDDLE[i].substring(WHERE_MIDDLE[i].indexOf("L:") + 2, WHERE_MIDDLE[i].length()) + " " + "'" + WHERE_RIGHT[i] + "'";
                    }
                } else {
                    if (WHERE_TYPE[i].equals("O")) {
                        if (WHERE_MIDDLE[i].equals("IN")) {
                            sql += " and " + WHERE_LEFT[i] + " " + WHERE_MIDDLE[i] + " " + WHERE_RIGHT[i].replace("@", ",");
                        } else {
                            sql += " and " + WHERE_LEFT[i] + " " + WHERE_MIDDLE[i] + " " + "'" + WHERE_RIGHT[i] + "'";
                        }

                    }
                    if (WHERE_TYPE[i].equals("N")) {
                        if (type.equals("Mysql") || type.equals("Vastbase") || type.equals("PostgreSQL")) {
                            sql += " and " + WHERE_LEFT[i] + " " + WHERE_MIDDLE[i] + " " + "'" + WHERE_RIGHT[i] + "'";
                        } else if (type.equals("Clickhouse")) {
                            sql += " and toDecimal128OrZero(" + WHERE_LEFT[i] + ",2) " + WHERE_MIDDLE[i] + " " + "toDecimal128OrZero('" + WHERE_RIGHT[i] + "',2) ";
                        } else {
                            sql += " and " + WHERE_LEFT[i] + " " + WHERE_MIDDLE[i] + " " + "'" + WHERE_RIGHT[i] + "'";
                        }
                    }
                    if (WHERE_TYPE[i].equals("B") || WHERE_TYPE[i].equals("T")) {
                        String[] temps = WHERE_RIGHT[i].split("▲");
                        String temp = "";
                        for (int j = 0; j < temps.length; j++) {
                            temp += "'" + temps[j] + "',";
                        }
                        temp = "(" + temp.substring(0, temp.length() - 1) + ")";
                        sql += " and " + WHERE_LEFT[i] + " " + WHERE_MIDDLE[i] + " " + temp;
                    }
                    if (WHERE_TYPE[i].equals("D")) {
                        String[] temps = WHERE_RIGHT[i].split("▲");
                        String temp = "'" + temps[0] + "' and " + "'" + temps[1] + "'";
                        String left = "";
                        if (type.equals("Mysql") || type.equals("Vastbase") || type.equals("PostgreSQL")) {
                            left = " CASE LENGTH(" + WHERE_LEFT[i] + ") " +
                                    " WHEN 10 THEN " +
                                    " REPLACE (" + WHERE_LEFT[i] + ", '/', '-') " +
                                    " WHEN 7 THEN " +
                                    " REPLACE (" + WHERE_LEFT[i] + ", '/', '-') " +
                                    " WHEN 8 THEN " +
                                    " concat_ws( " +
                                    " '-'," +
                                    " substring(" + WHERE_LEFT[i] + ", 1, 4), " +
                                    " substring(" + WHERE_LEFT[i] + ", 5, 6), " +
                                    " substring(" + WHERE_LEFT[i] + ", 7, 8) " +
                                    " ) " +
                                    " WHEN 6 THEN " +
                                    " concat_ws( " +
                                    " '-', " +
                                    " LEFT (" + WHERE_LEFT[i] + ", 4), " +
                                    " RIGHT (" + WHERE_LEFT[i] + ", 2) " +
                                    " )" +
                                    "END";
                        } else if (type.equals("Clickhouse")) {
                            left = " CASE " +
                                    " WHEN LENGTH(toString(" + WHERE_LEFT[i] + ")) = 10 THEN " +
                                    " REPLACE (toString(" + WHERE_LEFT[i] + "), '/', '-') " +
                                    " WHEN LENGTH(toString(" + WHERE_LEFT[i] + ")) = 7 THEN " +
                                    " REPLACE (toString(" + WHERE_LEFT[i] + "), '/', '-') " +
                                    " WHEN LENGTH(toString(" + WHERE_LEFT[i] + ")) = 8 THEN " +
                                    " concat( " +
                                    " substring(toString(" + WHERE_LEFT[i] + "), 1, 4), " +
                                    " '-', " +
                                    " substring(toString(" + WHERE_LEFT[i] + "), 5, 2), " +
                                    " '-', " +
                                    " substring(toString(" + WHERE_LEFT[i] + "), 7, 2) " +
                                    " ) " +
                                    "WHEN LENGTH(toString(" + WHERE_LEFT[i] + ")) = 6 THEN " +
                                    " concat( " +
                                    " LEFT (toString(" + WHERE_LEFT[i] + "), 4), " +
                                    " '-', " +
                                    " RIGHT (toString(" + WHERE_LEFT[i] + "), 2) " +
                                    " ) " +
                                    "END ";
                        } else if (type.equals("DB2")) {
                            left = " CASE LENGTH(trim(" + WHERE_LEFT[i] + ")) " +
                                    " WHEN 10 THEN " +
                                    " REPLACE (" + WHERE_LEFT[i] + ", '/', '-') " +
                                    " WHEN 7 THEN " +
                                    " REPLACE (" + WHERE_LEFT[i] + ", '/', '-') " +
                                    " WHEN 8 THEN " +
                                    " ( " +
                                    " substr(" + WHERE_LEFT[i] + ", 1, 4)|| " +
                                    " '-'|| " +
                                    " substr(" + WHERE_LEFT[i] + ", 5, 6)|| " +
                                    " '-'|| " +
                                    " substr(" + WHERE_LEFT[i] + ", 7, 8) " +
                                    " ) " +
                                    " WHEN 6 THEN " +
                                    " ( " +
                                    " substr(" + WHERE_LEFT[i] + ",1,4)|| " +
                                    " '-'|| " +
                                    " substr (" + WHERE_LEFT[i] + ",5,6) " +
                                    " ) " +
                                    "END";
                        }
                        sql += " and (" + left + " " + WHERE_MIDDLE[i] + " " + temp + ")";
                    }
                }
            }
        }
        return sql;
    }

    @RequestMapping(value = {"/getSchemeTable"}, method = RequestMethod.POST)
    @ApiOperation("获取方案对应表")
    public Object getSchemeTable() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeTable(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeColumn"}, method = RequestMethod.POST)
    @ApiOperation("获取方案对应列")
    public Object getSchemeColumn() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeColumn(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeWhere"}, method = RequestMethod.POST)
    @ApiOperation("获取方案对应查询条件")
    public Object getSchemeWhere() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeWhere(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeOrder"}, method = RequestMethod.POST)
    @ApiOperation("获取方案对应排序")
    public Object getSchemeOrder() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeOrder(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/getSchemeIndication"}, method = RequestMethod.POST)
    @ApiOperation("获取方案对应指标")
    public Object getSchemeIndication() {
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData();
            jsonMap.put("rows", comprehensiveQueryService.getSchemeIndication(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    @RequestMapping(value = {"/calculate"}, method = RequestMethod.POST)
    @ApiOperation("计算")
    public Object calculate(@RequestParam(value = "WHERE_LEFT", required = false) String[] WHERE_LEFT,
                            @RequestParam(value = "WHERE_MIDDLE", required = false) String[] WHERE_MIDDLE,
                            @RequestParam(value = "WHERE_RIGHT", required = false) String[] WHERE_RIGHT,
                            @RequestParam(value = "WHERE_TYPE", required = false) String[] WHERE_TYPE,
                            @RequestParam(value = "TIME_COLUMN", required = false) String timeColumn,
                            @RequestParam(value = "DATA_TYPE", required = false) String[] dataType,
                            @RequestParam(value = "COUNT_TYPE", required = false) String COUNT_TYPE,
                            @RequestParam(value = "COUNT_COLUMN", required = false) String COUNT_COLUMN,
                            @RequestParam(value = "COLUMN_CN", required = false) String COLUMN_CN,
                            @RequestParam(value = "INDEX_NAME", required = false) String INDEX_NAME,
                            @RequestParam(value = "tableName", required = false) String tableName) {
        Map<String, Object> jsonMap = new HashMap<>();
        Map<String, Object> bqMap = new HashMap<>();
        Map<String, Object> sqMap = new HashMap<>();
        Map<String, Object> tqMap = new HashMap<>();
        Map<String, Object> bqhjMap = new HashMap<>();
        Map<String, Object> sqhjMap = new HashMap<>();
        Map<String, Object> tqhjMap = new HashMap<>();
        Map<String, Object> bqAvgMap = new HashMap<>();
        Map<String, Object> sqAvgMap = new HashMap<>();
        Map<String, Object> tqAvgMap = new HashMap<>();
        Map<String, Object>[] tt = new Map[7];
        Integer bqCount = 0;
        Integer sqCount = 0;
        Integer tqCount = 0;
        PageData pd = new PageData();
        String[] tables = tableName.split("▲");
        pd.put("ID", tables[0]);
        Map<String, String> type = getType(pd);
        String table = type.get("DBNAME") + "." + tables[1];
        try {
            if (null != COUNT_TYPE && !COUNT_TYPE.equals("")) {
                String sql = "";
                String[] countTypes = COUNT_TYPE.split("▲");
                for (int i = 0; i < countTypes.length; i++) {
                    if (countTypes[i].equals("合计值")) {
                        //当期合计
                        sql = createSumSql(table, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        bqMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);
                        bqhjMap.put("sumnum", thousand(Double.parseDouble(null == bqMap ? "0" : bqMap.get("sumnum") + "")));

                        //上期合计
                        String[] shangqiRight = getSQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createSumSql(table, WHERE_LEFT, WHERE_MIDDLE, shangqiRight, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        sqMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);
                        sqhjMap.put("sumnum", thousand(Double.parseDouble(null == sqMap ? "0" : sqMap.get("sumnum") + "")));

                        //同期合计
                        String[] tongqiRight = getTQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createSumSql(table, WHERE_LEFT, WHERE_MIDDLE, tongqiRight, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        tqMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);
                        tqhjMap.put("sumnum", thousand(Double.parseDouble(null == tqMap ? "0" : tqMap.get("sumnum") + "")));

                        Map<String, Object> result = new HashMap<>();
                        result.put(COLUMN_CN + "本期合计", bqhjMap.get("sumnum"));
                        tt[0] = result;
                    }
                    //均值
                    if (countTypes[i].equals("均值")) {
                        //当期均值
                        sql = createAvgSql(table, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        bqAvgMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);
                        //上期均值
                        String[] shangqiRight = getSQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createAvgSql(table, WHERE_LEFT, WHERE_MIDDLE, shangqiRight, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        sqAvgMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);
                        //同期均值
                        String[] tongqiRight = getTQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createAvgSql(table, WHERE_LEFT, WHERE_MIDDLE, tongqiRight, WHERE_TYPE, type.get("TYPE"), COUNT_COLUMN);
                        pd.put("sql", sql);
                        tqAvgMap = comprehensiveQueryService.executeSql(pd, tables[0]).get(0);

                        Map<String, Object> result = new HashMap<>();
                        result.put(COLUMN_CN + "本期均值", null == bqAvgMap || bqAvgMap.isEmpty() ? "-" : bqAvgMap.get("avgnum"));
                        tt[0] = result;
                    }
                    //计数
                    if (countTypes[i].equals("计数")) {
                        //当期计数
                        sql = createCountSql(table, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type.get("TYPE"));
                        pd.put("countSql", sql);
                        bqCount = comprehensiveQueryService.countSql(pd, tables[0]);
                        //上期计数
                        String[] shangqiRight = getSQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createCountSql(table, WHERE_LEFT, WHERE_MIDDLE, shangqiRight, WHERE_TYPE, type.get("TYPE"));
                        pd.put("countSql", sql);
                        sqCount = comprehensiveQueryService.countSql(pd, tables[0]);
                        //同期计数
                        String[] tongqiRight = getTQDate(WHERE_LEFT, WHERE_RIGHT, timeColumn, dataType);
                        sql = createCountSql(table, WHERE_LEFT, WHERE_MIDDLE, tongqiRight, WHERE_TYPE, type.get("TYPE"));
                        pd.put("countSql", sql);
                        tqCount = comprehensiveQueryService.countSql(pd, tables[0]);

                        Map<String, Object> result = new HashMap<>();
                        result.put("本期计数", bqCount);
                        tt[0] = result;
                    }
                }
                if (null != INDEX_NAME && !"".equals(INDEX_NAME)) {
                    String[] indexs = INDEX_NAME.split("▲");
                    for (int i = 0; i < countTypes.length; i++) {
                        String[] index = indexs[i].split(",");
                        for (String in : index) {
                            if (in.equals("上期值")) {
                                if (countTypes[i].equals("合计值")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "上期合计", sqhjMap.get("sumnum"));
                                    tt[1] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "上期均值", null == sqAvgMap || sqAvgMap.isEmpty() ? "-" : thousand(Double.parseDouble(sqAvgMap.get("avgnum") + "")));
                                    tt[1] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put("上期计数", sqCount);
                                    tt[1] = result;
                                }
                            } else if (in.equals("去年同期值")) {
                                if (countTypes[i].equals("合计值")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "去年同期值合计", tqhjMap.get("sumnum"));
                                    tt[4] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "去年同期值均值", null == tqAvgMap || tqAvgMap.isEmpty() ? "-" : thousand(Double.parseDouble(tqAvgMap.get("avgnum") + "")));
                                    tt[4] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put("去年同期值计数", tqCount);
                                    tt[4] = result;
                                }
                            } else if (in.equals("环比增速")) {
                                double res = 0.0;
                                if (countTypes[i].equals("合计值")) {
                                    //本期合计值bqMap; 上期合计值sqMap;
                                    //环比即本期除上期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "合计环比增速", tbhb(bqMap, sqMap, false, "sumnum"));
                                    tt[3] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    //本期均值bqAvgMap; 上期均值sqAvgMap;
                                    //环比即本期除上期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "均值环比增速", tbhb(bqAvgMap, sqAvgMap, false, "avgnum"));
                                    tt[3] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    if (null == sqCount || sqCount == 0) {
                                        result.put("计数环比增速", "-");
                                    } else {
                                        res = ((Double.parseDouble(bqCount.toString()) - Double.parseDouble(sqCount.toString())) /
                                                Double.parseDouble(sqCount.toString())) * 100;
                                        result.put("计数环比增速", String.format("%.2f", res) + "%");
                                    }
                                    tt[3] = result;
                                }
                            } else if (in.equals("环比增量")) {
                                double res = 0.0;
                                if (countTypes[i].equals("合计值")) {
                                    //本期合计值bqMap; 上期合计值sqMap;
                                    //环比即本期除上期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "合计环比增量", tbhb(bqMap, sqMap, true, "sumnum"));
                                    tt[2] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    //本期均值bqAvgMap; 上期均值sqAvgMap;
                                    //环比即本期除上期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "均值环比增量", tbhb(bqAvgMap, sqAvgMap, true, "avgnum"));
                                    tt[2] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    if (null == sqCount || sqCount == 0) {
                                        result.put("计数环比增量", "-");
                                    } else {
                                        res = Double.parseDouble(bqCount.toString()) - Double.parseDouble(sqCount.toString());
                                        result.put("计数环比增量", thousand(res));
                                    }
                                    tt[2] = result;
                                }
                            } else if (in.equals("同比增速")) {
                                double res = 0.0;
                                if (countTypes[i].equals("合计值")) {
                                    //本期合计值bqMap; 同期合计值tqMap;
                                    //同比即本期除同期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "合计同比增速", tbhb(bqMap, tqMap, false, "sumnum"));
                                    tt[6] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    //本期均值bqAvgMap; 同期均值tqAvgMap;
                                    //同比即本期除同期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "均值同比增速", tbhb(bqAvgMap, tqAvgMap, false, "avgnum"));
                                    tt[6] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    if (null == tqCount || tqCount == 0) {
                                        result.put("计数同比增速", "-");
                                    } else {
                                        res = ((Double.parseDouble(bqCount.toString()) - Double.parseDouble(tqCount.toString())) /
                                                Double.parseDouble(tqCount.toString())) * 100;
                                        result.put("计数同比增速", String.format("%.2f", res) + "%");
                                    }
                                    tt[6] = result;
                                }
                            } else if (in.equals("同比增量")) {
                                double res = 0.0;
                                if (countTypes[i].equals("合计值")) {
                                    //本期合计值bqMap; 同期合计值tqMap;
                                    //同比即本期除同期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "合计同比增量", tbhb(bqMap, tqMap, true, "sumnum"));
                                    tt[5] = result;
                                }
                                if (countTypes[i].equals("均值")) {
                                    //本期均值bqAvgMap; 同期均值tqAvgMap;
                                    //同比即本期除同期
                                    Map<String, Object> result = new HashMap<>();
                                    result.put(COLUMN_CN + "均值同比增量", tbhb(bqAvgMap, tqAvgMap, true, "avgnum"));
                                    tt[5] = result;
                                }
                                if (countTypes[i].equals("计数")) {
                                    Map<String, Object> result = new HashMap<>();
                                    if (null == tqCount || tqCount == 0) {
                                        result.put("计数同比增量", "-");
                                    } else {
                                        res = Double.parseDouble(bqCount.toString()) - Double.parseDouble(tqCount.toString());
                                        result.put("计数同比增量", thousand(res));
                                    }
                                    tt[5] = result;
                                }
                            }
                        }
                    }
                }
                jsonMap.put(COLUMN_CN + "计算结果", tt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    private String createAvgSql(String table,
                                String[] WHERE_LEFT,
                                String[] WHERE_MIDDLE,
                                String[] WHERE_RIGHT,
                                String[] WHERE_TYPE,
                                String type,
                                String column) {
        PageData pd = new PageData();
        String countSql = "";
        if (type.equals("Mysql") || type.equals("Vastbase") || type.equals("PostgreSQL")) {
            countSql = "select avg(" + column + ") as avgnum ";
        }
        if (type.equals("Clickhouse")) {
//            countSql = "select avg(cast(" + column + " as decimal(28,2))) as avgnum ";
            countSql = "select avg(toDecimal128OrZero(" + column + ",2)) as avgnum ";
        }
        if (type.equals("DB2")) {
            countSql = "select avg(" + column + ") as avgnum ";
        }
        countSql += " from " + table + " where 1=1 ";
        String sql = addWhereSql(countSql, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type);
        return sql;
    }

    private String thousand(Double res) {
        BigDecimal a = new BigDecimal(res);
        DecimalFormat df = new DecimalFormat(",###,##0.00");
        return df.format(a);
    }

    private String tbhb(Map<String, Object> fz, Map<String, Object> fm, boolean isZl, String key) {
        double res = 0.0;
        if (null == fm || fm.isEmpty() || "0".equals(fm.get(key) + "") || "".equals(fm.get(key) + "") || "0.00".equals(fm.get(key) + "")) {
            return "-";
        }
        if (null == fz || fz.isEmpty() || "0".equals(fz.get(key) + "") || "".equals(fz.get(key) + "") || "0.00".equals(fz.get(key) + "")) {
            return "-";
        }
        if (null != fz.get(key)) {
            if (fm.get(key).toString().equals("0.0") || fm.get(key).toString().equals("0") || fm.get(key).toString().equals("")) {
                return "-";
            } else {
                if (isZl) {
                    res = Double.parseDouble(fz.get(key).toString()) - Double.parseDouble(fm.get(key).toString());
                    return thousand(res);
                } else {
                    res = ((Double.parseDouble(fz.get(key).toString()) - Double.parseDouble(fm.get(key).toString())) /
                            Double.parseDouble(fm.get(key).toString())) * 100;
                    return String.format("%.2f", res) + "%";
                }
            }
        }
        return "0.0";
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ApiOperation("下载")
    public void download(@RequestBody(required = false) JSONObject param, HttpServletResponse response) {
        PageData pd = this.getPageData(param);

        String table = pd.getString("table");
        String column = pd.getString("column");
        String order = pd.getString("order");
        String WHERE_LEFT = pd.getString("WHERE_LEFT");
        String WHERE_MIDDLE = pd.getString("WHERE_MIDDLE");
        String WHERE_RIGHT = pd.getString("WHERE_RIGHT");
        String WHERE_TYPE = pd.getString("WHERE_TYPE");

        String[] whereLeft = new String[0];
        String[] whereMiddle = new String[0];
        String[] whereRight = new String[0];
        String[] whereType = new String[0];
        if (null != WHERE_LEFT && !WHERE_LEFT.equals("")) {
            whereLeft = WHERE_LEFT.split(",");
            whereMiddle = WHERE_MIDDLE.split(",");
            whereRight = WHERE_RIGHT.split(",");
            whereType = WHERE_TYPE.split(",");
        }

        String[] tables = table.split("▲");
        pd.put("ID", tables[0]);
        Map<String, String> type = getType(pd);
        table = type.get("DBNAME") + "." + tables[1];
        String sql = createSelectSql(table, column, order, whereLeft, whereMiddle, whereRight, whereType, null, null, type.get("TYPE"), tables[0], pd.getString("DIMENSION_COLUMN"), pd.getString("DIMENSION_ID"), "");

        String countSql = "select count(1) from ( " + sql + " ) zz";
        pd.put("countSql", countSql);
        Integer count = comprehensiveQueryService.countSql(pd, tables[0]);

        int rowMaxCount = 60000;
        String filePath = saveDir + pd.getString("userId") + "/";
        String[] columns = pd.getString("COLUMN_EN").split(",");
        String[] columnCns = pd.getString("COLUMN_CN").split(",");
        String tableName = pd.getString("tableName");
        String tableName_en = pd.getString("tableName_en");

        if (null != count && count > rowMaxCount) {
            List<Map<String, Object>> list = new ArrayList();
            //1.设置相应头
            String filename = "导出_" + pd.getString("userId") + ".zip";
            try {
                filename = new String(filename.getBytes("GBK"), "iso-8859-1");
                response.reset();
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + filename);
                response.addHeader("pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                //2.设置批次文件名
                String fileSuff = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                fileSuff = sdf.format(new Date());
                String fileName = "导出_" + pd.getString("userId") + fileSuff;
                List<String> fileNames = new ArrayList<String>();  //存放生成的文件名称
                if (!new File(filePath).exists()) {
                    new File(filePath).mkdirs();
                }
                File zip = new File(filePath + fileName + ".zip");  //压缩文件路径
                //3.分批次生成excel
                int tempsize = (count % rowMaxCount) == 0 ? count / rowMaxCount : count / rowMaxCount + 1;
                int pageNo = 0;
                for (int i = 1; i <= tempsize; i++) {
                    System.out.println("开始第" + i + "个页签,当前页签从" + ((i - 1) * rowMaxCount + 1) + "到" + (i == tempsize ? count : i * rowMaxCount));
                    pageNo = ((i - 1) * rowMaxCount + 1);
                    String executeSql = sql + " LIMIT " + rowMaxCount + " OFFSET " + pageNo;
                    pd.put("sql", executeSql);
                    list = comprehensiveQueryService.executeSql(pd, tables[0]);

                    //3.2生成excel
                    String tempExcelFile = filePath + fileName + "[" + (i) + "].xlsx";
                    fileNames.add(tempExcelFile);
                    CreateExcel_2.createExcel("", filePath, fileName + "[" + (i) + "].xlsx", columnCns, columns, list, list.size(), tableName_en);
                    list.clear();
                }
                //4.导出zip压缩文件
                exportZip(response, fileNames, zip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pd.put("sql", sql);
            List<Map<String, Object>> temp = comprehensiveQueryService.executeSql(pd, tables[0]);
            String filename = "";
            if (null != temp && temp.size() > 0) {
                try {
                    filename = "export_" + pd.getString("userId") + ".xls";
                    CreateExcel_2.createExcel("", filePath, filename, columnCns, columns, temp, temp.size(), tableName_en);
                    FileDownload.fileDownload(response, filePath + filename, tableName + ".xls", this.getRequest());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件压缩并导出到客户端
     *
     * @param response
     * @param fileNames
     * @param zip
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void exportZip(HttpServletResponse response, List<String> fileNames, File zip)
            throws FileNotFoundException, IOException {
        OutputStream outPut = response.getOutputStream();

        //1.压缩文件
        File srcFile[] = new File[fileNames.size()];
        for (int i = 0; i < fileNames.size(); i++) {
            srcFile[i] = new File(fileNames.get(i));
        }
        byte[] byt = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zip));
        for (int i = 0; i < srcFile.length; i++) {
            FileInputStream in = new FileInputStream(srcFile[i]);
            out.putNextEntry(new ZipEntry(srcFile[i].getName()));
            int length;
            while ((length = in.read(byt)) > 0) {
                out.write(byt, 0, length);
            }
            out.closeEntry();
            in.close();
        }
        out.close();

        //2.删除服务器上的临时文件(excel)
        for (int i = 0; i < srcFile.length; i++) {
            File temFile = srcFile[i];
            if (temFile.exists() && temFile.isFile()) {
                temFile.delete();
            }
        }

        //3.返回客户端压缩文件
        FileInputStream inStream = new FileInputStream(zip);
        byte[] buf = new byte[4096];
        int readLenght;
        while ((readLenght = inStream.read(buf)) != -1) {
            outPut.write(buf, 0, readLenght);
        }
        inStream.close();
        outPut.close();

        //4.删除压缩文件
        if (zip.exists() && zip.isFile()) {
            zip.delete();
        }
    }

    private String createSumSql(String table,
                                String[] WHERE_LEFT,
                                String[] WHERE_MIDDLE,
                                String[] WHERE_RIGHT,
                                String[] WHERE_TYPE,
                                String type,
                                String column) {
        PageData pd = new PageData();
        String countSql = "";
        if (type.equals("Mysql") || type.equals("Vastbase") || type.equals("PostgreSQL")) {
            countSql = "select sum(" + column + ") as sumnum";
        }
        if (type.equals("Clickhouse")) {
//            countSql = "select sum(cast(" + column + "as decimal(28,2))) as sumnum";
            countSql = "select sum(toDecimal128OrZero(" + column + ",2)) as sumnum";
        }
        if (type.equals("DB2")) {
            countSql = "select sum(" + column + ") as sumnum ";
            countSql = "select sum(" + column + ") as sumnum ";
        }
        countSql += " from " + table + " where 1=1 ";
        String sql = addWhereSql(countSql, WHERE_LEFT, WHERE_MIDDLE, WHERE_RIGHT, WHERE_TYPE, type);
        return sql;
    }

    private String[] getTQDate(String[] WHERE_LEFT, String[] WHERE_RIGHT, String timeColumn, String[] dataType) {
        String[] tongQiLeft = WHERE_LEFT;
        String[] tongQiRight = new String[WHERE_RIGHT.length];
        System.arraycopy(WHERE_RIGHT, 0, tongQiRight, 0, WHERE_RIGHT.length);
        timeColumn = timeColumn.substring(0, timeColumn.indexOf("▲"));
        for (int i = 0; i < tongQiLeft.length; i++) {
            if (timeColumn.equals(tongQiLeft[i])) {
                String[] temps = tongQiRight[i].split("▲");
                String startLastMonthTime = "";
                String endLastMonthTime = "";
                if (dataType[i].equals("D")) {
                    //日
                    startLastMonthTime = DateUtil.dateAddString(temps[0], 1, -1, DateUtil.Pattern.YYYY_MM_DD);
                    endLastMonthTime = DateUtil.dateAddString(temps[1], 1, -1, DateUtil.Pattern.YYYY_MM_DD);
                }
                if (dataType[i].equals("M")) {
                    //月
                    startLastMonthTime = DateUtil.dateAddString(temps[0], 1, -1, DateUtil.Pattern.YYYY_MM);
                    endLastMonthTime = DateUtil.dateAddString(temps[1], 1, -1, DateUtil.Pattern.YYYY_MM);
                }
                if (dataType[i].equals("")) {
                    //季
                }
                if (dataType[i].equals("Y")) {
                    //年
                    startLastMonthTime = DateUtil.dateAddString(temps[0], 1, -1, DateUtil.Pattern.YYYY);
                    endLastMonthTime = DateUtil.dateAddString(temps[1], 1, -1, DateUtil.Pattern.YYYY);
                }
                String whereRight = startLastMonthTime + "▲" + endLastMonthTime;
                tongQiRight[i] = whereRight;
                break;
            }
        }
        return tongQiRight;
    }

    private String[] getSQDate(String[] WHERE_LEFT, String[] WHERE_RIGHT, String timeColumn, String[] dataType) {
        String[] shangQiLeft = WHERE_LEFT;
        String[] shangQiRight = new String[WHERE_RIGHT.length];
        System.arraycopy(WHERE_RIGHT, 0, shangQiRight, 0, WHERE_RIGHT.length);
        timeColumn = timeColumn.substring(0, timeColumn.indexOf("▲"));
        for (int i = 0; i < shangQiLeft.length; i++) {
            if (timeColumn.equals(shangQiLeft[i])) {
                String[] temps = shangQiRight[i].split("▲");
                String startLastMonthTime = "";
                String endLastMonthTime = "";
                if (dataType[i].equals("D")) {
                    //日
                    endLastMonthTime = DateUtil.dateAddString(temps[0], 5, -1, DateUtil.Pattern.YYYY_MM_DD);
                    if (temps[0].equals(temps[1])) {
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 5, -1, DateUtil.Pattern.YYYY_MM_DD);
                    } else {
                        Integer dif = DateUtil.differentDaysByMillisecond(temps[0], temps[1]) + 1;
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 5, -dif, DateUtil.Pattern.YYYY_MM_DD);
                    }

                }
                if (dataType[i].equals("M")) {
                    //月
                    endLastMonthTime = DateUtil.dateAddString(temps[0], 2, -1, DateUtil.Pattern.YYYY_MM);
                    if (temps[0].equals(temps[1])) {
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 2, -1, DateUtil.Pattern.YYYY_MM);
                    } else {
                        Integer dif = DateUtil.getMonthSpace(temps[0], temps[1]) + 1;
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 2, -dif, DateUtil.Pattern.YYYY_MM);
                    }

                }
                if (dataType[i].equals("")) {
                    //季
                }
                if (dataType[i].equals("Y")) {
                    //年
                    endLastMonthTime = DateUtil.dateAddString(temps[0], 1, -1, DateUtil.Pattern.YYYY);
                    if (temps[0].equals(temps[1])) {
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 1, -1, DateUtil.Pattern.YYYY);
                    } else {
                        Integer dif = Integer.parseInt(temps[1]) - Integer.parseInt(temps[0]) + 1;
                        startLastMonthTime = DateUtil.dateAddString(temps[0], 1, -dif, DateUtil.Pattern.YYYY);
                    }
                }
                String whereRight = startLastMonthTime + "▲" + endLastMonthTime;
                shangQiRight[i] = whereRight;
                break;
            }
        }
        return shangQiRight;
    }

    @Autowired
    private DimensionService dimensionService;

    @RequestMapping(value = {"/getMainAll"}, method = RequestMethod.POST)
    @ApiOperation("自定义维度信息(不分页)")
    public Object getDataSource(@RequestBody(required = false) JSONObject param) {
        //设置数据源
        DataSourceContextHolder.setDBType("default");
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            PageData pd = this.getPageData(param);
            jsonMap.put("rows", dimensionService.getMainAll(pd));
            jsonMap.put("result", "success");
        } catch (Exception e) {
            jsonMap.put("result", "false");
            jsonMap.put("msg", e.getMessage());
        }
        return jsonMap;
    }
}
