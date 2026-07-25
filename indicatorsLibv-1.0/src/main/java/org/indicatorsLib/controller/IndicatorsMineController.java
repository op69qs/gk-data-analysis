// IndicatorsMineNewController.java

package org.indicatorsLib.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.indicatorsLib.BaseController;
import org.indicatorsLib.service.IndicatorsMineService;
import org.indicatorsLib.util.*;
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

/**
 * 我的指标
 *
 * @author Created by Samer on 2019/12/30.
 */

@Slf4j
@Api(tags = "指标管理")
@RestController
@RequestMapping(value = "/indicatorsMine", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndicatorsMineController extends BaseController {

    @Autowired
    private IndicatorsMineService indicatorsMineService;
    /**
     * 获取指标父级信息
     * @param jsonObject
     * @return
     */
    @PostMapping("/getIndexParentInfo")
    @ApiOperation("获取指标父级信息")
    public Map<String, Object> getIndexParentInfo(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = applyCurrentUser(this.getPageData(jsonObject));
            List<Map<String, Object>> dataList = indicatorsMineService.getIndexParentInfo(pageData);
            List<TreeNode> treeNodeList = TreeFilterHeaper.definedTreeFilter(dataList);
            result.put("rows", treeNodeList);
            result.put("result", "success");
            result.put("msg", "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询失败");
        }
        return result;
    }

    @PostMapping("/getIndexDimnsn")
    @ApiOperation("查询指标的维度")
    public Map<String, Object> getIndexDimnsn(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = applyCurrentUser(this.getPageData(jsonObject));
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> dataList = indicatorsMineService.getIndexDimnsn(pageData.getString("INDEX_ID").split(","));
            for (Map<String, Object> map : dataList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("id", map.get("INDEX_DIMNSN"));
                dataMap.put("label", map.get("INDEX_DIMNSN_DSCR"));
                data.add(dataMap);
            }
            result.put("rows", data);
            result.put("result", "success");
            result.put("msg", "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询失败");
        }
        return result;
    }

    @PostMapping("/getIndexPeriod")
    @ApiOperation("查询指标的周期")
    public Map<String, Object> getIndexPeriod(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = applyCurrentUser(this.getPageData(jsonObject));
            List<Map<String, Object>> data = new ArrayList<>();
            List<Map<String, Object>> dataList = indicatorsMineService.getIndexPeriod(pageData.getString("INDEX_ID").split(","));
            for (Map<String, Object> map : dataList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("id", map.get("INDEX_PERIOD"));
                dataMap.put("label", map.get("INDEX_PERIOD_DSCR"));
                data.add(dataMap);
            }
            result.put("rows", data);
            result.put("result", "success");
            result.put("msg", "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询失败");
        }
        return result;
    }
    /**
     * 指标管理指标查询
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "指标管理指标查询")
    @PostMapping(value = "/getIndexManageList")
    public Map<String, Object> getIndexManageList(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = applyCurrentUser(this.getPageData(param));
        pd.put("PERSONAL_FLAG", "0");
        Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
        pd.put("page", pageNo);
        pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
        try {
            List<Map<String, Object>> data = indicatorsMineService.getIndexManageList(pd);
            int count = indicatorsMineService.getIndexManageCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("result", "success");
            result.put("msg", "查询个人指标成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询个人指标失败");
  }
        return result;
    }
 /**
     * 公共指标管理指标查询
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "公共指标管理指标查询")
    @PostMapping(value = "/getPublicIndexManageList")
    public Map<String, Object> getPublicIndexManageList(
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = applyCurrentUser(this.getPageData(param));
        pd.put("PERSONAL_FLAG", "1");
        if (pd.get("C_BDGLEVEL") != null) {
            pd.put("C_BDGLEVEL", pd.getString("C_BDGLEVEL").split(","));
        }
        if (pd.get("JURISDICTION") != null) {
            pd.put("JURISDICTION", pd.getString("JURISDICTION").split(","));
        }
        Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
        pd.put("page", pageNo);
        pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
        try {
            List<Map<String, Object>> data = indicatorsMineService.getIndexManageList(pd);
            int count = indicatorsMineService.getIndexManageCount(pd);
            result.put("total", count);//total键 存放总记录数，必须的
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询公共指标成功");
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询公共指标失败");
        }
        return result;
    }
      
 
	

    /**
     * 我的指标新增
     *
     * @return
     */
    @PostMapping(value = "/addMineNew")
    @ApiOperation(value = "我的指标新增")
    public Map<String, Object> addMineNew(
            @ApiParam(value =
                    "INDEX_NAME: 指标名称,\n" +
                            "INDEX_DESCR: 指标描述\n" +
                            "INDEX_TYPE: 指标类型\n" +
                            "INDEX_DIMNSN:维      度\n" +
                            "INDEX_PERIOD:周      期\n" +
                            "BUILD_TYPE: 指标组装类型\n" +
                            "ORIGINAL_DSCR:原始运算表达式\n" +
                            "ORIGINAL_DSCR_ARRY:原始表达式数组/SQL字段\n" +
                            "INDEX_DSCR: 指标ID运算表达式\n" +
                            "INDEX_DSCR_ARRY: 指标表达式数组/SQL条件\n" +
                            "INDEX_DETAILS: 指标详细描述\n" +
                            "HTML_STR: 指标计算公式Html\n" +
                            "ADD_USERID: 当前用户ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = applyCurrentUser(this.getPageData(param));
        pd.put("INDEX_ID", this.get32UUID());
        String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String exeDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);

        String seq = String.valueOf(Integer.parseInt(indicatorsMineService.getMaxSeq(null).get("SEQ").toString()) + 1000000 + 1);
        String IDENTITY_PROPERTY = seq.substring(1, seq.length());

        String tableName = "lib_indicators_" + IDENTITY_PROPERTY;
        pd.put("INDEX_CORRE_TABLE", tableName);
        pd.put("PERSONAL_FLAG", "0");
        pd.put("IDENTITY_PROPERTY", IDENTITY_PROPERTY);
        pd.put("ADD_DATE", dateNow);

        String[] indexType = pd.getString("INDEX_PERIOD").split(",");
        String EXE_DEL = "DELETE FROM indicators_lib." + tableName + " WHERE INDEX_ID ='" + pd.getString("INDEX_ID") + "' AND (";
//        String EXE_WHERE = " INDEX_ID ='" + pd.getString("INDEX_ID") + "'AND (";
        for (int i = 0; i < indexType.length; i++) {
            String dateStr = "";
            switch (indexType[i]) {
                case ("1"):
                    dateStr = "to_char(@DATA_DATE, 'YYYY-MM-DD')";
                    break;
                case ("2"):
                    dateStr = "to_char(@DATA_DATE, 'YYYY-MM')";
                    break;
                case ("3"):
                    dateStr = "CONCAT(EXTRACT(YEAR FROM @DATA_DATE),'Q',EXTRACT(QUARTER FROM @DATA_DATE))";
                    break;
                case ("4"):
                    dateStr = "to_char(@DATA_DATE, 'YYYY')";
                    break;
            }
            EXE_DEL += "ACCOUNT_PERIOD = " + dateStr;
//            EXE_WHERE += "ACCOUNT_PERIOD = " + dateStr;
            if (indexType.length - 1 > i) {
                EXE_DEL += " OR ";
//                EXE_WHERE += " OR ";
            }
        }
        EXE_DEL += ")";
//        EXE_WHERE += ")";
        String EXE_INSERT =
                "INSERT INTO " + tableName + "(" +
                        "INDEX_ID," +
                        "ACCOUNT_PERIOD," +
                        "INDEX_DIM_CODE," +
                        "INDEX_DIM_DESCR," +
                        "DIMENSION_FLAG," +
                        "PERIOD_FLAG," +
                        "ADD_USERID," +
                        "ADD_DATE," +
                        "INDEX_VALUE" +
                        ")";
        //组装类型BUILD_TYPE     0：计算公式组装，1：自定义SQL组装
        String EXE_SQL = "0".equals(pd.getString("BUILD_TYPE")) ? analysisSTMT(pd) : analysisSQL(pd);
        pd.put("EXE_DEL", EXE_DEL);
        pd.put("EXE_INSERT", EXE_INSERT);
        pd.put("EXE_SQL", EXE_SQL);
//        pd.put("EXE_WHERE", EXE_WHERE);
        pd.put("IS_USABLE", "0");
        pd.put("exeDate", exeDate);

        try {

            /*创建指标事实表*/
            indicatorsMineService.createIndexTable(pd);
            /*指标对应数据表*/
            indicatorsMineService.addMineNewRelation(pd);
            /*增加公式信息*/
            indicatorsMineService.addFormula(pd);
            /*新增指标和个人关系表信息*/
            pd.put("ID", this.get32UUID());
            indicatorsMineService.addIndexUser(pd);
            /*手动调用加工逻辑*/
            indicatorsMineService.callExeFormulaHand(pd);

            res.put("result", "success");
            res.put("msg", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", "新增失败");
        }
        return res;
    }

    /**
     * 指标新增SQL试运行
     *
     * @return
     */
    @PostMapping(value = "/pilotRunSQL")
    @ApiOperation(value = "试运行指标SQL")
    public Map<String, Object> pilotRunSQL(@ApiParam(value =
            "ORIGINAL_DSCR_ARRY:原始表达式数组/SQL字段\n" +
                    "INDEX_DSCR_ARRY: 指标表达式数组/SQL条件\n") @RequestBody JSONObject param) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = applyCurrentUser(this.getPageData(param));

        try {
            String dateNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
            if (oConvertUtils.isEmpty(pd.get("pageNo"))) {
                pd.put("pageNo", "1");
            }
            if (oConvertUtils.isEmpty(pd.get("pageSize"))) {
                pd.put("pageSize", "10");
            }
            Integer pageNo = (Integer.parseInt(pd.getString("pageNo")) - 1) * Integer.parseInt(pd.getString("pageSize"));
            pd.put("page", pageNo);
            pd.put("rows", Integer.parseInt(pd.getString("pageSize")));
            pd.put("ADD_DATE", dateNow);
            //组装试运行的SQL
            String runSQL = analysisSQL(pd);
            String[] sqlResult = indicatorsMineService.pilotRunSQL(runSQL);
            if ("false".equals(sqlResult[0])) { //sql试运行出错
                res.put("result", "false");
                res.put("msg", sqlResult[1]);
                return res;
            }
            //组装返回查询列表的SQL
            runSQL = analysisSQLData(pd) + " LIMIT 1";
            List<Map<String, Object>> dataList = indicatorsMineService.selectDataBySQL(runSQL);
            res.put("result", "success");
            res.put("total", sqlResult[1]);//total键 存放总记录数，必须的
            res.put("rows", dataList);
            res.put("msg", "SQL试运行成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", e.getCause().getMessage());
        }
        return res;
    }

    /**
     * 指标管理-编辑指标
     *
     * @return
     */
    @PostMapping(value = "/updateMineIndex")
    @ApiOperation(value = "编辑个人指标")
    public Map<String, Object> updateMineIndex(
            @ApiParam(value =
                    "INDEX_ID: 指标ID\n" +
                            "INDEX_NAME: 指标名称\n" +
                            "INDEX_DESCR: 指标描述\n" +
                            "BUILD_TYPE: 指标公式组装类型\n" +
                            "ORIGINAL_DSCR:原始运算表达式\n" +
                            "ORIGINAL_DSCR_ARRY:原始表达式数组/SQL字段\n" +
                            "INDEX_DSCR: 指标ID运算表达式\n" +
                            "INDEX_DSCR_ARRY: 指标表达式数组/SQL条件\n" +
                            "EXE_DEL: 指标删除语句\n" +
                            "EXE_INSERT: 指标插入语句\n" +
                            "EXE_SQL: 指标查询语句\n" +
                            "EXE_WHERE: 指标过滤条件\n" +
                            "INDEX_DETAILS: 指标详细描述\n" +
                            "HTML_STR: 指标计算公式Html\n" +
                            "MODIFY_USERID: 当前用户ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();

        try {
            PageData pd = applyCurrentUser(this.getPageData(param));
            pd.put("MODIFY_USERID", pd.get("ADD_USERID"));
            pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));

             String[] indexType = pd.getString("INDEX_PERIOD").split(",");
            
            pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            //组装类型BUILD_TYPE     0：计算公式组装，1：自定义SQL组装
            String EXE_SQL = "0".equals(pd.getString("BUILD_TYPE")) ? analysisSTMT(pd) : analysisSQL(pd);
            pd.put("EXE_SQL", EXE_SQL);

            /*修改指标对应数据表*/
            indicatorsMineService.updateMineRelation(pd);
            /*修改公式信息*/
            indicatorsMineService.updateFormula(pd);
             /*修改指标和个人关系表信息*/
            //indicatorsMineService.updateIndexUser(pd);
  res.put("result", "success");
            res.put("msg", "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", "编辑失败");
        }
        return res;
    }
 /**
     * 指标管理-编辑个公共指标
     *
     * @return
     */
    @PostMapping(value = "/updatePublicIndex")
    @ApiOperation(value = "编辑公共指标")
    public Map<String, Object> updatePublicIndex(
            @ApiParam(value =
                    "INDEX_ID: 指标ID\n" +
                            "INDEX_NAME: 指标名称\n" +
                            "INDEX_DESCR: 指标描述\n" +
                            "INDEX_DETAILS: 指标详情\n" +
                            "MODIFY_USERID: 当前用户ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();

        try {
            PageData pd = applyCurrentUser(this.getPageData(param));
            pd.put("MODIFY_USERID", pd.get("ADD_USERID"));
            pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            /*修改指标对应数据表*/
            indicatorsMineService.updateMineRelation(pd);
            res.put("result", "success");
            res.put("msg", "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", "编辑失败");
        }
        return res;
    }
          

    /**
     * 指标管理-提交指标
     *
     * @return
     */
    @PostMapping(value = "/submitIndexData")
    @ApiOperation(value = "提交指标")
    public Map<String, Object> submitIndexData(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pd = applyCurrentUser(this.getPageData(param));
            pd.put("PERSONAL_FLAG", "1"); //个人指标标识(0是，1否)
            pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            /*删除指标对应数据表*/
            indicatorsMineService.submitIndexData(pd);
            result.put("result", "success");
            result.put("msg", "提交指标成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "提交指标失败");
        }
        return result;
    }

    /**
     * 指标管理-历史跑批
     *
     * @return
     */
    @PostMapping(value = "/historyRunBatch")
    @ApiOperation(value = "指标历史跑批")
    public Map<String, Object> historyRunBatch(@RequestBody JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = applyCurrentUser(this.getPageData(param));
        try {
            pd.put("returnVal", ""); //接收存储过程返回结果
            pd.put("RUN_BATCH_STATUS", "1");
            result.put("result", "success");
            result.put("msg", "指标历史数据跑批进行中,请稍后查看...");
            //先将跑批状态修改为：未开始
            indicatorsMineService.updateHistoryState(pd);
            //调用指标历史数据跑批存储过程
            indicatorsMineService.indexHistoryRunBatch(pd);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "指标历史数据跑批失败");
        }
        return result;
    }

    /**
     * 指标管理-个人删除指标
     *
     * @return
     */
    @PostMapping(value = "/deleteMineIndex")
    @ApiOperation(value = "删除指标")
    public Map<String, Object> deleteMineIndex(
            @ApiParam(value =
                    "INDEX_ID: 指标ID\n" +
                            "MODIFY_USERID: 当前用户ID\n" +
                            "INDEX_CORRE_TABLE: 指标表\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();

        try {
            PageData pd = applyCurrentUser(this.getPageData(param));
            pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            /*删除指标对应数据表*/
            boolean detele1 = indicatorsMineService.deteleMineRelation(pd);
            /*删除公式信息*/
            boolean detele2 = indicatorsMineService.deleteFormula(pd);
            /*删除指标和个人关系表信息*/
            boolean detele3 = indicatorsMineService.deleteIndexUser(pd);

            if (detele1 && detele2 && detele3) { //指标相关数据删除后drop指标表
                indicatorsMineService.dropIndexTable(pd);
            }

            res.put("result", "success");
            res.put("msg", "指标删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", "指标删除失败");
        }
        return res;
    }
	

  
 /**
     * 指标管理-逻辑删除公共指标
     *
     * @return
     */
    @PostMapping(value = "/deletePublicIndex")
    @ApiOperation(value = "删除公共指标")
    public Map<String, Object> deletePublicIndex(
            @ApiParam(value =
                    "INDEX_ID: 指标ID\n" +
                            "MODIFY_USERID: 当前用户ID\n" +
                            "INDEX_CORRE_TABLE: 指标表\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();

        try {
            PageData pd = applyCurrentUser(this.getPageData(param));
            pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            /*逻辑删除公共指标对应数据表*/
            indicatorsMineService.detelePublicRelation(pd);
            res.put("result", "success");
            res.put("msg", "公共指标删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("result", "false");
            res.put("msg", "公共指标删除失败");
        }
        return res;
    }
  /**
     * 运算解析
     *
     * @param pd
     * @return
     */
    private String analysisSTMT(PageData pd) {
      StringBuilder indexSQL = new StringBuilder(); //指标组装SQL
        //pd.getString("dateStr") +
        String selectStr = " SELECT '" + pd.getString("INDEX_ID") + "',"
                + "a0.ACCOUNT_PERIOD,"
                + "a0.INDEX_DIM_CODE,"
                + "a0.INDEX_DIM_DESCR,"
                + "'@DIMENSION_FLAG' AS DIMENSION_FLAG,"
                + "'@PERIOD_FLAG' AS PERIOD_FLAG,"

                + "'system',"
                + "'" + pd.getString("ADD_DATE") + "',";
        String fromStr = " FROM ";
		
       String whereStr = "  ";

        StringBuilder builder = new StringBuilder(); //组装SQL
        String indexDscrRep = pd.getString("INDEX_DSCR").replace("×", "*").replace("÷", "/");
        String str = indexDscrRep
                .replace("(", ",")
                .replace(")", ",")
                .replace("+", ",")
                .replace("-", ",")
                .replace("*", ",")
                .replace("/", ",");
        String[] strArr = str.split(",");

        int start = 0;
        PageData pageData = new PageData();
        for (int i = 0; i < strArr.length; i++) {
            start += 1;
            if (StringUtils.isNotBlank(strArr[i])) {
                String[] indexArry = strArr[i].replace("[", ",").replace("#", ",").replace("]", ",").split(",");
                pageData.put("INDEX_ID", indexArry[0]);
                List<Map<String, String>> indexInfo_0 = indicatorsMineService.getIndexInfo(pageData);
                if (indexInfo_0.size() > 0) {
                    indexDscrRep = indexDscrRep.replace(strArr[i], "a0.INDEX_VALUE");
                    builder.append(" (SELECT ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,INDEX_VALUE FROM indicators_lib." + indexInfo_0.get(0).get("INDEX_CORRE_TABLE"));
                   // builder.append(" WHERE INDEX_ID='" + indexArry[0] + "' AND JURISDICTION='" + indexArry[1] + "' AND C_BDGLEVEL='" + indexArry[2] + "'");
                    builder.append(" AND DIMENSION_FLAG='@DIMENSION_FLAG' AND PERIOD_FLAG='@PERIOD_FLAG') a0");
                    break;
                }
            }
        }

        for (int i = start; i < strArr.length; i++) {
            if (null != strArr[i] && !"".equals(strArr[i])) {
                String[] indexArry = strArr[i].replace("[", ",").replace("#", ",").replace("]", ",").split(",");
                pageData.put("INDEX_ID", indexArry[0]);
                List<Map<String, String>> indexInfo = indicatorsMineService.getIndexInfo(pageData);
                if (indexInfo.size() > 0) {
                    indexDscrRep = indexDscrRep.replace(strArr[i], "a" + i + ".INDEX_VALUE");
                    builder.append(" LEFT JOIN (SELECT ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,INDEX_VALUE FROM indicators_lib." + indexInfo.get(0).get("INDEX_CORRE_TABLE"));
                   // builder.append(" WHERE INDEX_ID='" + indexArry[0] + "' AND JURISDICTION='" + indexArry[1] + "' AND C_BDGLEVEL='" + indexArry[2] + "'");
                    builder.append(" AND DIMENSION_FLAG='@DIMENSION_FLAG' AND PERIOD_FLAG='@PERIOD_FLAG')  a" + i);
                    builder.append(" ON a0.ACCOUNT_PERIOD=a" + i + ".ACCOUNT_PERIOD AND a0.INDEX_DIM_CODE=a" + i + ".INDEX_DIM_CODE");
                }
            }
        }
        //分组，区分周期、账期、维度
        if (builder.length() > 0) {
            builder.append(" WHERE a0.ACCOUNT_PERIOD=@ACCOUNT_PERIOD");
            builder.append(" GROUP BY ACCOUNT_PERIOD,INDEX_DIM_CODE");
            String sql = selectStr + indexDscrRep + " AS INDEX_VALUE FROM " + builder.toString();
            String[] dimnsnArray = pd.getString("INDEX_DIMNSN").split(",");
            String[] periodArray = pd.getString("INDEX_PERIOD").split(",");
            indexSQL.append("SELECT V.* FROM(");
            //标识变量，用于循环中拼接SQL
            int flag = periodArray.length * dimnsnArray.length;
            for (int i = 0; i < periodArray.length; i++) {
                String dateStr = "";
                switch (periodArray[i]) {
                    case ("1"):
                        dateStr = "to_char(@DATA_DATE, 'YYYY-MM-DD')";
                        break;
                    case ("2"):
                        dateStr = "to_char(@DATA_DATE, 'YYYY-MM')";
                        break;
                    case ("3"):
                        dateStr = "CONCAT(EXTRACT(YEAR FROM @DATA_DATE),'Q',EXTRACT(QUARTER FROM @DATA_DATE))";
                        break;
                    case ("4"):
                        dateStr = "to_char(@DATA_DATE, 'YYYY')";
                        break;
                }
                for (int j = 0; j < dimnsnArray.length; j++) {
                    flag -= 1;
                    indexSQL.append("(" + sql.replace("@ACCOUNT_PERIOD", dateStr).replace("@DIMENSION_FLAG", dimnsnArray[j]).replace("@PERIOD_FLAG", periodArray[i]) + ")");
                    if (flag > 0) {
                        indexSQL.append(" UNION ALL ");
                    }
                }
            }
            indexSQL.append(") V ORDER BY ACCOUNT_PERIOD,INDEX_DIM_CODE");
        }
        return indexSQL.toString();
    }

    /**
     * 新增指标SQL解析
     *
     * @param pd
     * @return
     */
    private String analysisSQL(PageData pd) {
        String[] columns = pd.getString("ORIGINAL_DSCR_ARRY").split(",");
        String selectStr = " SELECT '" + pd.get("INDEX_ID") + "' AS INDEX_ID,"
                + columns[0] + " AS ACCOUNT_PERIOD,"
                + columns[1] + " AS INDEX_DIM_CODE,"
                + columns[2] + " AS INDEX_DIM_DESCR,"
                + columns[3] + " AS DIMENSION_FLAG,"
                + columns[4] + " AS PERIOD_FLAG,"
                + "'system' AS ADD_USERID,"
                + "'" + pd.getString("ADD_DATE") + "' AS ADD_DATE,"
                + columns[5] + " AS INDEX_VALUE"
                + " FROM " + pd.getString("INDEX_DSCR_ARRY");
        return selectStr;
    }

    /**
     * 新增指标SQL组装查询列表数据
     *
     * @param pd
     * @return
     */
    private String analysisSQLData(PageData pd) {
        String[] columns = pd.getString("ORIGINAL_DSCR_ARRY").split(",");
        String selectStr = " SELECT '' AS INDEX_ID,"
                + "'' AS INDEX_NAME,"
                + columns[0] + " AS ACCOUNT_PERIOD,"
                + columns[1] + " AS INDEX_DIM_CODE,"
                + columns[2] + " AS INDEX_DIM_DESCR,"
                + "CONCAT('账期-',REPLACE(REPLACE(REPLACE(REPLACE(" + columns[3] + ",'1','国库'),'2','地区'),'3','核算主体'),',','，')) AS DIMENSION_FLAG,"
                + "CASE WHEN " + columns[4] + "='1' THEN '日指标'"
                + "     WHEN " + columns[4] + " ='2' THEN '月指标'"
                + "     WHEN " + columns[4] + " ='3' THEN '季指标'"
                + "     WHEN " + columns[4] + " ='4' THEN '年指标'"
                + "     ELSE '' END  AS PERIOD_FLAG,"
                + columns[5] + " AS INDEX_VALUE,"
                + "'system' AS ADD_USERID,"
                + "'" + pd.getString("ADD_DATE") + "' AS ADD_DATE"
                + " FROM " + pd.getString("INDEX_DSCR_ARRY");
        return selectStr;
    }

} ///:~
