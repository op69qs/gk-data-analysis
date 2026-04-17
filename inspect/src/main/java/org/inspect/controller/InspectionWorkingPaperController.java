// InspectionWorkingPaperController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.inspect.BaseController;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作底稿控制类
 *
 * @author Created by Samer on 2019/10/22.
 */
@Slf4j
@RestController
@Api(tags = "工作底稿控制类")
@RequestMapping(value = "/inspectionWorkingPaper", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionWorkingPaperController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionWorkingPaperService inspectionWorkingPaperService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    @Autowired
    private InspectionQuestionLedgerService inspectionQuestionLedgerService;

    /**
     * 工作底稿导出，无检查人写入
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "工作底稿导出，无检查人写入")
    @GetMapping(value = "/downLoadWorkingPaper")
    public void downLoadWorkingPaper(
            @ApiParam(value = "工作底稿ID：TASK_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> workingPaperList = inspectionWorkingPaperService.getWorkingPapersByPaperId(pd);
        String print_attachment = workingPaperList.get(0).get("PRINT_ATTACHMENT");
        String tempName_1 = print_attachment.substring(0, print_attachment.lastIndexOf("/"));
        String tempName_2 = tempName_1.substring(0, tempName_1.lastIndexOf("/"));
        String tempName = tempName_2.substring(0, tempName_2.lastIndexOf("/"));
        String zipFileName = tempName + "/工作底稿.zip";
        //FileZip.zip(tempName_1, zipFileName);
        ZipUtils.toZip(tempName_1, false);
        FileDownload.fileDownload(response, tempName_1 + ".zip", "工作底稿.zip", this.getRequest());
    }

/*    *//**
     * 根据当前用户任务ID获取台账一级二级
     *
     * @param param param
     * @return dataList
     *//*
    @ApiOperation(value = "根据当前用户任务ID获取台账一级二级")
    @PostMapping("/getQuestionLedgerLvById")
    public List<Map<String, String>> getQuestionLedgerLvById(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前用户ID：ADD_USERID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionWorkingPaperService.getQuestionLedgerLvById(pd);
        return dataList;
    }

    *//**
     * 根据当前用户任务ID获取末级台账制度及整改方式信息
     *
     * @param param param
     * @return dataList
     *//*
    @ApiOperation(value = "根据当前用户任务ID获取末级台账制度及整改方式信息")
    @PostMapping("/getRuleOpinionById")
    public List<Map<String, String>> getRuleOpinionById(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前用户ID：ADD_USERID")
            @RequestBody(required = false) JSONObject param
    ) {
        PageData pd = this.getPageData(param);
        List<Map<String, String>> dataList = inspectionWorkingPaperService.getRuleOpinionById(pd);
        return dataList;
    }*/

    /**
     * 获取结构化台账信息
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取结构化台账信息")
    @PostMapping(value = "/getStructuredContent")
    public Map<String, Object> getStructuredContent(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前用户ID：ADD_USERID")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> res = new HashMap<String, Object>();
        PageData pd = this.getPageData(param);
        pd.put("queryType", "workingPaper");
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        Map<String, String> workingPaperInfo = inspectionWorkingPaperService.getWorkingPapersByTaskIdUserId(pd);
        res = inspectionQuestionLedgerService.assembleContent_html(pd);
        if ("false".equals(res.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        if (workingPaperInfo == null || workingPaperInfo.isEmpty()) {
            res.put("CHECK_ITEMS_CONTENT", "");
            res.put("PAPER_ID", null);
        } else {
            res.put("CHECK_ITEMS_CONTENT", workingPaperInfo.get("CHECK_ITEMS_CONTENT") == null ? "" : workingPaperInfo.get("CHECK_ITEMS_CONTENT"));
            res.put("PAPER_ID", workingPaperInfo.get("PAPER_ID") == null ? "" : workingPaperInfo.get("PAPER_ID"));
        }
        res.put("TASK_TYPE_DSCR", taskInfo.get("TASK_TYPE_DSCR"));
        res.put("INSPECTED_GUOKU_DSCR", taskInfo.get("INSPECTED_GUOKU_DSCR") == null ? "" : taskInfo.get("INSPECTED_GUOKU_DSCR"));
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME");
        String timeNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
        String FILLING_DATE = DateUtil.compareTo(beginTime, timeNow, DateUtil.Pattern.YYYY_MM_DD) == -1 ?
                (DateUtil.compareTo(timeNow, endTime, DateUtil.Pattern.YYYY_MM_DD) == -1 ? timeNow : endTime) : beginTime;
        FILLING_DATE = DateUtil.transformDateToStr(
                DateUtil.transformStrToDate(FILLING_DATE, DateUtil.Pattern.YYYY_MM_DD),
                DateUtil.Pattern.YYYY_MM_DD_CN);
        res.put("INSPECTION_DATE", beginTime + "至" + endTime);
        res.put("FILLING_DATE", FILLING_DATE);
        res.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        res.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        result.put("result", "success");
        result.put("rows", res);
        result.put("msg", "查询成功");
        return result;
    }

    /**
     * 新增工作底稿
     *
     * @param param param
     */
    @ApiOperation(value = "新增工作底稿")
    @PostMapping(value = "/addWorkingPaper")
    public Map<String, Object> addWorkingPaper(
            @ApiParam(value = "当前任务ID：TASK_ID。\n" +
                    "当前工作底稿名称：TITLE。\n" +
                    "当前工作底稿ID：PAPER_ID。\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "检查组负责人：GROUP_LEADER。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "检查项目与内容：CHECK_ITEMS_CONTENT。\n")
            @RequestBody(required = false) JSONObject param
    ) throws UnsupportedEncodingException {
        Map<String, Object> res = new HashMap<String, Object>();
        String paperId = this.get32UUID();
        PageData pd = this.getPageData(param);
        pd.put("queryType", "workingPaper");
        Map<String, String> userInfo = inspectionWorkingPaperService.getUserInfoByUserId(pd);
        Map<String, String> taskInfo = inspectionWorkingPaperService.getTaskInfoByTaskId(pd);
        List<Map<String, String>> workingPaperList = inspectionWorkingPaperService.getWorkingPapersByPaperId(pd);
        res = inspectionQuestionLedgerService.assembleContent(pd);
        if ("false".equals(res.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        pd.put("PAPER_ID", pd.get("PAPER_ID") == null ? paperId : pd.get("PAPER_ID"));
        pd.put("NAME", userInfo.get("realname") == null ? "" : userInfo.get("realname"));
        pd.put("TASK_TYPE_DSCR", taskInfo.get("TASK_TYPE_DSCR"));
        pd.put("INSPECTED_GUOKU_DSCR", taskInfo.get("INSPECTED_GUOKU_DSCR") == null ? "" : taskInfo.get("INSPECTED_GUOKU_DSCR"));
        String beginTime = taskInfo.get("INSPECTION_TASK_BEGINTIME");
        String endTime = taskInfo.get("INSPECTION_TASK_ENDTIME");
        String timeNow = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
        String FILLING_DATE = DateUtil.compareTo(beginTime, timeNow, DateUtil.Pattern.YYYY_MM_DD) == -1 ?
                (DateUtil.compareTo(timeNow, endTime, DateUtil.Pattern.YYYY_MM_DD) == -1 ? timeNow : endTime) : beginTime;
        pd.put("FILLING_DATE", FILLING_DATE);
        pd.put("INSPECTION_DATE", beginTime + "至" + endTime);
        String templateName = "";
        String docName = "";
        String procCode = pd.getString("PROC_ID").substring(0, 3);
        switch (procCode) {
            case ("001"):
                templateName = "workingPaper_statis.xml";
                break;
            case ("002"):
                break;
            case ("003"):
                break;
            case ("004"):
                break;
            case ("005"):
                templateName = "workingPaper.xml";
                break;
            case ("006"):
                templateName = "workingPaper_debt.xml";
                break;
            case ("007"):
                break;
            case ("008"):
                break;
            case ("009"):
                break;
        }
        String templetFilePath = saveDir + "template/" + templateName;
        docName = pd.getString("TITLE") + "(" + (userInfo.get("realname") == null ? "姓名" : userInfo.get("realname")) + ").docx";
        //目标文件存放路径
        String targetFilePath = saveDir + pd.getString("TASK_ID") + "/工作底稿/" + docName;
        String targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/工作底稿/print/" + docName;
        /*电子档路径*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档路径*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (workingPaperList == null || workingPaperList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionWorkingPaperService.addWorkingPapers(pd);
                res.put("msg", "工作底稿新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionWorkingPaperService.updateWorkingPapersByPaperId(pd);
                res.put("msg", "工作底稿修改成功");
            }
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
            if (inspectionProcessControlService.getWorkingPapersState(pd) == 1) {
                inspectionProcessControlService.finishCurSubProcessById(pd);
                if (inspectionProcessControlService.activateFollowProc(pd)) {
                    /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
                }
            }
            PageData subPd = new PageData();
            subPd.put("TASK_ID", pd.getString("TASK_ID"));
            subPd.put("TITLE", pd.getString("TITLE"));
            subPd.put("FILLING_DATE", pd.getString("FILLING_DATE"));
            subPd.put("INSPECTED_GUOKU_DSCR", pd.getString("INSPECTED_GUOKU_DSCR"));
            subPd.put("CHECK_ITEMS_CONTENT", pd.getString("CHECK_ITEMS_CONTENT"));
            subPd.put("NAME", pd.getString("NAME"));
            if ("005".equals(procCode)) {
                String targetFilePath_sub = saveDir + pd.getString("TASK_ID") + "/工作底稿/临时文件/" + docName;
                String templatePath = saveDir + "template/workingPaper.docx";
                subPd.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT").replace("<w:br/>", "\n")
                        .replace("&#12288;&#12288;", "　　"));
                subPd.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT").replace("<w:br/>", "\n")
                        .replace("&#12288;&#12288;", "　　"));
                XWPFDocument template = new XWPFDocument(new FileInputStream(templatePath));
                BokeWordUtils.changeText(template, subPd);
                BokeWordUtils.eachTable(template.getTables().get(0).getRows(), subPd);
                FileUpload.writeToFile(template, targetFilePath_sub);
                String fileContentPath = saveDir + "template/workingPaperFormat.docx";
                String fileEndPath = saveDir + "template/workingPaperFormat_end.docx";
                String formatFileName = subPd.getString("TITLE") + "(" + (userInfo.get("realname") == null ? "姓名" : userInfo.get("realname")) + ").docx";
                String formatTargetFilePath = saveDir + subPd.getString("TASK_ID") + "/工作底稿/" + formatFileName;
                String formatTargetFilePath_NONAME = saveDir + subPd.getString("TASK_ID") + "/工作底稿/print/" + formatFileName;
                DocFileFormatPages.DocFileFormat(fileContentPath, fileEndPath, targetFilePath_sub, formatTargetFilePath, 19, 34, subPd);
                subPd.put("NAME", "");
                DocFileFormatPages.DocFileFormat(fileContentPath, fileEndPath, targetFilePath_sub, formatTargetFilePath_NONAME, 19, 34, subPd);
                FileUtil.delFolder(saveDir + pd.getString("TASK_ID") + "/工作底稿/临时文件/");
            } else {
                Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
                pd.put("NAME", "");
                pd.put("GROUP_LEADER", "");
                Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            }
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (workingPaperList == null || workingPaperList.isEmpty()) {
                res.put("msg", "工作底稿新增失败");
            } else {
                res.put("msg", "工作底稿修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

} ///:~
