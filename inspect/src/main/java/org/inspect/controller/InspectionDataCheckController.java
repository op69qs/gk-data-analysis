package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据核对
 *
 * @author Created by dj on 2019/11/12.
 */
@Slf4j
@RestController
@Api(tags = "统计数据核对情况检查表")
@RequestMapping(value = "/inspectionDataCheck", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionDataCheckController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionDataCheckService inspectionDataCheckService;

    @Autowired
    private InspectionIssueListService inspectionIssueListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;
    @Autowired
    private InspectionCheckAccountSheetService inspectionCheckAccountSheetService;

    /**
     * 打包下载统计数据核对情况检查表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "打包下载统计数据核对情况检查表")
    @GetMapping(value = "/downLoadCheckList")
    public void downLoadCheckList(
            @ApiParam("当前任务ID：TASK_ID \n")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        String zipFileName = saveDir  + pd.getString("TASK_ID") + "/国库统计数据核对情况检查表/print/";
        ZipUtils.toZip(zipFileName,  false);
        FileDownload.fileDownload(response, zipFileName + ".zip", "国库统计数据核对情况检查表.zip", this.getRequest());
    }


    /**
     * 获取结构化数据核对表
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化数据核对表")
    @PostMapping(value = "/getStructuredDataCheck")
    public Map<String, Object> getStructuredDataCheck(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        /*获取当前任务信息*/
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        res.put("TASK_TYPE_DSCR", taskInfo.get("TASK_TYPE_DSCR"));
        res.put("INSPECTED", taskInfo.get("INSPECTED_GUOKU_DSCR") == null ? "" : taskInfo.get("INSPECTED_GUOKU_DSCR"));
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getCurTreCodeByTaskId(pd);
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME")+ " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        String timeNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
        String FILLING_DATE = endTime;
        FILLING_DATE = DateUtil.transformDateToStr(
                DateUtil.transformStrToDate(FILLING_DATE, DateUtil.Pattern.YYYY_MM_DD),
                DateUtil.Pattern.YYYY_MM_DD_CN);
        res.put("FILLING_DATE", FILLING_DATE);
        res.put("START_END_DATE",
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
        );
        //数据核查问题组装
        Map<String, String> ReportListInfo = inspectionDataCheckService.getDataCheckById(pd);
        if (ReportListInfo == null || ReportListInfo.isEmpty()) {
            res.put("SHEET_ID", null);
            res.put("TIME_YEAR", "");
            res.put("TIME_MONTH", "");
            res.put("TIME_DAY", "");
            res.put("REPORT_NUM", "");
            res.put("REGISTER_BOOK", "");
            res.put("ANA_NUM", "");
            res.put("OTHER_NUM", "");
            res.put("DEPARTMENT_HEAD", "");
            res.put("LEADER", "");
            res.put("MEMBER", "");
            res.put("VALUE_1", "0");
            res.put("VALUE_2", "0");
            res.put("VALUE_3", "0");
            res.put("VALUE_4", "0");
            res.put("VALUE_5", "0");
            res.put("VALUE_6", "0");
            res.put("VALUE_7", "0");
            res.put("VALUE_8", "0");
            res.put("VALUE_9", "0");
            res.put("VALUE_10", "0");
        } else {
            res.put("SHEET_ID", ReportListInfo.get("SHEET_ID") == null ? "" : ReportListInfo.get("SHEET_ID"));
            res.put("TIME_YEAR", ReportListInfo.get("TIME_YEAR") == null ? "" : ReportListInfo.get("TIME_YEAR"));
            res.put("TIME_MONTH", ReportListInfo.get("TIME_MONTH") == null ? "" : ReportListInfo.get("TIME_MONTH").split(",|，"));
            res.put("TIME_DAY", ReportListInfo.get("TIME_DAY") == null ? "" : ReportListInfo.get("TIME_DAY"));
            res.put("REPORT_NUM", ReportListInfo.get("REPORT_NUM") == null ? "" : ReportListInfo.get("REPORT_NUM"));
            res.put("REGISTER_BOOK", ReportListInfo.get("REGISTER_BOOK") == null ? "" : ReportListInfo.get("REGISTER_BOOK"));
            res.put("ANA_NUM", ReportListInfo.get("ANA_NUM") == null ? "" : ReportListInfo.get("ANA_NUM"));
            res.put("OTHER_NUM", ReportListInfo.get("OTHER_NUM") == null ? "" : ReportListInfo.get("OTHER_NUM"));
            res.put("DEPARTMENT_HEAD", ReportListInfo.get("DEPARTMENT_HEAD") == null ? "" : ReportListInfo.get("DEPARTMENT_HEAD"));
            res.put("LEADER", ReportListInfo.get("LEADER") == null ? "0" : ReportListInfo.get("LEADER"));
            res.put("MEMBER", ReportListInfo.get("MEMBER") == null ? "0" : ReportListInfo.get("MEMBER"));
            res.put("VALUE_1", ReportListInfo.get("VALUE_1") == null ? "0" : ReportListInfo.get("VALUE_1"));
            res.put("VALUE_2", ReportListInfo.get("VALUE_2") == null ? "0" : ReportListInfo.get("VALUE_2"));
            res.put("VALUE_3", ReportListInfo.get("VALUE_3") == null ? "0" : ReportListInfo.get("VALUE_3"));
            res.put("VALUE_4", ReportListInfo.get("VALUE_4") == null ? "0" : ReportListInfo.get("VALUE_4"));
            res.put("VALUE_5", ReportListInfo.get("VALUE_5") == null ? "0" : ReportListInfo.get("VALUE_5"));
            res.put("VALUE_6", ReportListInfo.get("VALUE_6") == null ? "0" : ReportListInfo.get("VALUE_6"));
            res.put("VALUE_7", ReportListInfo.get("VALUE_7") == null ? "0" : ReportListInfo.get("VALUE_7"));
            res.put("VALUE_8", ReportListInfo.get("VALUE_8") == null ? "0" : ReportListInfo.get("VALUE_8"));
            res.put("VALUE_9", ReportListInfo.get("VALUE_9") == null ? "0" : ReportListInfo.get("VALUE_9"));
            res.put("VALUE_10", ReportListInfo.get("VALUE_10") == null ? "0" : ReportListInfo.get("VALUE_10"));
        }
        res.put("result", "success");
        return res;
    }

    /**
     * 新增数据核对
     *
     * @param param param
     */
    @ApiOperation(value = "新增数据核对")
    @PostMapping(value = "/addDataCheckList")
    public Map<String, Object> addDataCheckList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        String SHEET_ID = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("SHEET_ID", SHEET_ID);
        pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        pd.put("MODIFY_USERID","");
        pd.put("MODIFY_DATE","");
        String templetFilePath = saveDir + "template/dataCheck.xml"  ;
        String docName = pd.get("INSPECTED") + "国库统计数据核对情况检查表.docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/国库统计数据核对情况检查表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/国库统计数据核对情况检查表/print/" + docName;
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档路径*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        Integer rs = checkRepeat(pd);
        try {
            if(null != rs && rs>0){
                result.put("msg", "新增失败！已存在，请修改后重试。");
                result.put("result", "false");
            }else {
                inspectionDataCheckService.addInspectionDataCheck(pd);
                result.put("msg", "数据核对新增成功");
                result.put("result", "success");
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            pd.put("LEADER", "");
            pd.put("DEPARTMENT_HEAD", "");
            pd.put("MEMBER", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            pd.put("PROC_ID",pd.get("PROC_SUB_ID").toString().substring(0,5));
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*result.put("msg", result.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "数据核对新增失败");
            result.put("result", "false");
        }
        return result;
    }
    /**
     * 修改数据核对
     *
     * @param param param
     */
    @ApiOperation(value = "修改数据核对")
    @PostMapping(value = "/editDataCheckList")
    public Map<String, Object> editDataCheckList(
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
        String templetFilePath = saveDir + "template/dataCheck.xml"  ;
        String docName = pd.get("TASK_TYPE_DSCR") + "国库统计数据核对情况检查表.docx";
//        目标文件存放路径
//        String targetFilePath = pd.get("PAPER_ATTACHMENT").toString();
//        String targetFilePath_NONAME = pd.get("PRINT_ATTACHMENT").toString();
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/国库统计数据核对情况检查表/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/国库统计数据核对情况检查表/print/" + docName;
        try {
                pd.put("VALUE_3", pd.getString("VALUE_3") == null?"":pd.getString("VALUE_3"));
                pd.put("VALUE_6", pd.getString("VALUE_6") == null?"":pd.getString("VALUE_6"));
                pd.put("VALUE_9", pd.getString("VALUE_9") == null?"":pd.getString("VALUE_9"));
                inspectionDataCheckService.updateDataCheckById(pd);
                result.put("msg", "数据核对修改成功");
                result.put("result", "success");
                Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
                pd.put("LEADER", "");
                pd.put("DEPARTMENT_HEAD", "");
                pd.put("MEMBER", "");
                Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", "数据核对修改失败");
            result.put("result", "false");
        }

        return result;
    }
    private Integer checkRepeat(PageData pd){
        List<Map<String, Object>> data = inspectionDataCheckService.checkRepeat(pd);
        if (null != data && data.size() > 0) {
            return data.size();
        }
        return 0;
    }
    /**
     * 根据当前任务ID获取核算主体包含的国库
     *
     * @param param params.TASK_ID 当前任务ID
     * @return
     */
    @ApiOperation(value = "根据当前任务ID获取核算主体包含的国库")
    @PostMapping(value = "/getCurTreCodeByTaskId")
    public Map<String, Object> getCurTreCodeByTaskId(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionCheckAccountSheetService.getCurTreCodeByTaskId(pd);
        if( dataList == null || dataList.size() ==0 ){
            result.put("result", "false");
            result.put("msg", "查询无数据");
        } else {
            result.put("result", "success");
            result.put("rows", dataList);
        }
        return result;
    }
    /**
     *
     * 获取所有数据核对列表
     */
    @RequestMapping(value = {"/getInspectionCheck"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取所有数据核对列表")
    public Map<String, Object> getInspectionCheck(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionDataCheckService.getInspectionCheck(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "查询失败");
        }
        return result;
    }
    @RequestMapping(value = "/delInspectionCheck", method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("删除")
    public Map<String, Object> delInspectionCheck(@RequestBody(required = false) JSONObject jsonob
    ) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            inspectionDataCheckService.delInspectionCheck(pd);
            result.put("msg", "删除成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", "删除失败");
            result.put("result", "failed");
        }
        return result;
    }
    /**
     *
     * 获取根据国库和taskId获取每个被查库数据
     */
    @RequestMapping(value = {"/getInspectionCheckInspected"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取根据国库和taskId获取每个被查库数据")
    public Map<String, Object> getInspectionCheckInspected(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionDataCheckService.getInspectionCheckInspected(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }
    /**
     *
     * 获取根据国库和taskId获取每个被查库数据
     */
    @RequestMapping(value = {"/getInspectionCheckOne"}, method = RequestMethod.POST, produces = {"application/json"})
    @ApiOperation("获取每个被查库数据")
    public Map<String, Object> getInspectionCheckOne(@RequestBody(required = false) JSONObject jsonob) {
        Map<String, Object> result = new HashMap<>();
        PageData pd = new PageData(jsonob);
        try {
            List<Map<String, Object>> data = inspectionDataCheckService.getInspectionCheckOne(pd);
            result.put("rows", data);//rows键 存放每页记录 list
            result.put("msg", "查询成功");
            result.put("result", "success");
        } catch (Exception e) {
            result.put("msg", e.getMessage());
            result.put("result", "success");
        }
        return result;
    }
} ///:~
