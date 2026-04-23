package org.indicatorsLib.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.indicatorsLib.BaseController;
import org.indicatorsLib.model.IndexTreeNode;
import org.indicatorsLib.service.IndexRelationService;
import org.indicatorsLib.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RestController
@Api(tags = "指标查询")
@RequestMapping(value = "/indexRelationController", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndexRelationController extends BaseController {

    @Autowired
    private IndexRelationService indexRelationService;

    @Autowired
    private CreateSchemeSQL createSchemeSQL;

    /**
     * 公共指标导出
     */
    @ApiOperation(value = "公共指标导出")
    @GetMapping(value = "/downloadIndexInfo")
    public void downloadIndexInfo(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = new PageData();
        String dimensionFlag = request.getParameter("dimensionFlag");
        String periodFlag = request.getParameter("periodFlag");
        String personalFlag = request.getParameter("personalFlag");
        pd.put("dimensionFlag", dimensionFlag);
        pd.put("periodFlag", periodFlag);
        pd.put("personalFlag", personalFlag);
        List<Map<String, Object>> dataList = indexRelationService.selectIndexRelationInfo(pd);
        String fileName = "公共指标";
        String[] titleAar = new String[]{
                "指标序号",
                "指标名称",
                "指标描述",
                "指标维度",
                "指标周期",
                "指标类型",
                "指标详情"
        };
        String[] colAar = new String[]{
                "IDENTITY_PROPERTY",
                "INDEX_NAME",
                "INDEX_DESCR",
                "INDEX_DIMNSN_DSCR",
                "INDEX_PERIOD_DSCR",
                "INDEX_TYPE_DSCR",
                "INDEX_DETAILS"
        };
        //创建excel的文档对象
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(fileName);
        HSSFRow row0 = sheet.createRow(0);
        row0.setHeightInPoints(30);
        Cell cell = row0.createCell(0);
        cell.setCellValue(fileName);

        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色
        font.setFontName("黑体"); //字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
        font.setItalic(false); //是否使用斜体

        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
        cell.setCellStyle(titleStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

        HSSFRow row1 = sheet.createRow(1);

        CellStyle cellStyle = wb.createCellStyle();
        CellStyle dataStyle = wb.createCellStyle();

        // 添加表头
        for (int i = 0; i < titleAar.length; i++) {
            Cell cellSub = row1.createCell(i);
            cellSub.setCellValue(titleAar[i]);
            cellSub.setCellStyle(setCellStyle(cellStyle));
            sheet.setColumnWidth(i, 256 * 30 + 184);
        }

        // 数据行
        for (int row = 0; row < dataList.size(); row++) {
            HSSFRow newRow = sheet.createRow(row + 2);
            @SuppressWarnings("unchecked")
            Map<String, Object> data = dataList.get(row);
            for (int col = 0; col < colAar.length; col++) {
                Cell dataCell = newRow.createCell(col);
                dataCell.setCellValue(data != null && data.get(colAar[col]) != null ? String.valueOf(data.get(colAar[col])) : "");
                dataCell.setCellStyle(setDataStyle(dataStyle));
            }
        }

        fileName = fileName + ".xls";
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/octet-stream;charset=UTF-8");
        wb.write(response.getOutputStream());

    }


    /**
     * 获取指标详细描述
     *
     * @return
     */
    @ApiOperation(value = "获取指标详细描述")
    @PostMapping(value = "/getIndexDetails")
    public Map<String, Object> getIndexDetails(
            @RequestBody JSONObject jsonObject
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(jsonObject);
        Map<String, Object> dataMap = indexRelationService.getIndexDetails(pd);
        result.put("result", "success");
        result.put("rows", dataMap);
        return result;
    }

    /**
     * 根据筛选条件查询指标Tree
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询指标Tree")
    @PostMapping(value = "/selectIndexRelationTree")
    public Map<String, Object> selectIndexRelationTree(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            if (oConvertUtils.isNotEmpty(pageData.get("name"))) { //若指标关键字查询不为空
                pageData.put("name", pageData.getString("name").split(",")); //按逗号截取字符串
            }


            List<String> categories = (List<String>) JSONObject.parse(pageData.getString("categories"));
            pageData.put("categories", categories);

            List<Map<String, Object>> dataList = indexRelationService.selectIndexRelationTree(pageData);
//            List<IndexTreeNode> treeNodeList = new ArrayList<>();
//            boolean initQuery = (oConvertUtils.isEmpty(pageData.get("periodFlag")) && oConvertUtils.isEmpty(pageData.get("dimensionFlag"))) ? true : false;
//            getTreeList(treeNodeList, dataList, initQuery);

            /*dataList.forEach(item->{
                item.put(String.valueOf(item.get("name")), item.get("name").toString() + "▲" + item.get("title").toString() + "▲" + item.get("flag").toString());
            });*/
            List<TreeNode> treeNodeList = TreeFilterHeaper.definedTreeFilter(dataList);

            result.put("result", "success");
            result.put("msg", "查询指标Tree成功");
            result.put("rows", treeNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标Tree失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 处理Tree数据结构
     *
     * @param treeList
     * @param metaList
     */
    private void getTreeList(List<IndexTreeNode> treeList, List<Map<String, Object>> metaList, boolean initQuery) {
        for (Map<String, Object> map : metaList) {
            String tempPid = map.get("pId").toString();
            IndexTreeNode tree = new IndexTreeNode();
            tree.setId(map.get("id").toString());
            tree.setLabel(map.get("name").toString() + "▲" + map.get("title").toString() + "▲" + map.get("flag").toString());
            tree.setParentId(tempPid);
            if (StringUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                for (Map<String, Object> children : metaList) {
                    if (tree.getId().equals(children.get("pId").toString())) {
                        IndexTreeNode childNode = new IndexTreeNode();
                        childNode.setId(children.get("id").toString());
                        childNode.setLabel(children.get("name").toString() + "▲" + children.get("title").toString() + "▲" + children.get("flag").toString());
                        childNode.setParentId(tempPid);
                        tree.getChildren().add(childNode);
                    }
                }
                if (null == tree.getChildren() || tree.getChildren().size() < 1) {
//                    treeList.remove(tree);
                }else{
                    tree.setDisabled(true); //设置父节点不可选
                }
            }
        }
    }

    /**
     * 查询指标图表Tree
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询指标图表Tree")
    @PostMapping(value = "/selectIndexEchartsTree")
    public Map<String, Object> selectIndexEchartsTree(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        PageData pageData = this.getPageData(jsonObject);
        try {
            List<Map<String, Object>> dataList = indexRelationService.selectIndexRelationTree(pageData);
            List<IndexTreeNode> treeNodeList = new ArrayList<>();
            getEchartsTreeList(treeNodeList, dataList);
            result.put("result", "success");
            result.put("msg", "查询指标图表Tree成功");
            result.put("rows", treeNodeList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标图表Tree失败");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * 处理Tree数据结构
     *
     * @param treeList
     * @param metaList
     */
    private void getEchartsTreeList(List<IndexTreeNode> treeList, List<Map<String, Object>> metaList) {
        for (Map<String, Object> map : metaList) {
            String tempPid = map.get("pId").toString();
            IndexTreeNode tree = new IndexTreeNode();
            tree.setKey(map.get("id").toString());
            tree.setValue(map.get("id").toString());
            tree.setLabel(map.get("name").toString());
            tree.setTitle(map.get("name").toString());
            tree.setParentId(tempPid);
            if (StringUtils.isEmpty(tempPid)) {
                treeList.add(tree);
                for (Map<String, Object> children : metaList) {
                    if (tree.getKey().equals(children.get("pId").toString())) {
                        IndexTreeNode childNode = new IndexTreeNode();
                        childNode.setKey(children.get("id").toString());
                        childNode.setValue(children.get("id").toString());
                        childNode.setLabel(children.get("name").toString());
                        childNode.setTitle(children.get("name").toString());
                        tree.getChildren().add(childNode);
                    }
                }
                if (null == tree.getChildren() || tree.getChildren().size() < 1) {
                    treeList.remove(tree);
                }else{
                    tree.setDisabled(true); //设置父节点不可选
                }
            }
        }
    }
/**
     * 查询指标方案table
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation(value = "查询指标方案table")
    @PostMapping(value = "/getIndicatorsTable")
    public Map<String, Object> getIndicatorsTable(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Integer pageSize = Integer.parseInt(pageData.getString("pageSize"));
            Integer pageNo = (Integer.parseInt(pageData.getString("pageNo")) - 1) * pageSize;

            String schemeSql = createSchemeSQL.getSchemeSQL(pageData);
            if (StringUtils.isBlank(schemeSql)) {
                result.put("result", "false");
                result.put("msg", "指标组装方案失败");
            } else {
                Integer count = indexRelationService.getIndicatorsCount(schemeSql);
                //加入指标检测值
                String[] columns = pageData.getString("columns").split(",");
                List<Map<String, Object>> detectionValues = indexRelationService.getDetectionValue(columns);
                //拼接指标列字段
                if (detectionValues.size() > 0) {
                    StringBuilder columnBuilder = new StringBuilder();
                    Map<String, Object> mainCondition = (Map<String, Object>) JSON.parse(pageData.get("mainCondition").toString()); //方案的查询主SQL条件
                    String price = mainCondition.get("price").toString(); //金额单位
                    schemeSql = schemeSql.replace("FORMAT(", "ROUND(");
                    //将检测值拼到指标值内
                    Map<String, Object> valueMap = new HashMap<>();
                    detectionValues.forEach(map -> {
                        valueMap.put(map.get("DETECTION_ID").toString(), map.get("DETECTION_CONDITIONS"));
                    });
                    for (int i = 0; i < columns.length; i++) {
                        if (valueMap.get(columns[i]) != null) {
                            columnBuilder.append(" CASE WHEN " + valueMap.get(columns[i]).toString().replace("@VALUE", "V." + columns[i]).replace("@UNIT", price));
                            columnBuilder.append(" THEN CONCAT('RGB_', CAST(ROUND(CAST(V." + columns[i] + " AS NUMERIC),2) AS TEXT))");
                            columnBuilder.append(" ELSE CASE WHEN COALESCE(CAST(V." + columns[i] + " AS TEXT),'')='' THEN '' ELSE CAST(ROUND(CAST(V." + columns[i] + " AS NUMERIC),2) AS TEXT) END END AS '" + columns[i] + "',");
                        } else {
                            columnBuilder.append("CASE WHEN COALESCE(CAST(V." + columns[i] + " AS TEXT),'')='' THEN '' ELSE CAST(ROUND(CAST(V." + columns[i] + " AS NUMERIC),2) AS TEXT) END AS '" + columns[i] + "',");
                        }
                    }
                    columnBuilder.append("V.ACCOUNT_DATE,V.ACCOUNT_PERIOD,V.CODE,V.GK");
                    schemeSql = schemeSql.replace("V.*", columnBuilder.toString());
                }
                schemeSql += " LIMIT " + pageSize + " OFFSET " + pageNo;
                List<Map<String, Object>> dataList = indexRelationService.getIndicatorsTable(schemeSql);
                result.put("result", "success");
                result.put("total", count);//total键 存放总记录数，必须的
                result.put("msg", "查询指标数据成功");
                result.put("rows", dataList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标数据错误");
            log.error(e.getMessage(), result);
        }
        return result;
//        Map<String, Object> result = new HashMap<>();
//        try {
//            Integer pageNo = 1;
//            Integer pageSize = 10;
//            PageData pageData = this.getPageData(jsonObject);
//            if (oConvertUtils.isNotEmpty(pageData.get("pageNo"))) {
//                pageNo = Integer.parseInt(pageData.getString("pageNo"));
//            }
//            if (oConvertUtils.isNotEmpty(pageData.get("pageSize"))) {
//                pageSize = Integer.parseInt(pageData.getString("pageSize"));
//            }
//
//            String schemeSql = createSchemeSQL.getSchemeSQL(pageData);
//            if (StringUtils.isBlank(schemeSql)) {
//                result.put("result", "false");
//                result.put("msg", "指标组装方案失败");
//            } else {
//                Integer count = indexRelationService.getIndicatorsCount(schemeSql);
//                schemeSql = schemeSql + " LIMIT " + ((pageNo - 1) * pageSize) + "," + pageSize;
//                List<Map<String, Object>> dataList = indexRelationService.getIndicatorsTable(schemeSql);
//                result.put("result", "success");
//                result.put("total", count);//total键 存放总记录数，必须的
//                result.put("msg", "查询指标数据成功");
//                result.put("rows", dataList);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put("result", "false");
//            result.put("msg", "查询指标数据错误");
//            log.error(e.getMessage(), result);
//        }
//        return result;
    }

    @ApiOperation(value = "查询指标ECharts")
    @PostMapping(value = "/getIndicatorsECharts")
    public Map<String, Object> getIndicatorsECharts(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            String schemeSql = createSchemeSQL.getSchemeSQL(pageData);
            if (StringUtils.isBlank(schemeSql)) {
                result.put("result", "false");
                result.put("msg", "查询指标ECharts数据失败");
            } else {
                LinkedHashMap<String, Object> condition = JSON.parseObject(pageData.get("mainCondition").toString(), LinkedHashMap.class);
                LinkedHashMap<String, Object> eChartsCondition = JSON.parseObject(pageData.get("eChartsCondition").toString(), LinkedHashMap.class);
                List<String> scaleList = new ArrayList<>();
                List<Map<String, Object>> dimensionData = new ArrayList<>();
                if (!"cake".equals(eChartsCondition.get("eChartsFlag").toString())) {
                    if (oConvertUtils.isNotEmpty(eChartsCondition.get("eChartsDate"))) { //以维度作为X轴刻度
                        if (oConvertUtils.isEmpty(condition.get("dimCode"))) { //维度不传代表全部维度
                            dimensionData = indexRelationService.getDimensionData(createSchemeSQL.getDimensionSQL(condition, eChartsCondition));
                            for (Map<String, Object> map : dimensionData) {
                                scaleList.add(map.get("dimCode").toString());
                            }
                        } else {
                            String[] dimCodes = condition.get("dimCode").toString().split(",");
                            for (String code : dimCodes) {
                                scaleList.add(code);
                            }
                        }
                        scaleList.add("CODE");
                    } else {
                        scaleList = setEChartsScale(condition, eChartsCondition); //以账期作为X轴刻度
                        scaleList.add("ACCOUNT_PERIOD");
                    }
                }
                Object[] dataArray = indexRelationService.getIndicatorsECharts(scaleList, eChartsCondition, schemeSql);
                result.put("result", "success");
                result.put("msg", "查询指标ECharts数据成功");
                result.put("rows", dataArray);
                result.put("scale", scaleList);
                result.put("dimension", dimensionData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标ECharts数据错误");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    @ApiOperation(value = "查询指标柱状折线图")
    @PostMapping(value = "/getColumnLineChart")
    public Map<String, Object> getColumnLineChart(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);

            String schemeSql = createSchemeSQL.getSchemeSQL(pageData);
            if (StringUtils.isBlank(schemeSql)) {
                result.put("result", "false");
                result.put("msg", "查询指标ECharts数据失败");
            } else {
                LinkedHashMap<String, Object> condition = JSON.parseObject(pageData.get("mainCondition").toString(), LinkedHashMap.class);
                //获取选择的指标——指标名称和图形
                LinkedHashMap<String, Object> inedexNameMap = JSON.parseObject(pageData.get("indexName").toString(), LinkedHashMap.class);
                LinkedHashMap<String, Object> eChartsCondition = JSON.parseObject(pageData.get("eChartsCondition").toString(), LinkedHashMap.class);
                eChartsCondition.putAll(inedexNameMap);
                List<String> scaleList = new ArrayList<>();
                if (oConvertUtils.isEmpty(eChartsCondition.get("direction")) && oConvertUtils.isNotEmpty(eChartsCondition.get("eChartsDate"))) { //横轴显示国库
                    if(oConvertUtils.isNotEmpty(condition.get("dimCode"))){
                        List<String> codeList = Arrays.asList(condition.get("dimCode").toString().split(","));
                        scaleList = new ArrayList<>(codeList);
                    }
                    scaleList.add("CODE");
                } else if (oConvertUtils.isNotEmpty(eChartsCondition.get("direction")) && oConvertUtils.isEmpty(eChartsCondition.get("eChartsDate"))) { //横轴显示账期
                    scaleList = setEChartsScale(condition, eChartsCondition);
                    scaleList.add("ACCOUNT_PERIOD");
                }else{
                    if(eChartsCondition.containsKey("direction") && !eChartsCondition.containsKey("eChartsDate")){
                        scaleList.add("CODE");
                    }else{
                        scaleList.add("ACCOUNT_PERIOD");
                    }
                }
                Object[] dataArray = indexRelationService.getColumnLineChart(scaleList, eChartsCondition, schemeSql);
                result.put("result", "success");
                result.put("msg", "查询指标柱状折线ECharts数据成功");
                result.put("rows", dataArray);
                result.put("scale", scaleList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询指标ECharts数据错误");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    @ApiOperation(value = "获取两个日期区间的日期")
    @PostMapping(value = "/getDateInterval")
    public Map<String, Object> getDateInterval(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
            result.put("result", "success");
            result.put("msg", "获取两个日期区间的日期成功");
            result.put("rows", setEChartsScale(map, null).toArray());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "获取两个日期区间的日期错误");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    @ApiOperation(value = "查询选择的维度信息")
    @PostMapping(value = "/getDimensionSelect")
    public Map<String, Object> getDimensionSelect(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Map<String, String>> dataList = indexRelationService.getDimensionSelectById(jsonObject.getString("dimCode").split(","));
            result.put("result", "success");
            result.put("msg", "查询选择的维度信息成功");
            result.put("rows", dataList);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", "查询选择的维度信息错误");
            log.error(e.getMessage(), result);
        }
        return result;
    }

    /**
     * eCharts账期刻度
     *
     * @param map
     * @return
     */
    private List<String> setEChartsScale(Map<String, Object> map, Map<String, Object> eChartsCondition) {
        List<String> list = new ArrayList<>();
        String periodFlag = map.get("periodFlag").toString();
        String startDate = ""; //起始日期
        String endDate = ""; //结束日期
        if (oConvertUtils.isEmpty(map.get("startDate")) && oConvertUtils.isEmpty(map.get("endDate"))) {
            if (eChartsCondition == null) {
                eChartsCondition = new HashMap<>();
                eChartsCondition.put("direction", "");
            }
            Map<String, Object> dateMap = indexRelationService.getMinDateAndMaxDate(createSchemeSQL.getAccountPeriodSql(map, eChartsCondition));
            if (dateMap != null && dateMap.size() > 0) {
                startDate = dateMap.get("START_DATE").toString();
                endDate = dateMap.get("END_DATE").toString();
            }
        } else {
            startDate = map.get("startDate").toString();
            endDate = map.get("endDate").toString();
        }

        if ("1".equals(periodFlag)) { //日指标
            if (startDate.equals(endDate)) {
                list.add(startDate);
            } else {
                LocalDate lStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate lEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long days = ChronoUnit.DAYS.between(lStartDate, lEndDate);
                Stream.iterate(lStartDate, day -> {
                    return day.plusDays(1);
                }).limit(days + 1).forEach(date -> {
                    list.add(date.toString());
                });
            }
        } else if ("2".equals(periodFlag)) { //月指标
            if (startDate.equals(endDate)) {
                list.add(startDate);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate startMonth = DateUtil.transformStrToDate(startDate, DateUtil.Pattern.YYYY_MM).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endMonth = DateUtil.transformStrToDate(endDate, DateUtil.Pattern.YYYY_MM).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long months = ChronoUnit.MONTHS.between(startMonth, endMonth);
                Stream.iterate(startMonth, month -> {
                    return month.plusMonths(1);
                }).limit(months + 1).forEach(date -> {
                    list.add(date.format(formatter));
                });
            }
        } else if ("3".equals(periodFlag)) { //季指标
            String[] startQuarter = startDate.split("Q");
            String[] endQuarter = endDate.split("Q");
            if (startQuarter[0].equals(endQuarter[0])) { //同年内季度
                if (startQuarter[1].equals(endQuarter[1])) {
                    list.add(startQuarter[0] + "年" + getQuarterMap().get(startQuarter[1]));
                } else {
                    int abs = Math.abs(Integer.valueOf(endQuarter[1]) - Integer.valueOf(startQuarter[1]));
                    int yearAdd = Integer.valueOf(startQuarter[0]); //起始年份
                    int quarterAdd = Integer.valueOf(startQuarter[1]); //起始季度
                    for (int i = 0; i <= abs; i++) {
                        list.add(yearAdd + "年" + getQuarterMap().get(String.valueOf(quarterAdd)));
                        if (quarterAdd == 4) {//第四季度
                            yearAdd++;
                            quarterAdd = 1;
                        } else {//第一季度、第二季度、第三季度
                            quarterAdd++;
                        }
                    }
                }
            } else {
                int years = Integer.valueOf(endQuarter[0]) - Integer.valueOf(startQuarter[0]);
                int yearAdd = Integer.valueOf(startQuarter[0]); //起始年份
                int quarterAdd = Integer.valueOf(startQuarter[1]); //起始季度
                int lastYear = Integer.valueOf(endQuarter[0]); //结束年份
                int lastQuarter = Integer.valueOf(endQuarter[1]); //结束季度
                for (int i = 0; i <= years; i++) {
                    for (String key : getQuarterMap().keySet()) {
                        list.add(yearAdd + "年" + getQuarterMap().get(String.valueOf(quarterAdd)));
                        if (quarterAdd == 4) {
                            yearAdd++;
                            quarterAdd = 1;
                            break;
                        } else {
                            quarterAdd++;
                        }
                        if (yearAdd == lastYear && quarterAdd == lastQuarter) {
                            list.add(yearAdd + "年" + getQuarterMap().get(String.valueOf(quarterAdd)));
                            return list;
                        }
                    }
                }
            }
        } else if ("4".equals(periodFlag)) { //年指标
            if (startDate.equals(endDate)) {
                list.add(map.get("startDate").toString());
            } else {
                int startYear = Integer.valueOf(startDate); //起始年份
                int endYear = Integer.valueOf(endDate); //截止年份
                for (int i = 0; i <= (endYear - startYear); i++) {
                    list.add(String.valueOf((startYear + i)));
                }
            }
        }
        return list;
    }

    /**
     * 季度map
     *
     * @return
     */
    private static Map<String, String> getQuarterMap() {
        Map<String, String> map = new HashMap<>();
        map.put("1", "第一季度");
        map.put("2", "第二季度");
        map.put("3", "第三季度");
        map.put("4", "第四季度");
        return map;
    }

    private static CellStyle setCellStyle(CellStyle cellStyle) {
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }

    private static CellStyle setDataStyle(CellStyle dataStyle) {
        dataStyle.setWrapText(true);
        dataStyle.setBorderBottom(CellStyle.BORDER_THIN); // 底部边框
        dataStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        dataStyle.setBorderLeft(CellStyle.BORDER_THIN);  // 左边边框
        dataStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        dataStyle.setBorderRight(CellStyle.BORDER_THIN); // 右边边框
        dataStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        dataStyle.setBorderTop(CellStyle.BORDER_THIN); // 上边边框
        dataStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);  // 设置单元格水平方向对其方式
        dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 设置单元格垂直方向对其方式
        return dataStyle;
    }

}
