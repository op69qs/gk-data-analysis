// InspectionWorkingPaperController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.inspect.BaseController;
import org.inspect.service.InspectionIssueListService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.service.InspectionQuestionLedgerService;
import org.inspect.service.InspectionWorkingPaperService;
import org.inspect.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 问题清单
 *
 * @author Created by Samer on 2019/10/22.
 */
@Slf4j
@RestController
@Api(tags = "问题清单")
@RequestMapping(value = "/inspectionIssueList", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionIssueListController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionIssueListService inspectionIssueListService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;


    /**
     * 获取国债检查记录表列表信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取国债检查记录表列表信息")
    @PostMapping(value = "/getDebtRecordList")
    public Map<String, Object> getDebtRecordList(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("IS_LIST", "0");
        /*获取当前任务信息*/
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        Map<String, String> IssueListInfo = inspectionIssueListService.getDepartmentHeadByTaskId(pd);
        List<Map<String, Object>> contentList = inspectionQuestionLedgerService.assembleContentAndRule(pd);
        if (contentList == null || contentList.isEmpty()) {
            res.put("result", "false");
            res.put("msg", "信息查询失败");
            return res;
        }
        if (IssueListInfo == null || IssueListInfo.isEmpty()) {
            res.put("ISSUE_ID", null);
            res.put("contentList", contentList);
        } else {
            String ISSUE_ID = IssueListInfo.get("ISSUE_ID") == null ? "" : IssueListInfo.get("ISSUE_ID");
            res.put("ISSUE_ID", ISSUE_ID);
            pd.put("ISSUE_ID", ISSUE_ID);
            List<Map<String, Object>> dataList = inspectionIssueListService.getDebtRecordIssue(pd);
            res.put("contentList", dataList);
        }
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME") + " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        String FILLING_DATE = endTime;
        res.put("START_END_DATE",
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
        );
        result.put("result", "success");
        result.put("rows", res);
        result.put("msg", "查询成功");
        return result;
    }


    /**
     * 新增国债检查记录表
     *
     * @param param param
     */
    @ApiOperation(value = "新增国债检查记录表")
    @PostMapping(value = "/addDebtRecordList")
    public Map<String, Object> addDebtRecordList(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前问题清单名称：TITLE。\n" +
                    "当前问题清单ID：ISSUE_ID。\n" +
                    "被查国库名称：INSPECTED_GUOKU_DSCR。\n" +
                    "问题描述信息列表：contentList。\n" +
                    "检查人员：NAME。\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n")
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> res = new HashMap<String, Object>();
        String issueId = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("ISSUE_ID", pd.get("ISSUE_ID") == null ? issueId : pd.get("ISSUE_ID"));
         /*获取当前任务信息*/
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        Map<String, String> IssueListInfo = inspectionIssueListService.getDepartmentHeadByTaskId(pd);
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME") + " " + DateUtil.getCurrentDateStr(DateUtil.Pattern.HH_MM_SS);
        pd.put("START_END_DATE",
                DateUtil.transformDateToStr(DateUtil.transformStrToDate(beginTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
                        + "至"
                        + DateUtil.transformDateToStr(DateUtil.transformStrToDate(endTime, DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD_CN)
        );
        String templateName = "国债业务实地检查记录表.docx";
        String docFileName = saveDir + "template/recordSheet.docx" ;
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/国债业务实地检查记录表/" + templateName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/国债业务实地检查记录表/print/" + templateName;
        List<Map<String, String>> arr = (List<Map<String, String>>) JSONArray.parse(pd.getString("contentList"));
        pd.put("contentList", arr);
        List<String[]> tdList = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            String[] dataStr = new String[2];
            dataStr[0] = arr.get(i).get("SOURCE_DATE");
            dataStr[1] = arr.get(i).get("QUESTION_CONTENT");
            tdList.add(dataStr);
        }
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", templateName);
        try {
            //将xml模板转换为后缀为doc文件
            if (IssueListInfo == null || IssueListInfo.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionIssueListService.addIssueList(pd);
                inspectionIssueListService.addDebtRecordIssue(pd);
                res.put("msg", "国债检查记录表新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionIssueListService.updateIssueListById(pd);
                inspectionIssueListService.delDebtRecordIssue(pd);
                inspectionIssueListService.addDebtRecordIssue(pd);
                res.put("msg", "国债检查记录表修改成功");
            }
            /*本流程完成状态更新，后续流程激活*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            PageData pdSub = new PageData();
            pdSub.put("TITLE", pd.getString("TITLE"));
            pdSub.put("INSPECTED_GUOKU_DSCR", pd.getString("INSPECTED_GUOKU_DSCR"));
            pdSub.put("START_END_DATE", pd.getString("START_END_DATE"));
            pdSub.put("NAME", pd.getString("NAME"));
            XWPFDocument template = new XWPFDocument(new FileInputStream(docFileName));
            BokeWordUtils.changeText(template, pdSub);
            BokeWordUtils.changeTable(template, null, tdList, 0);
            FileUpload.writeToFile(template, targetFilePath);

            XWPFDocument template_2 = new XWPFDocument(new FileInputStream(docFileName));
            pdSub.put("NAME", "");
            BokeWordUtils.changeText(template_2, pdSub);
            BokeWordUtils.changeTable(template_2, null, tdList, 0);
            FileUpload.writeToFile(template_2, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (IssueListInfo == null || IssueListInfo.isEmpty()) {
                res.put("msg", "国债检查记录表新增失败");
            } else {
                res.put("msg", "国债检查记录表修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }


    /**
     * 问题清单导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "问题清单导出")
    @GetMapping(value = "/downLoadIssueList")
    public void downLoadIssueList(
            @ApiParam(value = "当前任务流程：ISSUE_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, String> issueList = inspectionIssueListService.getIssueListById(pd);
        String filePath = issueList.get("PRINT_ATTACHMENT");
        String fileName = issueList.get("ATTACHMENT_NAME");
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


    /**
     * 获取结构化问题清单
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化问题清单")
    @PostMapping(value = "/getStructuredIssueList")
    public Map<String, Object> getStructuredIssueList(
            @ApiParam(value = "当前任务ID：TASK_ID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("IS_LIST", "0");
        /*获取当前任务信息*/
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        /*检查组人员组装*/
        assembleTaskGroupUserInfo_html(pd);
        Map<String, String> IssueListInfo = inspectionIssueListService.getDepartmentHeadByTaskId(pd);
        res = inspectionQuestionLedgerService.assembleContent_html(pd);
        if ("false".equals(res.get("result"))) {
            res.put("result", "false");
            res.put("msg", "信息查询失败");
            return res;
        }
        res.put("GROUP_USER_NAMES", (String) pd.get("GROUP_USER_NAMES"));
        res.put("NAME", (String) pd.get("NAME"));
        res.put("result", "success");
        if (IssueListInfo == null || IssueListInfo.isEmpty()) {
            res.put("DEPARTMENT_HEAD", "");
            res.put("ISSUE_ID", null);
        } else {
            res.put("DEPARTMENT_HEAD", IssueListInfo.get("DEPARTMENT_HEAD") == null ? "" : IssueListInfo.get("DEPARTMENT_HEAD"));
            res.put("AFTER_HEAD", IssueListInfo.get("AFTER_HEAD") == null ? "" : IssueListInfo.get("AFTER_HEAD"));
            res.put("ISSUE_ID", IssueListInfo.get("ISSUE_ID") == null ? "" : IssueListInfo.get("ISSUE_ID"));
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
        res.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        res.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        return res;
    }


    /**
     * 新增问题清单
     *
     * @param param param
     */
    @ApiOperation(value = "新增问题清单")
    @PostMapping(value = "/addIssueList")
    public Map<String, Object> addIssueList(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前问题清单名称：TITLE。\n" +
                    "事后监督部门负责人：AFTER_HEAD。\n" +
                    "当前问题清单ID：ISSUE_ID。\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "被查国库部门负责人：DEPARTMENT_HEAD。\n")
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> res = new HashMap<String, Object>();
        String issueId = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("IS_LIST", "0");
        Map<String, String> userInfo = inspectionWorkingPaperService.getUserInfoByUserId(pd);
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        Map<String, String> IssueListInfo = inspectionIssueListService.getDepartmentHeadByTaskId(pd);
        res = inspectionQuestionLedgerService.assembleContent(pd);
        if ("false".equals(res.get("result"))) {
            res.put("result", "false");
            res.put("msg", "信息查询失败");
            return res;
        }
        /*检查组人员组装*/
        assembleTaskGroupUserInfo(pd);
        pd.put("ISSUE_ID", pd.get("ISSUE_ID") == null ? issueId : pd.get("ISSUE_ID"));
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
        String templetFilePath = saveDir + "template/issueList.xml";
        String docName = pd.getString("TITLE") + ".docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/问题清单/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/问题清单/print/" + docName;
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (IssueListInfo == null || IssueListInfo.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionIssueListService.addIssueList(pd);
                res.put("msg", "问题清单新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionIssueListService.updateIssueListById(pd);
                res.put("msg", "问题清单修改成功");
            }
            /*本流程完成状态更新，后续流程激活*/
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            pd.put("DEPARTMENT_HEAD", "");
            pd.put("AFTER_HEAD", "");
            pd.put("NAME", "");
            pd.put("GROUP_USER_NAMES", "");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            if (IssueListInfo == null || IssueListInfo.isEmpty()) {
                res.put("msg", "问题清单新增失败");
            } else {
                res.put("msg", "问题清单修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 检查组组装HTML
     *
     * @param pd
     */
    public void assembleTaskGroupUserInfo_html(PageData pd) {
        String groupUserNames = "";
        String leader = "";
        List<Map<String, String>> taskGroupUserInfo = inspectionIssueListService.getTaskGroupUserInfo(pd);
        if (taskGroupUserInfo != null && !taskGroupUserInfo.isEmpty()) {
            for (int i = 0, len = taskGroupUserInfo.size(); i < len; i++) {
                String leaderName = taskGroupUserInfo.get(i).get("LEADER");
                if (leaderName != null && !"".equals(leaderName)) {
                    leader += leaderName + "\n";
                }
                groupUserNames += taskGroupUserInfo.get(i).get("DUTIES_NAME") + "\n";
            }
        }
        pd.put("NAME", leader);
        pd.put("GROUP_USER_NAMES", groupUserNames);
    }

    /**
     * 检查组组装
     *
     * @param pd
     */
    public void assembleTaskGroupUserInfo(PageData pd) {
        String groupUserNames = "";
        String leader = "";
        List<Map<String, String>> taskGroupUserInfo = inspectionIssueListService.getTaskGroupUserInfo(pd);
        if (taskGroupUserInfo != null && !taskGroupUserInfo.isEmpty()) {
            for (int i = 0, len = taskGroupUserInfo.size(); i < len; i++) {
                String leaderName = taskGroupUserInfo.get(i).get("LEADER");
                if (leaderName != null && !"".equals(leaderName)) {
                    leader += leaderName + "<w:br/>";
                }
                groupUserNames += taskGroupUserInfo.get(i).get("DUTIES_NAME") + "<w:br/>";
            }
        }
        pd.put("NAME", leader);
        pd.put("GROUP_USER_NAMES", groupUserNames);
    }


} ///:~
