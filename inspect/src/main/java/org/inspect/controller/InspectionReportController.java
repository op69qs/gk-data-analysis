// InspectionWorkingPaperController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.*;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 检查报告
 *
 * @author Created by Samer on 2019/10/22.
 */
@Slf4j
@RestController
@Api(tags = "检查报告")
@RequestMapping(value = "/inspectionReport", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionReportController extends BaseController {

   final static String STRING_CSS =
            "&#12288;&#12288;";

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionReportService inspectionReportService;

    @Autowired
    private InspectionIssueListService inspectionIssueListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionStatisticsTableService inspectionStatisticsTableService;

    /**
     * 检查报告导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "检查报告导出")
    @GetMapping(value = "/downLoadReportList")
    public void downLoadIssueList(
            @ApiParam(value = "当前任务流程：REPORT_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, String> issueList = inspectionReportService.getReportById(pd);
        String filePath = issueList.get("PRINT_ATTACHMENT");
        String fileName = issueList.get("ATTACHMENT_NAME");
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


    /**
     * 获取结构化检查报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化检查报告")
    @PostMapping(value = "/getStructuredReport")
    public Map<String, Object> getStructuredReport(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> res = new HashMap<String, Object>();
        String GroupMembers = "";
        PageData pd = this.getPageData(param);
        pd.put("IS_REPORT", "0");
        /*获取当前任务信息*/
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        res = inspectionQuestionLedgerService.assembleContent_html(pd);
        if ("false".equals(res.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        res.put("TASK_TYPE_DSCR", taskInfo.get("TASK_TYPE_DSCR"));
        res.put("INSPECTED_GUOKU_DSCR", taskInfo.get("INSPECTED_GUOKU_DSCR") == null ? "" : taskInfo.get("INSPECTED_GUOKU_DSCR"));
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME") + " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
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
        /*检查组人员*/
        List<Map<String, String>> taskGroupUserInfo = inspectionReportService.getGroupUsers(pd);
        res.put("GROUP_LEADER", taskGroupUserInfo.get(0).get("NAME"));
        res.put("LEADER_DUTY", taskGroupUserInfo.get(0).get("DUTY"));
        res.put("CHIEF_MAN", taskGroupUserInfo.get(1).get("NAME"));
        res.put("CHIEF_DUTY", taskGroupUserInfo.get(1).get("DUTY"));
        for (int i = 2, len = taskGroupUserInfo.size(); i < len; i++) {
            GroupMembers += taskGroupUserInfo.get(i).get("NAME") + (i == len - 1 ? "" : "，");
        }
        res.put("GROUP_MEMBERS", GroupMembers);
        //检查报告问题组装
        Map<String, String> ReportListInfo = inspectionReportService.getReportById(pd);
        if (ReportListInfo == null || ReportListInfo.isEmpty()) {
            res.put("REPORT_ID", null);
            res.put("INSPECT_DATE", "");
            res.put("INSPECT_TYPE", "");
            res.put("INSPECT_ITEMS", new String[]{});
            res.put("NOTES", "");
            res.put("ACCT_VOUCHER", "");
            res.put("ACCT_ATTACH", "");
            res.put("TOTAL_ACCOUNT", "");
            res.put("DIVIDED_ACCOUNT", "");
            res.put("REPORT_NUM", "");
            res.put("REGISTY_TYPE_NUM", "");
            res.put("TOTAL_EVALUATION", "请输入总体评价");
            res.put("OPINION_SUGGESTION", "请输入整改意见或建议");
            res.put("DIRECTOR", "");
            res.put("DEPARTMENT_HEAD", "");
            res.put("AFTER_HEAD", "");
        } else {
            List<Map<String, String>> inspectItemsList = new ArrayList<>();
            String[] tempItemsArr = ReportListInfo.get("INSPECT_ITEMS_DSCR").split("##");
            for( int i = 0; i < tempItemsArr.length; i++ ){
                String[] subItemsArr = tempItemsArr[i].split("@@");
                Map<String, String> inspectItems = new HashMap<>();
                inspectItems.put("id", subItemsArr[0]);
                inspectItems.put("name", subItemsArr[1]);
                inspectItemsList.add(inspectItems);
            }
            res.put("inspectItemsList", inspectItemsList);

            res.put("REPORT_ID", ReportListInfo.get("REPORT_ID") == null ? "" : ReportListInfo.get("REPORT_ID"));
            res.put("INSPECT_DATE", ReportListInfo.get("INSPECT_DATE") == null ? "" : ReportListInfo.get("INSPECT_DATE"));
            res.put("INSPECT_TYPE", ReportListInfo.get("INSPECT_TYPE") == null ? "" : ReportListInfo.get("INSPECT_TYPE"));
            res.put("INSPECT_ITEMS", ReportListInfo.get("INSPECT_ITEMS") == null ? "" : ReportListInfo.get("INSPECT_ITEMS").split(",|，"));
            res.put("NOTES", ReportListInfo.get("NOTES") == null ? "" : ReportListInfo.get("NOTES"));
            res.put("ACCT_VOUCHER", ReportListInfo.get("ACCT_VOUCHER") == null ? "" : ReportListInfo.get("ACCT_VOUCHER"));
            res.put("ACCT_ATTACH", ReportListInfo.get("ACCT_ATTACH") == null ? "" : ReportListInfo.get("ACCT_ATTACH"));
            res.put("TOTAL_ACCOUNT", ReportListInfo.get("TOTAL_ACCOUNT") == null ? "请输入总体评价" : ReportListInfo.get("TOTAL_ACCOUNT"));
            res.put("DIVIDED_ACCOUNT", ReportListInfo.get("DIVIDED_ACCOUNT") == null ? "请输入整改意见或建议" : ReportListInfo.get("DIVIDED_ACCOUNT"));
            res.put("REPORT_NUM", ReportListInfo.get("REPORT_NUM") == null ? "" : ReportListInfo.get("REPORT_NUM"));
            res.put("REGISTY_TYPE_NUM", ReportListInfo.get("REGISTY_TYPE_NUM") == null ? "" : ReportListInfo.get("REGISTY_TYPE_NUM"));
            res.put("TOTAL_EVALUATION", (ReportListInfo.get("TOTAL_EVALUATION") == null || "".equals(ReportListInfo.get("TOTAL_EVALUATION")))
                    ? "请输入总体评价" : ReportListInfo.get("TOTAL_EVALUATION"));
            res.put("OPINION_SUGGESTION", (ReportListInfo.get("OPINION_SUGGESTION") == null || "".equals(ReportListInfo.get("OPINION_SUGGESTION")))
                    ? "请输入整改意见或建议" : ReportListInfo.get("OPINION_SUGGESTION"));
            res.put("DIRECTOR", ReportListInfo.get("DIRECTOR") == null ? "" : ReportListInfo.get("DIRECTOR"));
            res.put("DEPARTMENT_HEAD", ReportListInfo.get("DEPARTMENT_HEAD") == null ? "" : ReportListInfo.get("DEPARTMENT_HEAD"));
            res.put("AFTER_HEAD", ReportListInfo.get("AFTER_HEAD") == null ? "" : ReportListInfo.get("AFTER_HEAD"));
        }

        res.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        res.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        result.put("result", "success");
        result.put("rows", res);
        result.put("msg", "查询成功");
        return result;
    }

    /**
     * 新增检查报告
     *
     * @param param param
     */
    @ApiOperation(value = "新增检查报告")
    @PostMapping(value = "/addReportList")
    public Map<String, Object> addReportList(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前检查报告名称：TITLE。\n" +
                    "当前检查报告ID：REPORT_ID。\n" +
                    "检查业务期：INSPECT_DATE。\n" +
                    "检查项目ID（逗号隔开）：INSPECT_ITEMS。\n" +
                    "需要说明事项：NOTES。\n" +
                    "会计凭证：ACCT_VOUCHER。\n" +
                    "附件：ACCT_ATTACH。\n" +
                    "总账：TOTAL_ACCOUNT。\n" +
                    "分帐户：DIVIDED_ACCOUNT。\n" +
                    "报表份数：REPORT_NUM。\n" +
                    "登记簿种数量：REGISTY_TYPE_NUM。\n" +
                    "总体评价：TOTAL_EVALUATION。\n" +
                    "意见及建议：OPINION_SUGGESTION。\n" +
                    "被查国库主任（副主任）：DIRECTOR。\n" +
                    "被查国库部门负责人：DEPARTMENT_HEAD。\n" +
                    "事后监督部门负责人：AFTER_HEAD。\n" +
                    "被查国库意见：INSPECTED_TRE_OPINIOS。\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n")
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        String checkTick = "[√]";
        String GroupMembers = "";
        Map<String, Object> res = new HashMap<String, Object>();
        String reportId = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("IS_REPORT", "0");
        Map<String, String> userInfo = inspectionWorkingPaperService.getUserInfoByUserId(pd);
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        Map<String, String> ReportListInfo = inspectionReportService.getReportById(pd);
        res = inspectionQuestionLedgerService.assembleContent(pd);
        if ("false".equals(res.get("result"))) {
            return res;
        }
        /*表头信息*/
        pd.put("REPORT_ID", pd.get("REPORT_ID") == null ? reportId : pd.get("REPORT_ID"));
        pd.put("TASK_TYPE_DSCR", taskInfo.get("TASK_TYPE_DSCR"));
        pd.put("INSPECTED_GUOKU_DSCR", taskInfo.get("INSPECTED_GUOKU_DSCR") == null ? "" : taskInfo.get("INSPECTED_GUOKU_DSCR"));
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME") + " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        String timeNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
        String FILLING_DATE = endTime;
        FILLING_DATE = DateUtil.transformDateToStr(
                DateUtil.transformStrToDate(FILLING_DATE, DateUtil.Pattern.YYYY_MM_DD),
                DateUtil.Pattern.YYYY_MM_DD_CN);
        pd.put("FILLING_DATE", FILLING_DATE);
        pd.put("START_END_DATE",
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
        );
        List<Map<String, String>> taskGroupUserInfo = inspectionReportService.getGroupUsers(pd);
        pd.put("GROUP_LEADER", taskGroupUserInfo.get(0).get("NAME"));
        pd.put("GROUP_LEADER_FT", taskGroupUserInfo.get(0).get("NAME"));
        pd.put("LEADER_DUTY", taskGroupUserInfo.get(0).get("DUTY"));
        pd.put("CHIEF_MAN", taskGroupUserInfo.get(0).get("NAME"));
        pd.put("CHIEF_MAN_FT", taskGroupUserInfo.get(0).get("NAME"));
        pd.put("CHIEF_DUTY", taskGroupUserInfo.get(0).get("DUTY"));
        for (int i = 2, len = taskGroupUserInfo.size(); i < len; i++) {
            GroupMembers += taskGroupUserInfo.get(i).get("NAME") + (i == len - 1 ? "" : "，");
        }
        pd.put("GROUP_MEMBERS", GroupMembers);
        String[] checkedItems = ((String) pd.get("INSPECT_ITEMS")).split(",|，");
        /*检查项目组装*/
        PageData pd_2 = new PageData();
        String ENUM_TYPE_ID = "";
        String templateName = "";
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        pd.put("procCode", procCode);
        switch (procCode) {
            case ("001"):
                ENUM_TYPE_ID = "16";
                templateName = "inspectionReport_statis.xml";
                break;
            case ("002"):
                break;
            case ("003"):
                break;
            case ("004"):
                break;
            case ("005"):
                ENUM_TYPE_ID = "15";
                templateName = "inspectionReport.xml";
                break;
            case ("006"):
                templateName = "inspectionReport_debt.xml";
                break;
            case ("007"):
                break;
            case ("008"):
                break;
            case ("009"):
                break;
        }
        pd_2.put("ENUM_TYPE_ID", ENUM_TYPE_ID);
        String ITEMSCHECKED = "";
        String INSPECT_ITEMS_DSCR = "";
        if (ENUM_TYPE_ID != null && !"".equals(ENUM_TYPE_ID)) {
            List<Map<String, String>> inspectItems = inspectionReportService.getInspectItemsByTypeId(pd_2);
            if (checkedItems != null && checkedItems.length != 0) {
                for (int i = 0, len = inspectItems.size(); i < len; i++) {
                    INSPECT_ITEMS_DSCR += inspectItems.get(i).get("id") + "@@" + inspectItems.get(i).get("name") + (i == (len - 1) ? "" : "##");
                    if (Arrays.asList(checkedItems).contains(inspectItems.get(i).get("id"))) {
                        //pd.put("ITEMSCHECKED" + inspectItems.get(i).get("id"), checkTick);
                        ITEMSCHECKED += STRING_CSS + checkTick + inspectItems.get(i).get("name") + (i == (len - 1) ? "" : "<w:br/>");
                    } else {
                        //pd.put("ITEMSCHECKED" + inspectItems.get(i).get("id"), "[  ]");
                        ITEMSCHECKED += STRING_CSS +  "[  ]" + inspectItems.get(i).get("name") + (i == (len - 1) ? "" : "<w:br/>");
                    }
                }
                pd.put("ITEMSCHECKED", ITEMSCHECKED);
                pd.put("INSPECT_ITEMS_DSCR", INSPECT_ITEMS_DSCR);
            }
        }
        String templetFilePath = saveDir + "template/" + templateName;
        //String templetFilePath_NONAME = saveDir + "template/inspectionReport_NONAME.xml";
        String docName = pd.getString("TITLE") + ".docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/检查报告/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/检查报告/print/" + docName;
        /*电子文档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档路径*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        pd.put("SUB_GROUP_LEADER", pd.getString("GROUP_LEADER"));
        pd.put("SUB_GROUP_MEMBERS", pd.getString("GROUP_MEMBERS"));
        try {
            if (ReportListInfo == null || ReportListInfo.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionReportService.addInspectionReport(pd);
                res.put("msg", "检查报告新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionReportService.updateReportById(pd);
                res.put("msg", "检查报告修改成功");
            }
            /*统计汇总表信息同步*/
            inspectionStatisticsTableService.callStatisticsTableSYNC_TYPE(pd);
            /*检查报告完成，检查档案与被查国库统计表也标记为完成。*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishReportProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            //将xml模板转换为后缀为doc文件
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            if ("001".equals(procCode)) {
                pd.put("DIRECTOR", "");
                pd.put("DEPARTMENT_HEAD", "");
                pd.put("SUB_GROUP_LEADER", "");
                pd.put("SUB_GROUP_MEMBERS", "");
            }
            if ("006".equals(procCode)) {
                pd.put("SUB_GROUP_LEADER", "");
            }
            if ("005".equals(procCode)) {
                pd.put("DIRECTOR", "");
                pd.put("DEPARTMENT_HEAD", "");
                pd.put("AFTER_HEAD", "");
                pd.put("GROUP_LEADER_FT", "");
                pd.put("CHIEF_MAN_FT", "");
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            if (ReportListInfo == null || ReportListInfo.isEmpty()) {
                res.put("msg", "检查报告新增失败");
            } else {
                res.put("msg", "检查报告修改失败");
            }
            e.printStackTrace();
            res.put("result", "false");
        }
        return res;
    }

} ///:~
