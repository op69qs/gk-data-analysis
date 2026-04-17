// InspectionEnforceLawReportController.java

package org.inspect.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.inspect.BaseController;
import org.inspect.service.InspectionEnforceLawWorkingPaperService;
import org.inspect.service.InspectionEnforceLawReportService;
import org.inspect.service.InspectionProcessControlService;
import org.inspect.service.InspectionQuestionLedgerService;
import org.inspect.util.DateUtil;
import org.inspect.util.FileDownload;
import org.inspect.util.PageData;
import org.inspect.util.Xml2XmlDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by Samer on 2019/11/21.
 */
@Slf4j
@Api(tags = "执法检查报告")
@RestController
@RequestMapping(value = "/enforceLawReport", produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionEnforceLawReportController extends BaseController {

    @Value("${TEMPLATE_FILE_PATH}")
    private String saveDir;

    @Autowired
    private InspectionEnforceLawWorkingPaperService inspectionEnforceLawWorkingPaperService;

    @Autowired
    protected InspectionQuestionLedgerService inspectionQuestionLedgerService;

    @Autowired
    private InspectionEnforceLawReportService inspectionEnforceLawReportService;

    @Autowired
    private InspectionProcessControlService inspectionProcessControlService;

    /**
     * 新增执法检查报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "新增执法检查报告")
    @PostMapping(value = "/addEnforceLawReport")
    public Map<String, Object> addEnforceLawReport(
            @ApiParam(value = "执法检查报告ID：REPORT_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n" +
                    "当前流程ID：PROC_ID。\n" +
                    "当前子流程ID：PROC_SUB_ID。\n" +
                    "当前用户ID：ADD_USERID。\n" +
                    "被查机构名称：CHECKED_ORG_DSCR \n" +
                    "检查机构名称： CHECK_ORG_DSCR\n" +
                    "检查业务名称: INSPECT_ITEM_DSCR\n" +
                    "检查时间: INSPECT_DATE\n" +
                    "检查人员签名: MEMBER\n" +
                    "小组主查人签名: GROUP_LEADER\n" +
                    "执法职能部门意见: LAW_DEP_OPINION\n" +
                    "执法职能部门负责人: LAW_DEP_HEAD\n" +
                    "分管行领导意见: LAW_LEADER_OPINION\n" +
                    "分管行领导: LAW_LEADER\n"
            )
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        PageData pd = this.getPageData(param);
        String REPORT_ID = this.get32UUID();
        String endTime = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS);
        String FILLING_DATE = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN);
        String docName = "",
                targetFilePath = "",
                targetFilePath_NONAME = "",
                templateName = "",
                templetFilePath = "";

        docName = "中国人民银行（" + pd.getString("CHECK_ORG_DSCR") + "）执法检查报告.docx";
        templateName = "enforceLawReport.xml";
        templetFilePath = saveDir + "template/" + templateName;
        //目标文件存放路径
        targetFilePath = saveDir + pd.getString("TASK_ID") + "/执法检查报告/" + docName;
        targetFilePath_NONAME = saveDir + pd.getString("TASK_ID") + "/执法检查报告/print/" + docName;

        Map<String, Object> paperList = inspectionEnforceLawReportService.getRecord(pd);
        if (paperList == null || paperList.isEmpty()) {
            pd.put("REPORT_ID", REPORT_ID);
        }

        Map<String, Object> enforceLawDataList = inspectionEnforceLawWorkingPaperService.getRecord(pd);
        res = inspectionQuestionLedgerService.assembleContent(pd);
        if ("false".equals(res.get("result"))) {
            return res;
        }
        if (enforceLawDataList != null && !enforceLawDataList.isEmpty()) {
            pd.put("CHECKED_EXECUTE", enforceLawDataList.get("CHECKED_EXECUTE"));
            pd.put("ENFORCE_LAW_BASE", enforceLawDataList.get("ENFORCE_LAW_BASE"));
        } else {
            pd.put("ENFORCE_LAW_BASE", "");
            pd.put("CHECKED_EXECUTE", "");
        }

        pd.put("SUGGESTION_BASIS", pd.getString("SUGGESTION_BASIS") == null ? "" : pd.getString("SUGGESTION_BASIS"));
        pd.put("FILLING_DATE", FILLING_DATE);
        /*电子档检查人写入*/
        pd.put("PAPER_ATTACHMENT", targetFilePath);
        /*导出文档检查人不写入*/
        pd.put("PRINT_ATTACHMENT", targetFilePath_NONAME);
        pd.put("ATTACHMENT_NAME", docName);
        try {
            //将xml模板转换为后缀为doc文件
            if (paperList == null || paperList.isEmpty()) {
                pd.put("ADD_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                inspectionEnforceLawReportService.addRecord(pd);
                res.put("msg", "执法检查报告新增成功");
            } else {
                pd.put("MODIFY_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
                pd.put("MODIFY_USERID", pd.getString("ADD_USERID"));
                inspectionEnforceLawReportService.editRecord(pd);
                res.put("msg", "执法检查报告修改成功");
            }
            //当前流程完成，尝试将大流程状态改为完成。
            pd.put("FINISH_TIME", endTime);
            inspectionProcessControlService.finishCurSubProcessById(pd);
            if (inspectionProcessControlService.activateFollowProc(pd)) {
                /*res.put("msg", res.get("msg") + inspectionProcessControlService.getProcDescById(pd) + "流程结束，后续流程已激活");*/
            }
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath);
            /*导出文件不包含检查人名*/
            pd.put("LAW_DEP_HEAD", "");
            pd.put("LAW_LEADER", "");
            pd.put("FILLING_DATE", "    年  月  日");
            Xml2XmlDoc.xml2XmlDoc(pd, templetFilePath, targetFilePath_NONAME);
            res.put("result", "success");
        } catch (Exception e) {
            e.printStackTrace();
            if (paperList == null || paperList.isEmpty()) {
                res.put("msg", "执法检查报告新增失败");
            } else {
                res.put("msg", "执法检查报告修改失败");
            }
            res.put("result", "false");
        }
        return res;
    }

    /**
     * 获取执法检查报告
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "获取执法检查报告")
    @PostMapping(value = "/getEnforceLawReport")
    public Map<String, Object> getEnforceLawReport(
            @ApiParam(value = "执法检查报告ID：REPORT_ID \n" +
                    "当前任务ID：TASK_ID\n" +
                    "用户所在部门ID：DEPARTMENT\n")
            @RequestBody(required = false) JSONObject param
    ) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> dataList = new HashMap<>();
        PageData pd = this.getPageData(param);
        pd.put("IS_REPORT", "0");
        Map<String, Object> enforceLawDataList = inspectionEnforceLawWorkingPaperService.getRecord(pd);
        Map<String, Object> contentHtml = inspectionQuestionLedgerService.assembleContent_html(pd);
        if ("false".equals(contentHtml.get("result"))) {
            res.put("msg", "信息查询失败");
            return res;
        }
        dataList = inspectionEnforceLawReportService.getRecord(pd);
        if (dataList == null || dataList.isEmpty()) {
            dataList = new HashMap<>();
            dataList.put("FILLING_DATE", DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_CN));
            dataList.put("SUGGESTION_BASIS", "");
        }
        dataList.put("NOT_OTHER_CONTENT", pd.getString("NOT_OTHER_CONTENT"));
        dataList.put("OTHER_CONTENT", pd.getString("OTHER_CONTENT"));
        if (enforceLawDataList != null && !enforceLawDataList.isEmpty()) {
            dataList.put("CHECKED_EXECUTE", enforceLawDataList.get("CHECKED_EXECUTE"));
            dataList.put("ENFORCE_LAW_BASE", enforceLawDataList.get("ENFORCE_LAW_BASE"));
        } else {
            dataList.put("ENFORCE_LAW_BASE", "");
            dataList.put("CHECKED_EXECUTE", "");
        }
        res.put("result", "success");
        res.put("rows", dataList);
        return res;
    }

    /**
     * 执法检查报告导出
     *
     * @param param
     * @param response
     */
    @ApiOperation(value = "执法检查报告导出")
    @GetMapping(value = "/downLoadEnforceLawReport")
    public void downLoadEnforceLawReport(
            @ApiParam(value = "执法检查报告ID：REPORT_ID")
            @RequestBody(required = false) JSONObject param,
            HttpServletResponse response
    ) throws Exception {
        PageData pd = this.getPageData(param);
        Map<String, Object> dataList = inspectionEnforceLawReportService.getRecord(pd);
        String fileName = dataList.get("ATTACHMENT_NAME").toString();
        String filePath = dataList.get("PRINT_ATTACHMENT").toString();
        FileDownload.fileDownload(response, filePath, fileName, this.getRequest());
    }


} ///:~
