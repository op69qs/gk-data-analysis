package org.inspect.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.inspect.BaseController;
import org.inspect.service.InspectionNationalDebtService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "国债巡查")
@RequestMapping(value = "/InspectionNationalDebt", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionNationalDebtController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionNationalDebtService nationalDebtService;

    @ApiOperation("国债巡查TreeList")
    @PostMapping("/getNationalDebtTreeList")
    public Map<String, Object> getInspectionGroupPage(@RequestBody(required = false) JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Integer pageNo = (Integer.parseInt(pageData.getString("pageNo")) - 1) * Integer.parseInt(pageData.getString("pageSize"));
            pageData.put("page", pageNo);
            pageData.put("rows", Integer.parseInt(pageData.getString("pageSize")));
            List<Map<String, Object>> dataList = nationalDebtService.getInspectionNationalDebtTreeList(pageData);
            result.put("rows", dataList);//rows键 存放每页记录 list
            result.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "false");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @ApiOperation("国债巡查新增")
    @PostMapping("/insertNationalDebtData")
    public Map<String, Object> insertNationalDebtData(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();

            map.put("ADD_USERID", pageData.getString("ADD_USERID"));
            map.put("CHECK_GK", pageData.getString("CHECK_GK"));
            map.put("CHECK_DATE", pageData.getString("CHECK_DATE"));
            map.put("LEVEL", pageData.getString("LEVEL"));
            map.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            //获取国库的巡查主键和检查次数
            String[] nationalDebtArray = nationalDebtService.getNationalDebtId(map);
            map.put("NATIONAL_DEBT_ID", nationalDebtArray[0]); //国债巡查表记录主键
            map.put("CHECK_COUNT", (Integer.valueOf(nationalDebtArray[1]) + 1));
            int summaryCount = nationalDebtService.selectProjectSummaryCount(map);
            if (summaryCount > 0) { //项目已经汇总
                result.put("result", "false");
                result.put("msg", "项目考评已汇总，不能新增");
                return result;
            }
            map.put("SUMMARY_STATE", "0");

            //根据用户所属的国库查询该国库所有上级国库信息

            //移除用户信息，保留考评分信息
            pageData.remove("ADD_USERID");
            pageData.remove("CHECK_GK");
            pageData.remove("SUMMARY_STATE");
            pageData.remove("CHECK_DATE");
            pageData.remove("CHECK_COUNT");
            pageData.remove("LEVEL");
            pageData.remove("NATIONAL_DEBT_ID");
            pageData.remove("PROJECT_ID");

            String projectId = this.get32UUID(); //项目表评分标识
            //将获得的参数整理成相应的表插入格式
            for (Object key : pageData.keySet()) {
                String[] projectArray = key.toString().split("_"); //截取参数id为数组
                Map<String, Object> projects = new HashMap<>();
                projects.put("PROJECT_ID", projectId);
                projects.put("NATIONAL_DEBT_ID", map.get("NATIONAL_DEBT_ID"));
                projects.put("CHECK_GK", map.get("CHECK_GK"));
                projects.put("ITEM_ID", projectArray[0]);
                projects.put("BANK_CODE", projectArray[1]);
                projects.put("SCORE", pageData.get(key));
                projects.put("CHECK_DATE", map.get("CHECK_DATE"));
                projects.put("CHECK_COUNT", map.get("CHECK_COUNT"));
                projects.put("ADD_DATE", map.get("ADD_DATE"));
                projects.put("ADD_USERID", map.get("ADD_USERID"));
                list.add(projects);
            }

            nationalDebtService.insertInspectionProjectRate(map, list);
            result.put("result", "success");
            result.put("msg", "国债巡查新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("国债巡查新增失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "国债巡查新增失败");
        }
        return result;
    }

    @ApiOperation("国债巡查编辑")
    @PostMapping("/editNationalDebtData")
    public Map<String, Object> editNationalDebtData(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();

            map.put("NATIONAL_DEBT_ID", pageData.getString("NATIONAL_DEBT_ID")); //国债巡查表记录主键
            map.put("PROJECT_ID", pageData.getString("PROJECT_ID"));
            map.put("ADD_USERID", pageData.getString("ADD_USERID"));
            map.put("CHECK_GK", pageData.getString("CHECK_GK"));
            map.put("CHECK_DATE", pageData.getString("CHECK_DATE"));
            map.put("CHECK_COUNT", pageData.getString("CHECK_COUNT"));
            map.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            int summaryCount = nationalDebtService.selectProjectSummaryCount(map);
            if (summaryCount > 0) { //项目已经汇总
                result.put("result", "false");
                result.put("msg", "项目考评已汇总，不能编辑");
                return result;
            }
            map.put("SUMMARY_STATE", "0");

            //移除用户信息，保留考评分信息
            pageData.remove("ADD_USERID");
            pageData.remove("PROJECT_ID");
            pageData.remove("NATIONAL_DEBT_ID");
            pageData.remove("CHECK_GK");
            pageData.remove("CHECK_DATE");
            pageData.remove("LEVEL");
            pageData.remove("CHECK_COUNT");

            //将获得的参数整理成相应的表插入格式
            for (Object key : pageData.keySet()) {
                String[] projectArray = key.toString().split("_"); //截取参数id为数组
                Map<String, Object> projects = new HashMap<>();
                projects.put("PROJECT_ID", map.get("PROJECT_ID"));
                projects.put("NATIONAL_DEBT_ID", map.get("NATIONAL_DEBT_ID"));
                projects.put("ITEM_ID", projectArray[0]);
                projects.put("BANK_CODE", projectArray[1]);
                projects.put("SCORE", pageData.get(key));
                projects.put("CHECK_DATE", map.get("CHECK_DATE"));
                projects.put("CHECK_GK", map.get("CHECK_GK"));
                projects.put("CHECK_COUNT", map.get("CHECK_COUNT"));
                projects.put("ADD_DATE", map.get("ADD_DATE"));
                projects.put("ADD_USERID", map.get("ADD_USERID"));
                list.add(projects);
            }
            nationalDebtService.updateInspectionProjectRate(map, list);
            result.put("result", "success");
            result.put("msg", "国债巡查编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("国债巡查编辑失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "国债巡查编辑失败");
        }
        return result;
    }

    @ApiOperation("国债巡查查看")
    @PostMapping("/queryNationalDebtData")
    public Map<String, Object> queryNationalDebtData(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);

            //后期改造，需要查询考评项目工作具体类型和描述

            List<Map<String, Object>> dataList = new ArrayList<>();
            //若SUMMARY_STATE参数不为空且值为2，则表示项目考评已汇总，需要先查看汇总评分
            if ("0".equals(pageData.get("CHECK_COUNT")) && "1,2".contains(pageData.getString("SUMMARY_STATE"))) {
                dataList = nationalDebtService.selectInspectionProjectSummary(pageData);
            } else {
                dataList = nationalDebtService.selectInspectionProjectRate(pageData);
            }

            //将查询的表数据整理成相应的对象格式返回前端
            Map<String, Object> projects = new HashMap<>();
            String projectId = ""; //项目评分唯一标识
            for (Map<String, Object> data : dataList) {
                if ("".equals(projectId)) projectId = data.get("PROJECT_ID").toString();
                projects.put(data.get("ITEM_ID") + "_" + data.get("BANK_CODE"), data.get("SCORE"));
            }
            result.put("result", "success");
            result.put("msg", "国债巡查查看成功");
            result.put("key", projectId);
            result.put("rows", projects);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("国债巡查查看失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "国债巡查查看失败");
        }
        return result;
    }

    @ApiOperation("汇总数据")
    @PostMapping("/queryProjectSummary")
    public Map<String, Object> queryProjectSummary(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            pageData.put("SUMMARY_STATE", "2"); //用于非县级国库汇总数据
            if (oConvertUtils.isEmpty(pageData.get("PARENT_GK"))) { //省级国库
                pageData.put("parentId", pageData.get("GUOKU_ID"));
            }
            Map<String, Object> clearZeroProjectItems = nationalDebtService.getClearZeroProjectItems();
            List<String> clearZeroList = (List<String>) clearZeroProjectItems.get("clear");
            List<String> sumZeroList = (List<String>) clearZeroProjectItems.get("sum");
            List<Map<String, Object>> dataList = nationalDebtService.selectProjectSummary(pageData, sumZeroList);
            StringBuilder builder = new StringBuilder();
            if (oConvertUtils.isNotEmpty(pageData.get("GUOKU_ID"))) { //当前国库
                builder.append(pageData.get("GUOKU_ID"));
            }
            if (oConvertUtils.isNotEmpty(pageData.get("PARENT_GK"))) { //上级国库
                builder.append("," + pageData.get("PARENT_GK"));
            }

            //查询考评项目评分是否有清零项
            List<Map<String, Object>> list = nationalDebtService.selectClearZeroProjects(pageData, builder.toString().split(","), clearZeroList);

            //按照页面格式整理数据
            for (Map<String, Object> data : dataList) {
                map.put(data.get("ITEM_ID") + "_" + data.get("BANK_CODE"), data.get("SCORE"));
            }

            if (list.size() > 0) { //有清零项，则修改SCORE的值为√
                for (Map<String, Object> flags : list) {
                    if (map.get(flags.get("ITEM_ID") + "_" + flags.get("BANK_CODE")) != null) {
                        map.put(flags.get("ITEM_ID") + "_" + flags.get("BANK_CODE"), "√");
                    }
                }
            }

            result.put("result", "success");
            result.put("msg", "数据汇总成功");
            result.put("rows", map);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据汇总失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "数据汇总失败");
        }
        return result;
    }

    @ApiOperation("保存汇总数据")
    @PostMapping("/insertProjectSummary")
    public Map<String, Object> insertProjectSummary(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            Map<String, Object> map = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();

            map.put("NATIONAL_DEBT_ID", pageData.getString("NATIONAL_DEBT_ID")); //国债巡查表记录主键
            map.put("CHECK_GK", pageData.getString("CHECK_GK")); //上级国库Id
            map.put("CHECK_DATE", pageData.getString("CHECK_DATE"));
            map.put("MODIFY_USERID", pageData.getString("ADD_USERID"));
            map.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            int summaryCount = nationalDebtService.selectProjectSummaryCount(map);
            if (summaryCount > 0) { //项目已经汇总
                result.put("result", "false");
                result.put("msg", "数据已汇总，不能重复汇总");
                return result;
            }
            map.put("SUMMARY_STATE", "1"); //是否已汇总：0-未汇总，1-已汇总未提交，2-已提交

            //移除用户信息，保留考评分信息
            pageData.remove("ADD_USERID");
            pageData.remove("CHECK_DATE");
            pageData.remove("CHECK_COUNT");
            pageData.remove("CHECK_GK");
            pageData.remove("LEVEL");
            pageData.remove("NATIONAL_DEBT_ID");

            String projectId = this.get32UUID(); //项目表评分标识
            //将获得的参数整理成相应的表插入格式
            for (Object key : pageData.keySet()) {
                String[] projectArray = key.toString().split("_"); //截取参数id为数组
                Map<String, Object> projects = new HashMap<>();
                projects.put("PROJECT_ID", projectId);
                projects.put("NATIONAL_DEBT_ID", map.get("NATIONAL_DEBT_ID"));
                projects.put("CHECK_GK", map.get("CHECK_GK"));
                projects.put("ITEM_ID", projectArray[0]);
                projects.put("BANK_CODE", projectArray[1]);
                projects.put("SCORE", pageData.get(key));
                projects.put("CHECK_DATE", map.get("CHECK_DATE"));
                projects.put("ADD_DATE", map.get("ADD_DATE"));
                projects.put("ADD_USERID", map.get("ADD_USERID"));
                list.add(projects);
            }

            nationalDebtService.insertInspectionProjectSummary(map, list);
            result.put("result", "success");
            result.put("msg", "保存汇总数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存汇总数据失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "保存汇总数据失败");
        }
        return result;
    }

    @ApiOperation("提交汇总数据")
    @PostMapping("/submitProjectSummary")
    public Map<String, Object> submitProjectSummary(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            PageData pageData = this.getPageData(jsonObject);

            map.put("NATIONAL_DEBT_ID", pageData.getString("NATIONAL_DEBT_ID")); //国债巡查表记录主键
            map.put("SUMMARY_STATE", "2"); //检查状态
            map.put("CHECK_DATE", pageData.getString("CHECK_DATE"));
            map.put("MODIFY_USERID", pageData.getString("ADD_USERID"));
            map.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));

            nationalDebtService.updateNationalDebtData(map);
            result.put("result", "success");
            result.put("msg", "提交汇总数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("提交汇总数据失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "提交汇总数据失败");
        }
        return result;
    }

    @ApiOperation("撤销汇总")
    @PostMapping("/revokeNationalSummary")
    public Map<String, Object> revokeNationalSummary(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            PageData pageData = this.getPageData(jsonObject);
            pageData.put("SUMMARY_STATE", "0"); //用于非县级国库汇总数据
            pageData.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            nationalDebtService.revokeNationalSummary(pageData);
            result.put("result", "success");
            result.put("msg", "撤销汇总完成");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("撤销汇总失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "撤销汇总失败");
        }
        return result;
    }

    @ApiOperation("考评评分明细")
    @PostMapping("/selectCheckDataTable")
    public Map<String, Object> selectCheckDataTable(@RequestBody JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> checkData = nationalDebtService.getCheckDataList(this.getPageData(jsonObject));
            result.put("result", "success");
            result.put("msg", "查看考评评分明细成功");
            result.put("rows", checkData.get("rows"));
            result.put("count", checkData.get("count"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查看考评评分明细失败" + e.getMessage());
            result.put("result", "false");
            result.put("msg", "查看考评评分明细失败");
        }
        return result;
    }

    @ApiOperation("导出国债考评情况表")
    @GetMapping("/exportXls")
    public void exportXls(@RequestParam("params") String params, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
        }, Feature.OrderedField); //传递的json串按照顺序转换
        PageData pageData = this.getPageData(JSONObject.parseObject(params));

        Map<String, Object> exportData = nationalDebtService.getExprotData(pageData);

        String checkDate = ""; //检查年月
        String fileName = ""; //附件名称
        if (pageData.getString("CHECK_DATE").contains("-0")) {
            checkDate = pageData.getString("CHECK_DATE").replace("-0", "年") + "月";
        } else {
            checkDate = pageData.getString("CHECK_DATE").replace("-", "年") + "月";
        }
        exportData.put("modelPath", saveDir + "template/" + "NationalDebt_Evaluation_Model.xls"); //模版路径
        if ("0".equals(pageData.getString("CHECK_COUNT"))) { //汇总数据
            exportData.put("titleName", checkDate + "储蓄国债承销团成员考评情况表--" + pageData.getString("GUOKU_DESCR")); //Excel标题
            fileName = "国债承销团成员考评表—" + pageData.getString("GUOKU_DESCR") + "(" + checkDate + ").xls";
        } else {
            exportData.put("titleName", pageData.getString("GUOKU_DESCR") + checkDate + "第" + pageData.getString("CHECK_COUNT") + "次检查"); //Excel标题
            fileName = "国债承销团成员考评表—" + pageData.getString("GUOKU_DESCR") + "(" + checkDate + "第" + pageData.getString("CHECK_COUNT") + "次检查).xls";
        }
        exportData.put("fileName", fileName);
        exportData.put("url", saveDir + "nationalDebt"); //文件生成的url
        exportData.put("filePath", exportData.get("url") + "/" + fileName); //生成的Excel存放路径
        if (exportExcel(exportData)) {
            FileDownload.fileDownload(response, exportData.get("filePath").toString(), fileName, request);
        }
    }

    /**
     * 向Excel模版中插入数据
     * @param map
     *
     * @return
     */
    private boolean exportExcel(Map<String, Object> map) {
        boolean success = true;
        String titleName = map.get("titleName").toString(); //Excel标题
        String modelPath = map.get("modelPath").toString(); //Excel的模版路径
        //判断Excel模版是否存在
        File modelFile = new File(modelPath);
        if (!modelFile.exists()) {
            return false;
        }

        //判断Excel生成路径是否存在
        File file = new File(map.get("url").toString());
        if (!file.exists()) {
            file.mkdirs();
        }

        try (FileOutputStream outStream = new FileOutputStream(new File(map.get("filePath").toString()));) {
            //操作Excel的sheet页
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(modelFile));
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);

            // 获取行数
            int rowNum = sheet.getLastRowNum();
            HSSFRow row = null; //获取行元素
            //设置标题
            sheet.getRow(0).getCell(0).setCellValue(titleName);
            for (int i = 2; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    // 获取行里面的总列数
                    int columnNum = row.getPhysicalNumberOfCells();
                    //单元格赋值从第5列开始，下标为4
                    for (int j = 4; j < columnNum; j++) {
                        HSSFCell cell = row.getCell(j);
                        String cellValue = cell.getStringCellValue();
                        //遍历参数集合，为单元格赋值
                        if (map.get(cellValue) != null) { //查询无数据则赋值
                            cell.setCellValue(map.get(cellValue).toString());
                        } else { //查询无数据则赋空值
                            cell.setCellValue("");
                        }
                    }
                }
            }
            hssfWorkbook.write(outStream);
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

}
