// InspectionStatisticsTableController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.inspect.BaseController;
import org.inspect.service.InspectionStatisticsTableService;
import org.inspect.service.InspectionTaskService;
import org.inspect.util.CreateExcel_2;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Created by Samer on 2019/10/29.
 */
@Slf4j
@Api(tags = "被查国库统计表")
@RestController
@RequestMapping(value = "/inspectionStatisticsTable", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionStatisticsTableController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    /**
     * 查询当前任务问题台账中的一级问题分类
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "查询当前任务问题台账中的一级问题分类")
    @PostMapping(value = "/getCurTaskQuestion_1")
    public List<Map<String, Object>> getCurTaskQuestion_1(
            @ApiParam("当前任务ID：TASK_ID \n")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, Object>> dataList = inspectionStatisticsTableService.getCurTaskQuestion_1(pd);
        return dataList;
    }


    /**
     * 获取被查国库统计表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取被查国库统计表")
    @PostMapping(value = "/getStatisticsTable")
    public List<Map<String, Object>> getStatisticsTable(
            @ApiParam("当前任务ID：TASK_ID \n" +
                    "拼接报表表头字段数组：queryCols[]" +
                    "拼接报表表头字段数组1：queryCols_1[]")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        /*String[] queryColsT1 = (String[])JSONArray.parse(pd.getString("queryCols"));
        String[] queryColsT2 = (String[])JSONArray.parse(pd.getString("queryCols_1"));
        String[] queryCols = new String[queryColsT1.length + queryColsT2.length];
        System.arraycopy(queryColsT2, 0, queryCols, 0, queryColsT2.length);
        System.arraycopy(queryColsT1, queryColsT2.length-1, queryCols, 0, queryColsT1.length);
        pd.put("queryCols", queryCols);*/
        pd.put("columns", JSONArray.parse(pd.getString("queryCols")));
        List<Map<String, Object>> dataList = inspectionStatisticsTableService.getStatisticsTable(pd);
        return dataList;
    }

    /**
     * 下载被查国库统计表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "下载被查国库统计表")
    @GetMapping(value = "/downLoadStatisticsTable")
    public void downLoadStatisticsTable(
            @ApiParam("当前任务ID：TASK_ID \n" +
                    "拼接报表表头字段数组：queryCols[]")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        String fileName = "业务量和发现问题汇总表.xls";
        String title = "业务量和发现问题汇总表";
        PageData pd = this.getPageData(param);
        String taskId = pd.getString("TASK_ID");
        PageData subPd = new PageData();
        subPd.put("INSPECTION_TASK_ID", taskId);
        List<Map<String, Object>> taskDataList = inspectionTaskService.getInspectionTaskData(subPd);
        String taskType = taskDataList.get(0).get("INSPECTION_TASK_TYPE").toString();
        String filepath = saveDir + "/" + pd.getString("TASK_ID") + "/";
        String[] queryCols = pd.getString("queryCols").split(",|，");
        pd.put("columns", queryCols);
        /*1-7*/
        List<String> titlelist = new ArrayList<>();
        /*问题一类*/
        List<String> titlelist_2 = new ArrayList<>();
        /*问题一类统计项目*/
        List<String> titlelist_3 = new ArrayList<>();
        if( "005".equals(taskType)){
            titlelist.add("被查国库");
            titlelist.add("凭证（张）");
            titlelist.add("附件（张）");
            titlelist.add("总账（户）");
            titlelist.add("分户账（户）");
            titlelist.add("报表（份）");
            titlelist.add("登记簿（册）");
            for (int i = 7, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }
        if( "001".equals(taskType) ){
            titlelist.add("被查国库");
            titlelist.add("报表（份）");
            titlelist.add("登记薄（册）");
            titlelist.add("分析报告（份）");
            titlelist.add("其他资料（份）");

            titlelist_2.add("国库收支存统计报表国库收支存统计报表");
            titlelist_2.add("退库报表");
            titlelist_2.add("计息核对");

            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");
            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");
            titlelist_3.add("一致");
            titlelist_3.add("不一致");
            titlelist_3.add("部分一致");

            for (int i = 14, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }
        if( "006".equals(taskType) ){
            titlelist.add("被查国库");
            for (int i = 1, len = queryCols.length; i < len; i += 3) {
                String QUESTION_ID = queryCols[i].substring(0, queryCols[i].lastIndexOf("_"));
                String QUESTION_DSCR = inspectionStatisticsTableService.getQuestionDscrById(QUESTION_ID);
                titlelist_2.add(QUESTION_DSCR);
                titlelist_3.add("问题总数");
                titlelist_3.add("现场整改");
                titlelist_3.add("限期整改");
            }
        }
        List<Map<String, Object>> dataList = inspectionStatisticsTableService.getStatisticsTable(pd);
        CreateExcel_2.createInspectStatisticsTable(title, filepath, fileName, titlelist, titlelist_2, titlelist_3, queryCols, dataList, taskType);
        FileDownload.fileDownload(response, filepath + fileName, fileName, this.getRequest());
    }



} ///:~
